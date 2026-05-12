package com.example.pedeja.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedeja.R;
import com.example.pedeja.model.Categoria;
import com.example.pedeja.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvCategoriasFull);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RetrofitClient.getApiService().getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rv.setAdapter(new FullCatAdapter(CategoriasActivity.this, response.body()));
                }
            }
            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {}
        });
    }

    
    static class FullCatAdapter extends RecyclerView.Adapter<FullCatAdapter.VH> {
        final Context ctx;
        final List<Categoria> lista;

        FullCatAdapter(Context ctx, List<Categoria> lista) {
            this.ctx = ctx;
            this.lista = lista;
        }

        private String getEmoji(String nome) {
            if (nome == null) return "🍽️";
            nome = nome.toLowerCase();
            if (nome.contains("lanche") || nome.contains("burger")) return "🍔";
            if (nome.contains("sushi") || nome.contains("japones")) return "🍣";
            if (nome.contains("pizza") || nome.contains("massa")) return "🍕";
            if (nome.contains("bebida")) return "🥤";
            if (nome.contains("sobremesa") || nome.contains("doce")) return "🍰";
            if (nome.contains("salada")) return "🥗";
            return "🍽️";
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_categoria_full, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
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
                tvEmoji = v.findViewById(R.id.tvEmojiCatFull);
                tvNome = v.findViewById(R.id.tvNomeCatFull);
            }
        }
    }
}
