package com.marluki.misterymap.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.marluki.misterymap.provider.DatuBaseKontratua.*;

import java.util.ArrayList;

public class MisteryProvider extends ContentProvider {

    public static final String TAG = "Provider";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private static final String DATABASE_NAME = "mistery.db";
    private static final int DATABASE_VERSION = 1;

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


    public static final int HISTORICOS = 500;
    public static final int HISTORICOS_ID = 501;

    public static final int SIN_RESOLVER = 600;
    public static final int SIN_RESOLVER_ID = 601;

    public static final int COMENTARIOS = 700;
    public static final int COMENTARIOS_ID = 701;
    public static final int COMENTARIOS_ID_COMENTARIOS = 702;

    public static final int PSICOFONIAS = 800;
    public static final int PSICOFONIAS_ID = 801;

    public static final int FOTOS = 900;
    public static final int FOTOS_ID = 901;

    public static final int USUARIOS = 1000;
    public static final int USUARIOS_ID =1001;

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
// [/URI_MATCHER]

    // CAMPOS AUXILIARES
    private final static String OBJETO_MAPA_JOIN_OVNI_JOIN_USUARIO_Y_TIPO = "objeto_mapa " +
            "INNER JOIN ovni " +
            "ON objeto_mapa.id = ovni.objeto_id " +
            "INNER JOIN usuario " +
            "ON objeto_mapa.usuario_id = usuario.id " +
            "INNER JOIN tipo " +
            "ON objeto_mapa.tipo_id = tipo.id";

    private final static String OBJETO_MAPA_JOIN_FANTASMA_JOIN_USUARIO_Y_TIPO = "objeto_mapa " +
            "INNER JOIN fantasma " +
            "ON objeto_mapa.id = fantasma.objeto_id " +
            "INNER JOIN usuario " +
            "ON objeto_mapa.usuario_id = usuario.id " +
            "INNER JOIN tipo " +
            "ON objeto_mapa.tipo_id = tipo.id";

    private final static String OBJETO_MAPA_JOIN_HISTORICO_JOIN_USUARIO_Y_TIPO = "objeto_mapa " +
            "INNER JOIN historico " +
            "ON objeto_mapa.id = historico.objeto_id " +
            "INNER JOIN usuario " +
            "ON objeto_mapa.usuario_id = usuario.id " +
            "INNER JOIN tipo " +
            "ON objeto_mapa.tipo_id = tipo.id";

    private final static String OBJETO_MAPA_JOIN_SIN_RESOLVER_JOIN_USUARIO_Y_TIPO = "objeto_mapa " +
            "INNER JOIN sin_resolver " +
            "ON objeto_mapa.id = sin_resolver.objeto_id " +
            "INNER JOIN usuario " +
            "ON objeto_mapa.usuario_id = usuario.id " +
            "INNER JOIN tipo " +
            "ON objeto_mapa.tipo_id = tipo.id";

    private static final String PSICOFONIA_JOIN_USUARIO =
            "psicofonia " +
                    "INNER JOIN usuario " +
                    "ON psicofonia.usuario_id = usuario.id";
    private static final String FOTO_JOIN_USUARIO =
            "foto " +
                    "INNER JOIN usuario " +
                    "ON foto.usuario_id = usuario.id";
    private static final String COMENTARIO_JOIN_PRODUCTO =
            "comentario " +
                    "INNER JOIN usuario " +
                    "ON comentario.usuario_id = usuario.id";

