package com.example.pedeja.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pedeja.R;

public class ConfirmacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        long pedidoId = getIntent().getLongExtra("pedido_id", 0);

        TextView tvNum = findViewById(R.id.tvNumeroPedido);
        tvNum.setText("Seu pedido nº " + pedidoId + " está a caminho 🛵");

        
        findViewById(R.id.btnAcompanhar).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoricoActivity.class));
            finish();
        });

        
        findViewById(R.id.btnVoltarHome).setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}
