package com.example.pedeja.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class PedidosEntregaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_entrega);

        SharedPreferences prefs = getSharedPreferences("pedeja_prefs", MODE_PRIVATE);
        long clienteId = prefs.getLong("cliente_id", -1);

        findViewById(R.id.btnVoltarEntrega).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvEntrega);
        rv.setLayoutManager(new LinearLayoutManager(this));
        LinearLayout llVazio = findViewById(R.id.llVazio);

        RetrofitClient.getApiService().getPedidos().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> emEntrega = new ArrayList<>();
                    for (Pedido p : response.body()) {
                        if (p.getCliente() != null && clienteId == p.getCliente().getId()) {
                            String status = p.getStatus();
                            if ("EM_PREPARO".equals(status) || "A_CAMINHO".equals(status)) {
                                emEntrega.add(p);
                            }
                        }
                    }

                    if (emEntrega.isEmpty()) {
                        rv.setVisibility(View.GONE);
                        llVazio.setVisibility(View.VISIBLE);
                    } else {
                        rv.setAdapter(new EntregaAdapter(PedidosEntregaActivity.this, emEntrega));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {}
        });
    }

    static class EntregaAdapter extends RecyclerView.Adapter<EntregaAdapter.VH> {
        final Context ctx;
        final List<Pedido> lista;

        EntregaAdapter(Context ctx, List<Pedido> lista) {
            this.ctx = ctx;
            this.lista = lista;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_pedido_entrega, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            Pedido p = lista.get(pos);
            h.tvNumero.setText("Pedido #" + p.getId());
            h.tvTotal.setText(String.format("R$ %.2f", p.getTotal() != null ? p.getTotal() : 0));
            boolean aCaminho = "A_CAMINHO".equals(p.getStatus());
            h.tvStatus.setText(aCaminho ? "🛵 A caminho!" : "👨‍🍳 Em preparo");
            h.tvStatus.setTextColor(aCaminho ? 0xFF4CAF50 : 0xFFFF9800);
        }

        @Override
        public int getItemCount() { return lista.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvNumero, tvTotal, tvStatus;
            VH(View v) {
                super(v);
                tvNumero = v.findViewById(R.id.tvNumeroEntrega);
                tvTotal = v.findViewById(R.id.tvTotalEntrega);
                tvStatus = v.findViewById(R.id.tvStatusEntrega);
            }
        }
    }
}
