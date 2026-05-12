package com.example.pedeja.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedeja.R;
import com.example.pedeja.model.CarrinhoManager;
import com.example.pedeja.model.Produto;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private TextView tvTotal;
    private CarrinhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        tvTotal = findViewById(R.id.tvTotalCarrinho);

        findViewById(R.id.btnVoltarCarrinho).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvCarrinho);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarrinhoAdapter(this, CarrinhoManager.getInstance().getItens());
        rv.setAdapter(adapter);

        atualizarTotal();

        findViewById(R.id.btnContinuarCarrinho).setOnClickListener(v -> {
            if (CarrinhoManager.getInstance().getItens().isEmpty()) {
                Toast.makeText(this, "Carrinho vazio!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, EnderecoActivity.class));
            }
        });
    }

    private void atualizarTotal() {
        tvTotal.setText(String.format("R$ %.2f", CarrinhoManager.getInstance().getTotal()));
    }

    class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.VH> {
        final Context ctx;
        final List<Produto> lista;

        CarrinhoAdapter(Context ctx, List<Produto> lista) {
            this.ctx = ctx;
            this.lista = lista;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_carrinho, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            Produto p = lista.get(pos);
            h.tvNome.setText(p.getNome());
            h.tvQtd.setText(p.getQuantidade() + "x  R$ " + String.format("%.2f", p.getPreco()));
            h.tvEmoji.setText(getEmoji(p.getNome()));
            h.tvRemover.setOnClickListener(v -> {
                CarrinhoManager.getInstance().removerProduto(p);
                lista.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, lista.size());
                atualizarTotal();
            });
        }

        @Override
        public int getItemCount() { return lista.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tvNome, tvQtd, tvEmoji, tvRemover;
            VH(View v) {
                super(v);
                tvNome = v.findViewById(R.id.tvNomeCarrinho);
                tvQtd = v.findViewById(R.id.tvQtdCarrinho);
                tvEmoji = v.findViewById(R.id.tvEmojiCarrinho);
                tvRemover = v.findViewById(R.id.btnRemoverCarrinho);
            }
        }
    }

    private String getEmoji(String nome) {
        if (nome == null) return "🍽️";
        nome = nome.toLowerCase();
        if (nome.contains("burger") || nome.contains("hambur")) return "🍔";
        if (nome.contains("sushi")) return "🍣";
        if (nome.contains("pizza")) return "🍕";
        if (nome.contains("batata") || nome.contains("frita")) return "🍟";
        if (nome.contains("frango")) return "🍗";
        if (nome.contains("salada")) return "🥗";
        if (nome.contains("refri") || nome.contains("suco")) return "🥤";
        return "🍽️";
    }
}
