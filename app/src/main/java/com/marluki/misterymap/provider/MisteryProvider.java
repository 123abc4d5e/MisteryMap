package com.marluki.misterymap.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

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

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
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
