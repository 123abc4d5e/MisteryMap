package com.marluki.misterymap.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.marluki.misterymap.provider.DatuBaseKontratua.*;

/**
 * Created by charl on 04/05/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    interface Tablas {
        String OBJETO_MAPA = "objeto_mapa";
        String OVNI = "ovni";
        String FANTASMA = "fantasma";
        String HISTORICO = "historico";
        String SIN_RESOLVER = "sin_resolver";
        String COMENTARIO = "comentario";
        String PSICOFONIA = "psicofonia";
        String TIPO = "tipo";
        String USUARIO = "usuario";
        String FOTO = "foto";
    }

    interface referencias {
        String OBJETO_ID = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.OBJETO_MAPA, Objetos_mapa.ID);

        String TIPO_ID = String.format("REFERENCES %s(%s)",
                Tablas.TIPO, Tipos.ID);

        String USUARIO_ID = String.format("REFERENCES %s(%s)",
                Tablas.USUARIO, Usuarios.ID);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER UNIQUE NOT NULL,%s TEXT NOT NULL)",
                Tablas.TIPO, BaseColumns._ID, Tipos.ID, Tipos.NOMBRE_TIPO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL, %s TEXT DEFAULT NULL, %s INTEGER NOT NULL DEFAULT %s," +
                        " %s INTEGER NOT NULL DEFAULT 0)",
                Tablas.USUARIO, BaseColumns._ID, Usuarios.ID, Usuarios.NOMBRE, Usuarios.FOTO,
                Usuarios.ESTADO, ESTADO_OK, Usuarios.PENDIENTE_ACTUALIZACION));


        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s INT NOT NULL %s,%s REAL NOT NULL," +
                "%s REAL NOT NULL,%s TEXT NOT NULL %s, %s TEXT NOT NULL, %s TEXT, %s INTEGER NOT NULL DEFAULT %s," +
                        "%s INTEGER NOT NULL DEFAULT 1, %s INTEGER NOT NULL DEFAULT 0, %s INTEGER NOT NULL DEFAULT 0)",
                Tablas.OBJETO_MAPA, BaseColumns._ID, Objetos_mapa.ID,
                Objetos_mapa.TIPO_ID, referencias.TIPO_ID,
                Objetos_mapa.LATITUD, Objetos_mapa.LONGITUD,
                Objetos_mapa.USUARIO_ID, referencias.USUARIO_ID,
                Objetos_mapa.NOMBRE_OBJETO, Objetos_mapa.DETALLES,
                Objetos_mapa.ESTADO, ESTADO_OK, Objetos_mapa.PENDIENTE_INSERCION,
                Objetos_mapa.PENDIENTE_ACTUALIZACION, Objetos_mapa.PENDIENTE_ELIMINACION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL %s,%s TEXT NOT NULL,%s TEXT NOT NULL)",
                Tablas.OVNI, BaseColumns._ID, Ovnis.OBJETO_ID, referencias.OBJETO_ID, Ovnis.DIA, Ovnis.HORA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL %s,%s INTEGER NOT NULL,%s INTEGER NOT NULL)",
                Tablas.FANTASMA, BaseColumns._ID, Fantasmas.OBJETO_ID, referencias.OBJETO_ID, Fantasmas.VISTO, Fantasmas.FAKE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL %s,%s TEXT NOT NULL)",
                Tablas.HISTORICO, BaseColumns._ID, Historicos.OBJETO_ID, referencias.OBJETO_ID, Historicos.FECHA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL %s,%s TEXT NOT NULL)",
                Tablas.SIN_RESOLVER, BaseColumns._ID, SinResolver.OBJETO_ID, referencias.OBJETO_ID, SinResolver.FECHA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL %s,%s TEXT NOT NULL %s," +
                        "%s TEXT NOT NULL,%s TEXT DEFAULT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL DEFAULT %s," +
                        "%s INTEGER NOT NULL DEFAULT 1, %s INTEGER NOT NULL DEFAULT 0, %s INTEGER NOT NULL DEFAULT 0)",
                Tablas.COMENTARIO, BaseColumns._ID, Comentarios.ID,
                Comentarios.OBJETO_ID, referencias.OBJETO_ID,
                Comentarios.USUARIO_ID, referencias.USUARIO_ID,
                Comentarios.TEXTO, Comentarios.COMENTARIO_ID, Comentarios.DIA, Comentarios.HORA,
                Comentarios.ESTADO, ESTADO_OK, Comentarios.PENDIENTE_INSERCION,
                Comentarios.PENDIENTE_ACTUALIZACION, Comentarios.PENDIENTE_ELIMINACION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL %s,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL %s, %s TEXT NOT NULL, %s INTEGER NOT NULL DEFAULT %s," +
                        "%s INTEGER NOT NULL DEFAULT 1, %s INTEGER NOT NULL DEFAULT 0, %s INTEGER NOT NULL DEFAULT 0)",
                Tablas.PSICOFONIA, BaseColumns._ID, Psicofonias.ID,
                Psicofonias.OBJETO_ID, referencias.OBJETO_ID,
                Psicofonias.URL, Psicofonias.USUARIO_ID, referencias.USUARIO_ID,
                Psicofonias.NOMBRE_PSICOFONIA,
                Psicofonias.ESTADO, ESTADO_OK, Psicofonias.PENDIENTE_INSERCION,
                Psicofonias.PENDIENTE_ACTUALIZACION, Psicofonias.PENDIENTE_ELIMINACION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL %s, %s TEXT NOT NULL %s, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL DEFAULT %s," +
                        "%s INTEGER NOT NULL DEFAULT 1, %s INTEGER NOT NULL DEFAULT 0, %s INTEGER NOT NULL DEFAULT 0)",
                Tablas.FOTO, BaseColumns._ID, Fotos.ID, Fotos.OBJETO_ID, referencias.OBJETO_ID,
                Fotos.USUARIO_ID, referencias.USUARIO_ID,
                Fotos.NOMBRE_FOTO, Fotos.URL, Fotos.ESTADO, ESTADO_OK, Fotos.PENDIENTE_INSERCION,
                Fotos.PENDIENTE_ACTUALIZACION, Fotos.PENDIENTE_ELIMINACION));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.OVNI);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.FANTASMA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HISTORICO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.SIN_RESOLVER);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.COMENTARIO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PSICOFONIA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.FOTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.OBJETO_MAPA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TIPO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIO);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
