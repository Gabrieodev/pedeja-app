package com.example.pedeja.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pedeja.R;
import com.example.pedeja.model.ItemPedido;
import com.example.pedeja.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhePedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_pedido);

        long pedidoId = getIntent().getLongExtra("pedido_id", 0);
        double total = getIntent().getDoubleExtra("pedido_total", 0);
        String data = getIntent().getStringExtra("pedido_data");
        String status = getIntent().getStringExtra("pedido_status");

        ((TextView) findViewById(R.id.tvTituloDetPed)).setText("Pedido #" + pedidoId);
        ((TextView) findViewById(R.id.tvDataDetPed)).setText(data != null ? data : "");
        ((TextView) findViewById(R.id.tvTotalDetPed)).setText(String.format("R$ %.2f", total));

        TextView tvStatus = findViewById(R.id.tvStatusDetPed);
        tvStatus.setText(traduzirStatus(status));
        tvStatus.setTextColor(getCorStatus(status));

        findViewById(R.id.btnVoltarDetPed).setOnClickListener(v -> finish());

        
        LinearLayout llItens = findViewById(R.id.llItensDetPed);
        RetrofitClient.getApiService().getItensPedido().enqueue(new Callback<List<ItemPedido>>() {
            @Override
            public void onResponse(Call<List<ItemPedido>> call, Response<List<ItemPedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ItemPedido item : response.body()) {
                        if (item.getPedido() != null && item.getPedido().getId() == pedidoId) {
                            LinearLayout row = new LinearLayout(DetalhePedidoActivity.this);
                            row.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(0, 0, 0, 6);
                            row.setLayoutParams(lp);

                            TextView tvNome = new TextView(DetalhePedidoActivity.this);
                            tvNome.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                            String nomeProd = item.getProduto() != null ? item.getProduto().getNome() : "Produto";
                            tvNome.setText(item.getQuantidade() + "x  " + nomeProd);
                            tvNome.setTextColor(0xFF444444);
                            tvNome.setTextSize(14);

                            TextView tvVal = new TextView(DetalhePedidoActivity.this);
                            tvVal.setText(String.format("R$ %.2f", item.getSubtotal() != null ? item.getSubtotal() : 0));
                            tvVal.setTextColor(0xFF444444);
                            tvVal.setTextSize(14);

                            row.addView(tvNome);
                            row.addView(tvVal);
                            llItens.addView(row);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ItemPedido>> call, Throwable t) {}
        });
    }

    private String traduzirStatus(String s) {
        if (s == null) return "Pendente";
        switch (s) {
            case "ENTREGUE": return "Entregue";
            case "CANCELADO": return "Cancelado";
            case "EM_PREPARO": return "Em preparo";
            case "A_CAMINHO": return "A caminho";
            default: return s;
        }
    }

    private int getCorStatus(String s) {
        if (s == null) return 0xFF888888;
        switch (s) {
            case "ENTREGUE": return 0xFF4CAF50;
            case "CANCELADO": return 0xFFF44336;
            case "EM_PREPARO": return 0xFFFF9800;
            default: return 0xFF888888;
        }
    }
}
