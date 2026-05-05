package com.example.pedeja.database;

import android.content.ContentValues; // <-- Importação adicionada aqui
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pedeja.db";
    private static final int DATABASE_VERSION = 1;

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
                "preco REAL NOT NULL," + // Recebe o nosso valor em double
                "categoria_id INTEGER," +
                "FOREIGN KEY (categoria_id) REFERENCES categoria(id))");

        db.execSQL("CREATE TABLE cliente (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "email TEXT," +
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
        db.execSQL("DROP TABLE IF EXISTS item_pedido");
        db.execSQL("DROP TABLE IF EXISTS pedido");
        db.execSQL("DROP TABLE IF EXISTS produto");
        db.execSQL("DROP TABLE IF EXISTS cliente");
        db.execSQL("DROP TABLE IF EXISTS categoria");
        onCreate(db);
    }

    //  MÉTODO PARA INSERIR OS DADOS DA API ---
    public boolean inserirProduto(String nome, double preco) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Mapeando exatamente para as colunas da tabela 'produto'
        contentValues.put("nome", nome);
        contentValues.put("preco", preco);

        // Inserindo na tabela 'produto'
        long resultado = db.insert("produto", null, contentValues);

        if (resultado == -1) {
            return false; // Falhou ao salvar
        } else {
            return true;  // Salvo com sucesso no banco de dados local
        }
    }
}