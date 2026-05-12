package com.example.pedeja.model;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoManager {
    private static CarrinhoManager instance;
    private List<Produto> itens = new ArrayList<>();

    private CarrinhoManager() {}

    public static CarrinhoManager getInstance() {
        if (instance == null) instance = new CarrinhoManager();
        return instance;
    }

    public void adicionarProduto(Produto p) {
        for (Produto item : itens) {
            if (item.getId().equals(p.getId())) {
                item.setQuantidade(item.getQuantidade() + p.getQuantidade());
                return;
            }
        }
        itens.add(p);
    }

    public void removerProduto(Produto p) { itens.remove(p); }

    public List<Produto> getItens() { return itens; }

    public double getTotal() {
        double total = 0;
        for (Produto p : itens) total += p.getPreco() * p.getQuantidade();
        return total;
    }

    public void limpar() { itens.clear(); }

    public int getTotalItens() {
        int count = 0;
        for (Produto p : itens) count += p.getQuantidade();
        return count;
    }
}
