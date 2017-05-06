package com.marluki.misterymap.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.marluki.misterymap.provider.DatuBaseKontratua;

public class MisteryProvider extends ContentProvider {

    public static final String TAG = "Provider";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private DatabaseHelper helper;

    private ContentResolver resolver;

    public MisteryProvider() {
    }

    //{URI_MATCHER}
    public static final UriMatcher uriMatcher;
    //Casos
    public static final int OBJETOS_MAPA = 100;
    public static final int OBJETOS_MAPA_ID = 101;
    public static final int OBJETOS_ID_OVNIS = 102;
    public static final int OBJETOS_ID_FANTASMAS = 103;
    public static final int OBJETOS_ID_HISTORICOS = 104;
    public static final int OBJETOS_ID_SIN_RESOLVER = 105;
    public static final int OBJETOS_ID_COMENTARIOS = 106;
    public static final int OBJETOS_ID_FOTOS = 107;
    public static final int OBJETOS_ID_PSICOFONIAS = 107;

    public static final int OVNIS = 200;
    public static final int OVNIS_ID = 201;

    public static final int FANTASMAS = 300;
    public static final int FANTASMAS_ID = 301;
    public static final int FANTASMAS_ID_PSICOFONIAS = 302;


    public static final int HISTORICOS = 400;
    public static final int HISTORICOS_ID = 401;

    public static final int SIN_RESOLVER = 300;
    public static final int SIN_RESOLVER_ID = 301;

    public static final int COMENTARIOS = 400;
    public static final int COMENTARIOS_ID = 401;
    public static final int COMENTARIOS_ID_COMENTARIOS = 402;

    public static final int PSICOFONIAS = 500;
    public static final int PSICOFONIAS_ID = 501;

    public static final int FOTOS = 600;
    public static final int FOTOS_ID = 601;

    public static final int USUARIOS = 700;
    public static final int USUARIOS_ID = 701;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa", OBJETOS_MAPA);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*", OBJETOS_MAPA_ID);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/ovnis", OBJETOS_ID_OVNIS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/fantasmas", OBJETOS_ID_FANTASMAS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/historicos", OBJETOS_ID_HISTORICOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/sin_resolver", OBJETOS_ID_SIN_RESOLVER);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/comentarios", OBJETOS_ID_COMENTARIOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/fotos", OBJETOS_ID_FOTOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "objetos_mapa/*/psicofonias", OBJETOS_ID_PSICOFONIAS);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "ovnis", OVNIS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "ovnis/*", OVNIS_ID);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "fantasmas", FANTASMAS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "fantasmas/*", FANTASMAS_ID);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "historicos", HISTORICOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "historicos/*", HISTORICOS_ID);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "sin_resolver", SIN_RESOLVER);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "sin_resolver/*", SIN_RESOLVER_ID);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "comentarios", COMENTARIOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "comentarios/*", COMENTARIOS_ID);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "comentarios/*/comentarios", COMENTARIOS_ID_COMENTARIOS);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "psicofonias", PSICOFONIAS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "psicofonias/*", PSICOFONIAS_ID);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "fotos", FOTOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "fotos/*", FOTOS_ID);

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "usuarios", USUARIOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "usuarios/*", USUARIOS_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
