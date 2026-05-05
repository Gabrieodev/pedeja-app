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
    // 1. Subimos a versão para o Android perceber a atualização
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE categoria (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL)");

        db.execSQL("CREATE TABLE produto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "preco REAL NOT NULL," +
                "categoria_id INTEGER," +
                "FOREIGN KEY (categoria_id) REFERENCES categoria(id))");

        // 2. Coluna 'senha' adicionada para instalações novas do app
        db.execSQL("CREATE TABLE cliente (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "email TEXT," +
                "senha TEXT," +
                "endereco TEXT)");

        db.execSQL("CREATE TABLE pedido (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cliente_id INTEGER," +
                "total REAL," +
                "data TEXT," +
                "status TEXT CHECK(status IN ('CONFIRMADO','CANCELADO','PREPARANDO','SAIU_PARA_ENTREGA','ENTREGUE'))," +
                "FOREIGN KEY (cliente_id) REFERENCES cliente(id))");

        db.execSQL("CREATE TABLE item_pedido (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pedido_id INTEGER," +
                "produto_id INTEGER," +
                "quantidade INTEGER," +
                "subtotal REAL," +
                "FOREIGN KEY (pedido_id) REFERENCES pedido(id)," +
                "FOREIGN KEY (produto_id) REFERENCES produto(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 3. A ATUALIZAÇÃO SEGURA:
        // Se o celular do usuário tem a versão 1 do banco...
        if (oldVersion < 2) {
            //  adicionamo a coluna de senha. Nenhum dado é apagado!
            db.execSQL("ALTER TABLE cliente ADD COLUMN senha TEXT");
        }
    }

    // --- MÉTODOS DO BANCO ---

    public boolean inserirProduto(String nome, double preco) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", nome);
        contentValues.put("preco", preco);

        long resultado = db.insert("produto", null, contentValues);

        if (resultado == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<String> listarProdutos() {
        List<String> listaProdutos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nome, preco FROM produto", null);

        if (cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(0);
                double preco = cursor.getDouble(1);

                listaProdutos.add(nome + " - R$ " + preco);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaProdutos;
    }
}