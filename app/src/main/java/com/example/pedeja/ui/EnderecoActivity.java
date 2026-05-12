package com.example.pedeja.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pedeja.R;

public class EnderecoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

        SharedPreferences prefs = getSharedPreferences("pedeja_prefs", MODE_PRIVATE);

        EditText etNome = findViewById(R.id.etNomeEnd);
        EditText etRua = findViewById(R.id.etEnderecoRua);
        EditText etBairro = findViewById(R.id.etBairro);
        EditText etCidade = findViewById(R.id.etCidade);
        EditText etTel = findViewById(R.id.etTelefone);

        
        etNome.setText(prefs.getString("cliente_nome", ""));
        etRua.setText(prefs.getString("cliente_endereco", ""));

        findViewById(R.id.btnVoltarEnd).setOnClickListener(v -> finish());

        Button btnSalvar = findViewById(R.id.btnSalvarEnd);
        btnSalvar.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String rua = etRua.getText().toString().trim();

            if (nome.isEmpty() || rua.isEmpty()) {
                Toast.makeText(this, "Preencha nome e endereço", Toast.LENGTH_SHORT).show();
                return;
            }

            
            String enderecoCompleto = rua + ", " + etBairro.getText() + " - " + etCidade.getText();
            prefs.edit()
                    .putString("entrega_nome", nome)
                    .putString("entrega_endereco", enderecoCompleto)
                    .putString("entrega_telefone", etTel.getText().toString())
                    .apply();

            startActivity(new Intent(this, ResumoPedidoActivity.class));
        });
    }
}
