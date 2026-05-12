package com.example.pedeja.model;

import com.google.gson.annotations.SerializedName;

public class ItemPedido {
    @SerializedName("id")
    private Long id;

    @SerializedName("pedido")
    private Pedido pedido;

    @SerializedName("produto")
    private Produto produto;

    @SerializedName("quantidade")
    private Integer quantidade;

    @SerializedName("subtotal")
    private Double subtotal;

    public ItemPedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}
