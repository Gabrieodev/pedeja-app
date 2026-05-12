package com.example.pedeja.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedeja.R;
import com.example.pedeja.model.Categoria;
import com.example.pedeja.ui.ProdutosActivity;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.VH> {

    private final Context ctx;
    private final List<Categoria> lista;

    
    private static String getEmoji(String nome) {
        if (nome == null) return "🍽️";
        nome = nome.toLowerCase();
        if (nome.contains("lanche") || nome.contains("burger")) return "🍔";
        if (nome.contains("sushi") || nome.contains("japones")) return "🍣";
        if (nome.contains("pizza") || nome.contains("massa")) return "🍕";
        if (nome.contains("bebida")) return "🥤";
        if (nome.contains("sobremesa") || nome.contains("doce")) return "🍰";
        if (nome.contains("frango") || nome.contains("chicken")) return "🍗";
        if (nome.contains("salada")) return "🥗";
        return "🍽️";
    }

    public CategoriaAdapter(Context ctx, List<Categoria> lista) {
        this.ctx = ctx;
        this.lista = lista;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_categoria, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Categoria cat = lista.get(pos);
        h.tvEmoji.setText(getEmoji(cat.getNome()));
        h.tvNome.setText(cat.getNome());
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, ProdutosActivity.class);
            i.putExtra("categoria_id", cat.getId());
            i.putExtra("categoria_nome", cat.getNome());
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvNome;
        VH(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tvEmojiCategoria);
            tvNome = v.findViewById(R.id.tvNomeCategoria);
        }
    }
}
