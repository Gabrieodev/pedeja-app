package com.example.pedeja.model;

import com.google.gson.annotations.SerializedName;

public class Pedido {
    @SerializedName("id")
    private Long id;

    @SerializedName("cliente")
    private Cliente cliente;

    @SerializedName("total")
    private Double total;

    @SerializedName("data")
    private String data;

    @SerializedName("status")
    private String status;

    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
