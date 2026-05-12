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
import com.example.pedeja.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        long categoriaId = getIntent().getLongExtra("categoria_id", -1);
        String categoriaNome = getIntent().getStringExtra("categoria_nome");

        TextView tvTitulo = findViewById(R.id.tvTituloCategoria);
        tvTitulo.setText(categoriaNome != null ? categoriaNome : "Produtos");

        findViewById(R.id.btnVoltarProd).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvProdutos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RetrofitClient.getApiService().getProdutos().enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Produto> todos = response.body();
                    List<Produto> filtrados = new ArrayList<>();

                    for (Produto p : todos) {
                        if (categoriaId == -1 || (p.getCategoria() != null && categoriaId == p.getCategoria().getId())) {
                            filtrados.add(p);
                        }
                    }

                    rv.setAdapter(new ListaProdAdapter(ProdutosActivity.this, filtrados));
                }
            }
            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Toast.makeText(ProdutosActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ListaProdAdapter extends RecyclerView.Adapter<ListaProdAdapter.VH> {
        final Context ctx;
        final List<Produto> lista;

        ListaProdAdapter(Context ctx, List<Produto> lista) {
            this.ctx = ctx;
            this.lista = lista;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_produto_lista, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            Produto p = lista.get(pos);
            h.tvNome.setText(p.getNome());
            h.tvPreco.setText(String.format("R$ %.2f", p.getPreco()));

            
            h.itemView.setOnClickListener(v -> {
                Intent i = new Intent(ctx, DetalheProdutoActivity.class);
                i.putExtra("produto_id", p.getId());
                i.putExtra("produto_nome", p.getNome());
                i.putExtra("produto_preco", p.getPreco());
                ctx.startActivity(i);
            });

            
            h.btnAdd.setOnClickListener(v -> {
                p.setQuantidade(1);
                CarrinhoManager.getInstance().adicionarProduto(p);
                Toast.makeText(ctx, p.getNome() + " adicionado!", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() { return lista.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvNome, tvPreco, btnAdd;
            VH(View v) {
                super(v);
                tvNome = v.findViewById(R.id.tvNomeProdutoLista);
                tvPreco = v.findViewById(R.id.tvPrecoProdutoLista);
                btnAdd = v.findViewById(R.id.btnAddProduto);
            }
        }
    }
}
