package com.example.pedeja;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnTestar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTestar = findViewById(R.id.btnTestar);

        btnTestar.setOnClickListener(v -> {

            // futura chamada da API

        });
    }
}