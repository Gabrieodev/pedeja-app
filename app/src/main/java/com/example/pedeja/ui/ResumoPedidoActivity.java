package com.example.pedeja.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pedeja.R;
import com.example.pedeja.model.*;
import com.example.pedeja.network.RetrofitClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumoPedidoActivity extends AppCompatActivity {

    private static final double TAXA_ENTREGA = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_pedido);

        SharedPreferences prefs = getSharedPreferences("pedeja_prefs", MODE_PRIVATE);
        long clienteId = prefs.getLong("cliente_id", -1);

        
        LinearLayout llItens = findViewById(R.id.llItensResumo);
        List<Produto> itens = CarrinhoManager.getInstance().getItens();
        double subtotal = 0;

        for (Produto p : itens) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 8);
            row.setLayoutParams(lp);

            TextView tvNome = new TextView(this);
            tvNome.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            tvNome.setText(p.getNome());
            tvNome.setTextColor(0xFF444444);
            tvNome.setTextSize(14);

            TextView tvValor = new TextView(this);
            tvValor.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            double total = p.getPreco() * p.getQuantidade();
            tvValor.setText(String.format("R$ %.2f", total));
            tvValor.setTextColor(0xFF444444);
            tvValor.setTextSize(14);

            row.addView(tvNome);
            row.addView(tvValor);
            llItens.addView(row);
            subtotal += total;
        }

        double totalFinal = subtotal + TAXA_ENTREGA;
        ((TextView) findViewById(R.id.tvTotalResumo)).setText(String.format("R$ %.2f", totalFinal));

        findViewById(R.id.btnVoltarResumo).setOnClickListener(v -> finish());

        Button btnConfirmar = findViewById(R.id.btnConfirmarPedido);
        btnConfirmar.setOnClickListener(v -> {
            if (clienteId == -1) {
                Toast.makeText(this, "Cliente não identificado", Toast.LENGTH_SHORT).show();
                return;
            }
            btnConfirmar.setEnabled(false);
            btnConfirmar.setText("Processando...");
            criarPedidoNaApi(clienteId, totalFinal);
        });
    }

    private void criarPedidoNaApi(long clienteId, double total) {
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setTotal(total);
        pedido.setData(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        pedido.setStatus("EM_PREPARO");

        RetrofitClient.getApiService().criarPedido(pedido).enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pedido pedidoCriado = response.body();
                    adicionarItensAoPedido(pedidoCriado.getId());
                } else {
                    Toast.makeText(ResumoPedidoActivity.this, "Erro ao criar pedido", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.btnConfirmarPedido).setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(ResumoPedidoActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                findViewById(R.id.btnConfirmarPedido).setEnabled(true);
            }
        });
    }

    private void adicionarItensAoPedido(long pedidoId) {
        List<Produto> itens = CarrinhoManager.getInstance().getItens();
        final int[] count = {0};

        if (itens.isEmpty()) {
            finalizarPedido(pedidoId);
            return;
        }

        for (Produto p : itens) {
            Pedido pedidoRef = new Pedido();
            pedidoRef.setId(pedidoId);

            Produto prodRef = new Produto();
            prodRef.setId(p.getId());

            ItemPedido item = new ItemPedido();
            item.setPedido(pedidoRef);
            item.setProduto(prodRef);
            item.setQuantidade(p.getQuantidade());

            RetrofitClient.getApiService().adicionarItem(item).enqueue(new Callback<ItemPedido>() {
                @Override
                public void onResponse(Call<ItemPedido> call, Response<ItemPedido> response) {
                    count[0]++;
                    if (count[0] == itens.size()) finalizarPedido(pedidoId);
                }
                @Override
                public void onFailure(Call<ItemPedido> call, Throwable t) {
                    count[0]++;
                    if (count[0] == itens.size()) finalizarPedido(pedidoId);
                }
            });
        }
    }

    private void finalizarPedido(long pedidoId) {
        
        getSharedPreferences("pedeja_prefs", MODE_PRIVATE)
                .edit().putLong("ultimo_pedido_id", pedidoId).apply();

        CarrinhoManager.getInstance().limpar();

        Intent i = new Intent(this, ConfirmacaoActivity.class);
        i.putExtra("pedido_id", pedidoId);
        startActivity(i);
        finish();
    }
}
