package com.example.pedeja.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.example.pedeja.R;
import com.example.pedeja.model.Cliente;
import com.example.pedeja.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etSenha;
    private Button btnEntrar, btnCadastrar;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("pedeja_prefs", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        
        if (prefs.getLong("cliente_id", -1) != -1) {
            irParaHome();
            return;
        }

        btnEntrar.setOnClickListener(v -> fazerLogin());
        btnCadastrar.setOnClickListener(v -> mostrarDialogCadastro());
    }

    private void fazerLogin() {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Informe seu e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        
        RetrofitClient.getApiService().getClientes().enqueue(new Callback<java.util.List<Cliente>>() {
            @Override
            public void onResponse(Call<java.util.List<Cliente>> call, Response<java.util.List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Cliente c : response.body()) {
                        if (c.getEmail().equalsIgnoreCase(email)) {
                            salvarCliente(c);
                            irParaHome();
                            return;
                        }
                    }
                    Toast.makeText(LoginActivity.this, "E-mail não encontrado. Cadastre-se!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<java.util.List<Cliente>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogCadastro() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cadastro, null);
        EditText etNome = view.findViewById(R.id.etNomeCadastro);
        EditText etEmailCad = view.findViewById(R.id.etEmailCadastro);
        EditText etEndereco = view.findViewById(R.id.etEnderecoCadastro);

        new AlertDialog.Builder(this)
                .setTitle("Criar conta")
                .setView(view)
                .setPositiveButton("Cadastrar", (dialog, which) -> {
                    String nome = etNome.getText().toString().trim();
                    String email = etEmailCad.getText().toString().trim();
                    String endereco = etEndereco.getText().toString().trim();

                    if (nome.isEmpty() || email.isEmpty()) {
                        Toast.makeText(this, "Preencha nome e e-mail", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cliente novo = new Cliente();
                    novo.setNome(nome);
                    novo.setEmail(email);
                    novo.setEndereco(endereco);

                    RetrofitClient.getApiService().cadastrarCliente(novo).enqueue(new Callback<Cliente>() {
                        @Override
                        public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                salvarCliente(response.body());
                                Toast.makeText(LoginActivity.this, "Conta criada! Bem-vindo, " + response.body().getNome(), Toast.LENGTH_SHORT).show();
                                irParaHome();
                            }
                        }

                        @Override
                        public void onFailure(Call<Cliente> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void salvarCliente(Cliente c) {
        prefs.edit()
                .putLong("cliente_id", c.getId())
                .putString("cliente_nome", c.getNome())
                .putString("cliente_email", c.getEmail())
                .putString("cliente_endereco", c.getEndereco() != null ? c.getEndereco() : "")
                .apply();
    }

    private void irParaHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
