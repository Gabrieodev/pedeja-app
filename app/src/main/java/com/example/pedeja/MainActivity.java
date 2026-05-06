package com.example.pedeja;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pedeja.database.DatabaseHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnTestar;
    TextView txtResultado;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        btnTestar = findViewById(R.id.btnTestar);
        txtResultado = findViewById(R.id.txtResultado);

        btnTestar.setOnClickListener(v -> testarConexao());
    }

    // 🔹 Apenas testa conexão (não usa dados da API)
    private void testarConexao() {

        String url = "https://fakerestaurantapi.runasp.net/api/Restaurant/items";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {

                    Log.d("FLUXO", "ONLINE");

                    txtResultado.setText("🟢 ONLINE\n\n");
                    mostrarProdutos();
                },
                error -> {

                    Log.d("FLUXO", "OFFLINE");

                    txtResultado.setText("🔴 OFFLINE\n\n");
                    mostrarProdutos();
                }
        );

        // sem cache
        request.setShouldCache(false);
        queue.getCache().clear();

        queue.add(request);
    }

    // 🔹 Mostra SOMENTE dados do banco
    private void mostrarProdutos() {

        List<String> produtos = db.listarProdutos();

        StringBuilder resultado = new StringBuilder();

        for (String produto : produtos) {
            resultado.append(produto).append("\n\n");
        }

        txtResultado.append(resultado.toString());
    }
}