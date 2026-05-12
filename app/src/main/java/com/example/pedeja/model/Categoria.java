package com.example.pedeja.model;

import com.google.gson.annotations.SerializedName;

public class Categoria {
    @SerializedName("id")
    private Long id;

    @SerializedName("nome")
    private String nome;

    public Categoria() {}
    public Categoria(Long id) { this.id = id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
