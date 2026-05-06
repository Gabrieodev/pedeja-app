package com.example.pedeja.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pedeja.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Ativa foreign keys
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    // CRIAÇÃO DO BANCO
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE produto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "preco REAL NOT NULL)");

        inserirProdutosIniciais(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS produto");
        onCreate(db);
    }

    // DADOS INICIAIS
    private void inserirProdutosIniciais(SQLiteDatabase db) {

        inserirSeNaoExiste(db, "Hamburguer", 25.90);
        inserirSeNaoExiste(db, "Pizza", 40.00);
        inserirSeNaoExiste(db, "Refrigerante", 8.00);
    }

    private void inserirSeNaoExiste(SQLiteDatabase db, String nome, double preco) {

        Cursor cursor = db.rawQuery(
                "SELECT id FROM produto WHERE nome = ?",
                new String[]{nome}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();

        if (!existe) {
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("preco", preco);

            db.insert("produto", null, values);
        }
    }

    // LISTAR PRODUTOS
    public List<String> listarProdutos() {

        List<String> listaProdutos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nome, preco FROM produto", null);

        if (cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(0);
                double preco = cursor.getDouble(1);

                listaProdutos.add(nome + " - R$ " + String.format("%.2f", preco));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listaProdutos;
    }
}