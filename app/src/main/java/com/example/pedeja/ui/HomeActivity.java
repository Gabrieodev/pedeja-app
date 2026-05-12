package com.example.pedeja.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private List<Produto> todosProdutos = new ArrayList<>();
    private List<Produto> produtosFiltrados = new ArrayList<>();
    private GridAdapter gridAdapter;
    private String categoriaAtiva = "Todos";
    private String buscaAtiva = "";
    private LinearLayout llChips;

    
    private static final String[][] CATEGORIAS = {
        {"🍽️", "Todos", ""},
        {"🍔", "Lanches", "burger,hambur,x-b,bacon,salada,lanche"},
        {"🍕", "Pizzas", "pizza"},
        {"🍣", "Sushi", "sushi,temaki,combo"},
        {"🍟", "Acompanhamentos", "batata,frita,nugget,anel"},
        {"🥤", "Bebidas", "refri,suco,agua,cafe,bebida"},
        {"🍦", "Sobremesas", "sorvete,brownie,chocolate,doce,sobremesa"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("pedeja_prefs", MODE_PRIVATE);
        String nome = prefs.getString("cliente_nome", "");

        drawerLayout = findViewById(R.id.drawerLayout);
        llChips = findViewById(R.id.llChips);

        ((TextView) findViewById(R.id.tvSaudacao)).setText("Olá, " + nome + "!");
        ((TextView) findViewById(R.id.tvNomeMenu)).setText(nome);

        
        findViewById(R.id.btnMenu).setOnClickListener(v ->
                drawerLayout.openDrawer(findViewById(R.id.navDrawer)));
        findViewById(R.id.menuPedidos).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            startActivity(new Intent(this, HistoricoActivity.class));
        });
        findViewById(R.id.menuCarrinho).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            startActivity(new Intent(this, CarrinhoActivity.class));
        });
        findViewById(R.id.menuSair).setOnClickListener(v -> {
            prefs.edit().clear().apply();
            drawerLayout.closeDrawers();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        
        RecyclerView rvProdutos = findViewById(R.id.rvProdutos);
        rvProdutos.setLayoutManager(new GridLayoutManager(this, 2));
        gridAdapter = new GridAdapter(this, produtosFiltrados);
        rvProdutos.setAdapter(gridAdapter);

        
        criarChips();

        
        ((EditText) findViewById(R.id.etBusca)).addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscaAtiva = s.toString();
                aplicarFiltro();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        
        RetrofitClient.getApiService().getProdutos().enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    todosProdutos = response.body();
                    aplicarFiltro();
                }
            }
            @Override public void onFailure(Call<List<Produto>> call, Throwable t) {}
        });
    }

    private void criarChips() {
        for (String[] cat : CATEGORIAS) {
            String emoji = cat[0];
            String nome = cat[1];

            TextView chip = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 10, 0);
            chip.setLayoutParams(lp);
            chip.setText(emoji + " " + nome);
            chip.setTextSize(15f);
            chip.setTypeface(null, Typeface.BOLD);
            chip.setPadding(36, 20, 36, 20);
            chip.setBackground(getDrawable(nome.equals("Todos") ? R.drawable.chip_ativo : R.drawable.chip_inativo));
            chip.setTextColor(0xFF1A1A1A);

            chip.setOnClickListener(v -> {
                categoriaAtiva = nome;
                atualizarChips();
                aplicarFiltro();
            });

            llChips.addView(chip);
        }
    }

    private void atualizarChips() {
        for (int i = 0; i < llChips.getChildCount(); i++) {
            TextView chip = (TextView) llChips.getChildAt(i);
            String chipNome = CATEGORIAS[i][1];
            chip.setBackground(getDrawable(
                    chipNome.equals(categoriaAtiva) ? R.drawable.chip_ativo : R.drawable.chip_inativo));
        }
    }

    private void aplicarFiltro() {
        produtosFiltrados.clear();
        for (Produto p : todosProdutos) {
            if (p.getNome() == null) continue;
            String nomeLower = p.getNome().toLowerCase();

            
            if (!buscaAtiva.isEmpty() && !nomeLower.contains(buscaAtiva.toLowerCase())) continue;

            
            if (!categoriaAtiva.equals("Todos")) {
                String keywords = "";
                for (String[] cat : CATEGORIAS) {
                    if (cat[1].equals(categoriaAtiva)) { keywords = cat[2]; break; }
                }
                boolean match = false;
                for (String kw : keywords.split(",")) {
                    if (nomeLower.contains(kw.trim())) { match = true; break; }
                }
                if (!match) continue;
            }

            produtosFiltrados.add(p);
        }
        gridAdapter.notifyDataSetChanged();
    }

    
    static class GridAdapter extends RecyclerView.Adapter<GridAdapter.VH> {
        final Context ctx;
        final List<Produto> lista;

        GridAdapter(Context ctx, List<Produto> lista) {
            this.ctx = ctx;
            this.lista = lista;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_produto_grid, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            Produto p = lista.get(pos);
            h.tvNome.setText(p.getNome());
            h.tvPreco.setText(String.format("R$ %.2f", p.getPreco()));
            h.tvEmoji.setText(getEmoji(p.getNome()));

            h.itemView.setOnClickListener(v -> abrirDetalhe(p));
            h.btnVer.setOnClickListener(v -> abrirDetalhe(p));
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
                tvNome = v.findViewById(R.id.tvNomeGrid);
                tvPreco = v.findViewById(R.id.tvPrecoGrid);
                tvEmoji = v.findViewById(R.id.tvEmojiGrid);
                btnVer = v.findViewById(R.id.btnVerGrid);
            }
        }
    }
}
