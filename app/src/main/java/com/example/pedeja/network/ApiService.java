package com.example.pedeja.network;

import com.example.pedeja.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @GET("categorias")
    Call<List<Categoria>> getCategorias();

    @GET("produto")
    Call<List<Produto>> getProdutos();

    @POST("cliente")
    Call<Cliente> cadastrarCliente(@Body Cliente cliente);

    @GET("cliente")
    Call<List<Cliente>> getClientes();

    @GET("pedido")
    Call<List<Pedido>> getPedidos();

    @POST("pedido")
    Call<Pedido> criarPedido(@Body Pedido pedido);

    @GET("item-pedido")
    Call<List<ItemPedido>> getItensPedido();

    @POST("item-pedido")
    Call<ItemPedido> adicionarItem(@Body ItemPedido item);
}
