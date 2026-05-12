package com.example.pedeja.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedeja.R;
import com.example.pedeja.model.Pedido;
import com.example.pedeja.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        SharedPreferences prefs = getSharedPreferences("pedeja_prefs", MODE_PRIVATE);
        long clienteId = prefs.getLong("cliente_id", -1);

        findViewById(R.id.btnVoltarHist).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvHistorico);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RetrofitClient.getApiService().getPedidos().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    
                    List<Pedido> meusPedidos = new ArrayList<>();
                    for (Pedido p : response.body()) {
                        if (p.getCliente() != null && clienteId == p.getCliente().getId()) {
                            meusPedidos.add(p);
                        }
                    }
                    rv.setAdapter(new HistAdapter(HistoricoActivity.this, meusPedidos));
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {}
        });
    }

    static class HistAdapter extends RecyclerView.Adapter<HistAdapter.VH> {
        final Context ctx;
        final List<Pedido> lista;

        HistAdapter(Context ctx, List<Pedido> lista) {
            this.ctx = ctx;
            this.lista = lista;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_pedido_historico, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            Pedido p = lista.get(pos);
            h.tvNumero.setText("Pedido #" + p.getId());
            h.tvData.setText((p.getData() != null ? p.getData() : "") + " • R$ " + String.format("%.2f", p.getTotal() != null ? p.getTotal() : 0));

            String status = p.getStatus() != null ? p.getStatus() : "PENDENTE";
            h.tvStatus.setText(traduzirStatus(status));
            h.tvStatus.setTextColor(getCorStatus(status));

            h.itemView.setOnClickListener(v -> {
                Intent i = new Intent(ctx, DetalhePedidoActivity.class);
                i.putExtra("pedido_id", p.getId());
                i.putExtra("pedido_total", p.getTotal() != null ? p.getTotal() : 0.0);
                i.putExtra("pedido_data", p.getData());
                i.putExtra("pedido_status", status);
                ctx.startActivity(i);
            });
        }

        private String traduzirStatus(String s) {
            switch (s) {
                case "ENTREGUE": return "Entregue";
                case "CANCELADO": return "Cancelado";
                case "EM_PREPARO": return "Em preparo";
                case "A_CAMINHO": return "A caminho";
                default: return s;
            }
        }

        private int getCorStatus(String s) {
            switch (s) {
                case "ENTREGUE": return 0xFF4CAF50;
                case "CANCELADO": return 0xFFF44336;
                case "EM_PREPARO": return 0xFFFF9800;
                default: return 0xFF888888;
            }
        }

        @Override
        public int getItemCount() { return lista.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvNumero, tvData, tvStatus;
            VH(View v) {
                super(v);
                tvNumero = v.findViewById(R.id.tvPedidoNumero);
                tvData = v.findViewById(R.id.tvPedidoData);
                tvStatus = v.findViewById(R.id.tvPedidoStatus);
            }
        }
    }
}