    private final String[] proyOvni = new String[] {
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.OVNI + ".*",
            Tipos.NOMBRE
    };
    private final String[] proyFantasma = new String[] {
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.FANTASMA + ".*",
            Tipos.NOMBRE
    };
    private final String[] proyHistorico = new String[] {
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.HISTORICO + ".*",
            Tipos.NOMBRE
    };
    private final String[] proySinResolver = new String[] {
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.SIN_RESOLVER + ".*",
            Tipos.NOMBRE
    };
    private final String[] proyComentario = new String[] {
            DatabaseHelper.Tablas.COMENTARIO + ".*",
            Usuarios.NOMBRE
    };
    private final String[] proyPsicofonia = new String[] {
            DatabaseHelper.Tablas.PSICOFONIA + ".*",
            Usuarios.NOMBRE
    };
    private final String[] proyFoto = new String[] {
            DatabaseHelper.Tablas.FOTO + ".*",
            Usuarios.NOMBRE
    };
    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0;i<numOperations;i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        resolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OBJETOS_MAPA:
                return DatuBaseKontratua.generarMime("objetos_mapa");
            case OBJETOS_MAPA_ID:
                return DatuBaseKontratua.generarMimeItem("objetos_mapa");
            case OVNIS:
                return DatuBaseKontratua.generarMime("ovnis");
            case OVNIS_ID:
                return DatuBaseKontratua.generarMimeItem("ovnis");
            case FANTASMAS:
                return DatuBaseKontratua.generarMime("fantasmas");
            case FANTASMAS_ID:
                return DatuBaseKontratua.generarMimeItem("fantasmas");
            case HISTORICOS:
                return DatuBaseKontratua.generarMime("historicos");
            case HISTORICOS_ID:
                return DatuBaseKontratua.generarMimeItem("historicos");
            case SIN_RESOLVER:
                return DatuBaseKontratua.generarMime("sin_resolver");
            case SIN_RESOLVER_ID:
                return DatuBaseKontratua.generarMimeItem("sin_resolver");
            case COMENTARIOS:
                return DatuBaseKontratua.generarMime("comentarios");
            case COMENTARIOS_ID:
                return DatuBaseKontratua.generarMimeItem("comentarios");
            case FOTOS:
                return DatuBaseKontratua.generarMime("fotos");
            case FOTOS_ID:
                return DatuBaseKontratua.generarMimeItem("fotos");
            case PSICOFONIAS:
                return DatuBaseKontratua.generarMime("psicofonias");
            case PSICOFONIAS_ID:
                return DatuBaseKontratua.generarMimeItem("psicofonias");
            case USUARIOS:
                return DatuBaseKontratua.generarMime("usuarios");
            case USUARIOS_ID:
                return DatuBaseKontratua.generarMimeItem("usuarios");
            default:
                throw new UnsupportedOperationException("Uri desconocida  =>" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete: " + uri);

        SQLiteDatabase db = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (uriMatcher.match(uri)) {
            case OBJETOS_MAPA_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.delete(DatabaseHelper.Tablas.OBJETO_MAPA,Objetos_mapa.ID + "=?", new String[]{id});
                break;
            case OVNIS_ID:
                id = Ovnis.obtenerIdOvni(uri);
                afectados = db.delete(DatabaseHelper.Tablas.OVNI,Ovnis.OBJETO_ID + "=?", new String[]{id});
                break;
            case FANTASMAS_ID:
                id = Fantasmas.obtenerIdFantasma(uri);
                afectados = db.delete(DatabaseHelper.Tablas.FANTASMA,Fantasmas.OBJETO_ID + "=?", new String[]{id});
                break;
            case HISTORICOS_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.delete(DatabaseHelper.Tablas.HISTORICO,Historicos.OBJETO_ID + "=?", new String[]{id});
                break;
            case SIN_RESOLVER_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.delete(DatabaseHelper.Tablas.SIN_RESOLVER,SinResolver.OBJETO_ID + "=?", new String[]{id});
                break;
            case COMENTARIOS_ID:
                id = Comentarios.obtenerIdComentario(uri);
                afectados = db.delete(DatabaseHelper.Tablas.COMENTARIO,Comentarios.ID + "=?", new String[]{id});
                break;
            case PSICOFONIAS_ID:
                id = Psicofonias.obtenerIdPsicofonia(uri);
                afectados = db.delete(DatabaseHelper.Tablas.PSICOFONIA,Psicofonias.ID + "=?", new String[]{id});
                break;
            case FOTOS_ID:
                id = Fotos.obtenerIdFoto(uri);
                afectados = db.delete(DatabaseHelper.Tablas.FOTO,Fotos.ID + "=?", new String[]{id});
                break;
            case USUARIOS_ID:
                id = Usuarios.obtenerIdUsuario(uri);
                afectados = db.delete(DatabaseHelper.Tablas.USUARIO,Usuarios.ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA + " =>" + uri);
        }
        return afectados;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
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

    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }
}
