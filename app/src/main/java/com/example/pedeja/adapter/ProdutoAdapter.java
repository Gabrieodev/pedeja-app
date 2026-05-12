package com.example.pedeja.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedeja.R;
import com.example.pedeja.model.Produto;
import com.example.pedeja.ui.DetalheProdutoActivity;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.VH> {

    public interface OnProdutoClick {
        void onClick(Produto p);
    }

    private final Context ctx;
    private final List<Produto> lista;
    private final int layoutRes;
    private OnProdutoClick listener;

    public ProdutoAdapter(Context ctx, List<Produto> lista, int layoutRes) {
        this.ctx = ctx;
        this.lista = lista;
        this.layoutRes = layoutRes;
    }

    public void setListener(OnProdutoClick l) { this.listener = l; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(layoutRes, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Produto p = lista.get(pos);
        if (h.tvNome != null) h.tvNome.setText(p.getNome());
        if (h.tvPreco != null) h.tvPreco.setText(String.format("R$ %.2f", p.getPreco()));
        if (h.tvEmoji != null) h.tvEmoji.setText(getEmoji(p.getNome()));

        h.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(p);
            } else {
                abrirDetalhe(p);
            }
        });

        if (h.btnVer != null) {
            h.btnVer.setOnClickListener(v -> abrirDetalhe(p));
        }
    }

    private void abrirDetalhe(Produto p) {
        Intent i = new Intent(ctx, DetalheProdutoActivity.class);
        i.putExtra("produto_id", p.getId());
        i.putExtra("produto_nome", p.getNome());
        i.putExtra("produto_preco", p.getPreco());
        ctx.startActivity(i);
    }

    private String getEmoji(String nome) {
        if (nome == null) return "🍽️";
        String n = nome.toLowerCase();
        if (n.contains("burger") || n.contains("hambur") || n.contains("x-b") || n.contains("lanche")) return "🍔";
        if (n.contains("pizza")) return "🍕";
        if (n.contains("sushi") || n.contains("temaki") || n.contains("combo")) return "🍣";
        if (n.contains("batata") || n.contains("frita")) return "🍟";
        if (n.contains("frango") || n.contains("chicken")) return "🍗";
        if (n.contains("salada")) return "🥗";
        if (n.contains("refri") || n.contains("refrigerante")) return "🥤";
        if (n.contains("suco")) return "🧃";
        if (n.contains("sorvete")) return "🍦";
        if (n.contains("brownie") || n.contains("chocolate")) return "🍫";
        if (n.contains("sobremesa") || n.contains("doce")) return "🍰";
        if (n.contains("agua") || n.contains("água")) return "💧";
        if (n.contains("cafe") || n.contains("café")) return "☕";
        return "🍽️";
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNome, tvPreco, tvEmoji;
        Button btnVer;
        VH(View v) {
            super(v);
            tvNome = v.findViewById(R.id.tvNomeProdutoDestaque);
            tvPreco = v.findViewById(R.id.tvPrecoProdutoDestaque);
            tvEmoji = v.findViewById(R.id.tvEmojiProdutoDestaque);
            btnVer = v.findViewById(R.id.btnVerProduto);
        }
    }
}
