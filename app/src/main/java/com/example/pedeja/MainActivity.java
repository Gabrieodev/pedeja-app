package com.example.pedeja;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pedeja.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnTestar;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper db = new DatabaseHelper(this);
        db.getWritableDatabase();

        btnTestar = findViewById(R.id.btnTestar);
        txtResultado = findViewById(R.id.txtResultado);

        btnTestar.setOnClickListener(v -> testarAPI());
        }
    private void testarAPI() {

        String url = "https://fakerestaurantapi.runasp.net/api/Restaurant/items";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    StringBuilder resultado = new StringBuilder();

                    for (int i = 0; i < 5; i++) {
                        try {
                            JSONObject item = response.getJSONObject(i);

                            String nome = item.getString("itemName");
                            double preco = item.getDouble("itemPrice");

                            resultado.append(nome)
                                    .append(" - R$ ")
                                    .append(preco)
                                    .append("\n\n");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    txtResultado.setText(resultado.toString());

                },
                error -> {
                    txtResultado.setText("Erro: " + error.toString());
                }
        );

        queue.add(request);
    }
}