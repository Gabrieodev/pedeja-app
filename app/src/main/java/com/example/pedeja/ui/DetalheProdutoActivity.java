package com.example.pedeja.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pedeja.R;
import com.example.pedeja.model.CarrinhoManager;
import com.example.pedeja.model.Produto;

public class DetalheProdutoActivity extends AppCompatActivity {

    private int quantidade = 1;
    private TextView tvQuantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);

        long id = getIntent().getLongExtra("produto_id", 0);
        String nome = getIntent().getStringExtra("produto_nome");
        double preco = getIntent().getDoubleExtra("produto_preco", 0);

        TextView tvNome = findViewById(R.id.tvNomeDetalhe);
        TextView tvPreco = findViewById(R.id.tvPrecoDetalhe);
        tvQuantidade = findViewById(R.id.tvQuantidade);

        tvNome.setText(nome);
        tvPreco.setText(String.format("R$ %.2f", preco));

        
        TextView tvDesc = findViewById(R.id.tvDescDetalhe);
        tvDesc.setText(getDescricao(nome));

        
        TextView tvEmoji = findViewById(R.id.tvEmojiDetalhe);
        tvEmoji.setText(getEmoji(nome));

        
        findViewById(R.id.btnMenos).setOnClickListener(v -> {
            if (quantidade > 1) {
                quantidade--;
                tvQuantidade.setText(String.valueOf(quantidade));
            }
        });

        findViewById(R.id.btnMais).setOnClickListener(v -> {
            quantidade++;
            tvQuantidade.setText(String.valueOf(quantidade));
        });

        findViewById(R.id.btnVoltarDetalhe).setOnClickListener(v -> finish());

        
        Button btnAdd = findViewById(R.id.btnAdicionarCarrinho);
        btnAdd.setOnClickListener(v -> {
            Produto p = new Produto();
            p.setId(id);
            p.setNome(nome);
            p.setPreco(preco);
            p.setQuantidade(quantidade);

            CarrinhoManager.getInstance().adicionarProduto(p);
            Toast.makeText(this, nome + " adicionado ao carrinho!", Toast.LENGTH_SHORT).show();

            
            startActivity(new Intent(this, CarrinhoActivity.class));
        });
    }

    private String getDescricao(String nome) {
        if (nome == null) return "";
        String n = nome.toLowerCase();
        if (n.contains("x-burguer") || n.contains("xburguer")) return "Pão brioche, carne 150g, queijo cheddar, alface e tomate.";
        if (n.contains("x-bacon") || n.contains("xbacon")) return "Pão brioche, carne 150g, bacon crocante, queijo e maionese especial.";
        if (n.contains("x-salada") || n.contains("xsalada")) return "Pão integral, carne 120g, alface, tomate, pepino e molho light.";
        if (n.contains("batata") || n.contains("frita")) return "Batatas crocantes fritas na hora, temperadas com sal e ervas.";
        if (n.contains("pizza") && n.contains("margherita")) return "Molho de tomate, mussarela fresca e manjericão.";
        if (n.contains("pizza") && n.contains("calabresa")) return "Molho de tomate, mussarela e calabresa fatiada com cebola.";
        if (n.contains("pizza") && n.contains("frango")) return "Molho de tomate, mussarela, frango desfiado e catupiry.";
        if (n.contains("pizza")) return "Massa artesanal com recheio especial do chef.";
        if (n.contains("temaki")) return "Cone de alga com arroz japonês, salmão fresco e cream cheese.";
        if (n.contains("sushi") || n.contains("combo")) return "Combinado com peças variadas de sushi e sashimi frescos.";
        if (n.contains("refri") || n.contains("refrigerante")) return "Lata gelada 350ml. Sabores: Cola, Guaraná ou Laranja.";
        if (n.contains("suco")) return "Suco natural 300ml. Sabores: Laranja, Limão ou Maracujá.";
        if (n.contains("sorvete")) return "2 bolas de sorvete cremoso. Sabores: Chocolate, Baunilha ou Morango.";
        if (n.contains("brownie")) return "Brownie de chocolate belga com nozes e calda quente.";
        if (n.contains("burger") || n.contains("hambur") || n.contains("lanche")) return "Pão artesanal, carne grelhada e acompanhamentos especiais.";
        if (n.contains("frango")) return "Frango grelhado ou empanado, temperado com ervas e especiarias.";
        if (n.contains("salada")) return "Mix de folhas frescas com legumes da estação e molho especial.";
        return "Preparado com ingredientes frescos e selecionados.";
    }

    private String getEmoji(String nome) {
        if (nome == null) return "🍽️";
        nome = nome.toLowerCase();
        if (nome.contains("burger") || nome.contains("lanche") || nome.contains("hambur")) return "🍔";
        if (nome.contains("sushi") || nome.contains("temaki")) return "🍣";
        if (nome.contains("pizza")) return "🍕";
        if (nome.contains("frango") || nome.contains("chicken")) return "🍗";
        if (nome.contains("batata") || nome.contains("frita")) return "🍟";
        if (nome.contains("salada")) return "🥗";
        if (nome.contains("refri") || nome.contains("suco") || nome.contains("bebida")) return "🥤";
        if (nome.contains("sorvete") || nome.contains("sobremesa")) return "🍦";
        if (nome.contains("combo") || nome.contains("kit")) return "🍱";
        return "🍽️";
    }
}
