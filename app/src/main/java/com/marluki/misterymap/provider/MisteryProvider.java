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
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.marluki.misterymap.provider.DatuBaseKontratua.*;
import com.marluki.misterymap.ui.FragmentMapa;

import java.util.ArrayList;

public class MisteryProvider extends ContentProvider {

    public static final String TAG = "Provider";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private static final String DATABASE_NAME = "mistery.db";
    private static final int DATABASE_VERSION = 5;

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
    public static final int OBJETOS_ID_PSICOFONIAS = 108;

    public static final int OVNIS = 200;
    public static final int OVNIS_ID = 201;

    public static final int FANTASMAS = 300;
    public static final int FANTASMAS_ID = 301;


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
    public static final int USUARIOS_ID = 1001;

    public static final int TIPOS = 1100;
    public static final int TIPOS_ID = 1101;

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

        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "tipos", TIPOS);
        uriMatcher.addURI(DatuBaseKontratua.AUTHORITY, "tipos/*", TIPOS_ID);
    }
// [/URI_MATCHER]

    // CAMPOS AUXILIARES

    private final static String OBJETO_MAPA_JOIN_USUARIO_Y_TIPO = "objeto_mapa " +
            "INNER JOIN usuario " +
            "ON objeto_mapa.usuario_id = usuario.id " +
            "INNER JOIN tipo " +
            "ON objeto_mapa.tipo_id = tipo.id";

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

    private final static String OBJETO_MAPA_JOIN_COMENTARIO_JOIN_USUARIO = "comentario " +
            "INNER JOIN objeto_mapa " +
            "ON comentario.objeto_id = objeto_mapa.id " +
            "INNER JOIN usuario " +
            "ON comentario.usuario_id = usuario.id";

    private final static String OBJETO_MAPA_JOIN_PSICOFONIA = "objeto_mapa " +
            "INNER JOIN psicofonia " +
            "ON objeto_mapa.id = psicofonia.objeto_id";

    private final static String OBJETO_MAPA_JOIN_FOTO = "objeto_mapa " +
            "INNER JOIN foto " +
            "ON objeto_mapa.id = foto.objeto_id";

    private static final String PSICOFONIA_JOIN_USUARIO =
            "psicofonia " +
                    "INNER JOIN usuario " +
                    "ON psicofonia.usuario_id = usuario.id";
    private static final String FOTO_JOIN_USUARIO =
            "foto " +
                    "INNER JOIN usuario " +
                    "ON foto.usuario_id = usuario.id";
    private static final String COMENTARIO_JOIN_USUARIO =
            "comentario " +
                    "INNER JOIN usuario " +
                    "ON comentario.usuario_id = usuario.id";

    private final String[] proyObjetos = new String[]{
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            Usuarios.NOMBRE, Usuarios.FOTO,
            DatabaseHelper.Tablas.TIPO + "." + Tipos.NOMBRE_TIPO
    };
    private final String[] proyOvni = new String[]{
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.OVNI + "." + Ovnis.FECHA,
            Tipos.NOMBRE_TIPO
    };
    private final String[] proyFantasma = new String[]{
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.FANTASMA + "." + Fantasmas.FAKE,
            DatabaseHelper.Tablas.FANTASMA + "." + Fantasmas.VISTO,
            Tipos.NOMBRE_TIPO
    };
    private final String[] proyHistorico = new String[]{
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.HISTORICO + "." + Historicos.FECHA,
            Tipos.NOMBRE_TIPO
    };
    private final String[] proySinResolver = new String[]{
            DatabaseHelper.Tablas.OBJETO_MAPA + ".*",
            DatabaseHelper.Tablas.SIN_RESOLVER + "." + SinResolver.FECHA,
            Tipos.NOMBRE_TIPO
    };
    private final String[] proyObjComentario = new String[]{
            DatabaseHelper.Tablas.OBJETO_MAPA + "." + Objetos_mapa.NOMBRE_OBJETO,
            DatabaseHelper.Tablas.COMENTARIO + ".*",
            Usuarios.NOMBRE, Usuarios.FOTO
    };
    private final String[] proyComentario = new String[]{
            DatabaseHelper.Tablas.COMENTARIO + ".*",
            Usuarios.NOMBRE, Usuarios.FOTO
    };
    private final String[] proyPsicofonia = new String[]{
            DatabaseHelper.Tablas.PSICOFONIA + ".*",
            Usuarios.NOMBRE, Usuarios.FOTO
    };
    private final String[] proyFoto = new String[]{
            DatabaseHelper.Tablas.FOTO + ".*",
            Usuarios.NOMBRE, Usuarios.FOTO
    };

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
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
                afectados = db.delete(DatabaseHelper.Tablas.OBJETO_MAPA, Objetos_mapa.ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case OVNIS_ID:
                id = Ovnis.obtenerIdOvni(uri);
                afectados = db.delete(DatabaseHelper.Tablas.OVNI, Ovnis.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case FANTASMAS_ID:
                id = Fantasmas.obtenerIdFantasma(uri);
                afectados = db.delete(DatabaseHelper.Tablas.FANTASMA, Fantasmas.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case HISTORICOS_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.delete(DatabaseHelper.Tablas.HISTORICO, Historicos.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case SIN_RESOLVER_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.delete(DatabaseHelper.Tablas.SIN_RESOLVER, SinResolver.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case COMENTARIOS_ID:
                id = Comentarios.obtenerIdComentario(uri);
                afectados = db.delete(DatabaseHelper.Tablas.COMENTARIO, Comentarios.ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case PSICOFONIAS_ID:
                id = Psicofonias.obtenerIdPsicofonia(uri);
                afectados = db.delete(DatabaseHelper.Tablas.PSICOFONIA, Psicofonias.ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case FOTOS_ID:
                id = Fotos.obtenerIdFoto(uri);
                afectados = db.delete(DatabaseHelper.Tablas.FOTO, Fotos.ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case USUARIOS_ID:
                id = Usuarios.obtenerIdUsuario(uri);
                afectados = db.delete(DatabaseHelper.Tablas.USUARIO, Usuarios.ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA + " =>" + uri);
        }
        return afectados;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d(TAG, "Insercion en " + uri + "(" + values.toString() + ")");
        SQLiteDatabase db = helper.getWritableDatabase();

        String id = null;
        String id1 = null;

        int match = uriMatcher.match(uri);

        switch (match) {
            case OBJETOS_MAPA:
                if(null == values.getAsString(Objetos_mapa.ID)) {
                    id = Objetos_mapa.generarIdObjetoMapa();
                    values.put(Objetos_mapa.ID, id );
                }
                db.insertOrThrow(DatabaseHelper.Tablas.OBJETO_MAPA, null, values);
                notificarCambio(uri);
                return Objetos_mapa.crearUriObjetoMapa(values.getAsString(Objetos_mapa.ID));
            case OBJETOS_ID_OVNIS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                values.put(Ovnis.OBJETO_ID, id);

                db.insertOrThrow(DatabaseHelper.Tablas.OVNI, null, values);
                notificarCambio(uri);
                return Ovnis.crearUriOvni(id);
            case OBJETOS_ID_FANTASMAS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                values.put(Fantasmas.OBJETO_ID, id);

                db.insertOrThrow(DatabaseHelper.Tablas.FANTASMA, null, values);
                notificarCambio(uri);
                return Fantasmas.crearUriFantasma(id);
            case OBJETOS_ID_HISTORICOS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                values.put(Historicos.OBJETO_ID, id);

                db.insertOrThrow(DatabaseHelper.Tablas.HISTORICO, null, values);
                notificarCambio(uri);
                return Historicos.crearUriHistorico(id);
            case OBJETOS_ID_SIN_RESOLVER:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                values.put(SinResolver.OBJETO_ID, id);

                db.insertOrThrow(DatabaseHelper.Tablas.SIN_RESOLVER, null, values);
                notificarCambio(uri);
                return SinResolver.crearUriSinResolver( id);
            case OBJETOS_ID_COMENTARIOS:
                id1 = Objetos_mapa.obtenerIdObjetoMapa(uri);
                // obtener id del comentario a insertar
                if(null == values.getAsString(Comentarios.ID)) {
                    id = Comentarios.generarIdComentario();
                    values.put(Comentarios.ID, id );
                }
                values.put(Comentarios.OBJETO_ID, id1);

                db.insertOrThrow(DatabaseHelper.Tablas.COMENTARIO, null, values);
                notificarCambio(uri);
                return Comentarios.crearUriComentario(values.getAsString(Comentarios.ID));
            case OBJETOS_ID_FOTOS:
                id1 = Objetos_mapa.obtenerIdObjetoMapa(uri);
                // obtener id para foto
                if(null == values.getAsString(Fotos.ID)) {
                    id = Fotos.generarIdFoto();
                    values.put(Fotos.ID, id );
                }

                values.put(Fotos.OBJETO_ID, id1);

                db.insertOrThrow(DatabaseHelper.Tablas.FOTO, null, values);
                notificarCambio(uri);
                return Fotos.crearUriFoto(values.getAsString(Fotos.ID));
            case OBJETOS_ID_PSICOFONIAS:
                id1 = Objetos_mapa.obtenerIdObjetoMapa(uri);
                // obtener id para foto
                if(null == values.getAsString(Objetos_mapa.ID)) {
                    id = Psicofonias.generarIdPsicofonia();
                    values.put(Psicofonias.ID, id );
                }

                values.put(Psicofonias.OBJETO_ID, id1);

                db.insertOrThrow(DatabaseHelper.Tablas.PSICOFONIA, null, values);
                notificarCambio(uri);
                return Psicofonias.crearUriPsicofonia(values.getAsString(Psicofonias.ID));
            case COMENTARIOS_ID_COMENTARIOS:// INSERTA UN COMENTARIO QUE HACE REFERENCIA A OTRO COMENTARIO
                id1 = Comentarios.obtenerIdComentario(uri);
                if(null == values.getAsString(Objetos_mapa.ID)) {
                    id = Comentarios.generarIdComentario();
                    values.put(Comentarios.ID, id );
                }
                values.put(Comentarios.COMENTARIO_ID, id1);

                db.insertOrThrow(DatabaseHelper.Tablas.COMENTARIO, null, values);
                notificarCambio(uri);
                return Comentarios.crearUriComentario(values.getAsString(Comentarios.ID));
            case USUARIOS:
                if(null == values.getAsString(Usuarios.ID)) {
                    id = Usuarios.generarIdUsuario();
                    values.put(Usuarios.ID, id );
                }
                db.insertOrThrow(DatabaseHelper.Tablas.USUARIO, null, values);
                notificarCambio(uri);
                return Usuarios.crearUriUsuario(values.getAsString(Usuarios.ID));
            case TIPOS:
                db.insertOrThrow(DatabaseHelper.Tablas.TIPO, null, values);
                id = values.getAsString(Tipos.ID);
                notificarCambio(uri);
                return Tipos.crearUriTipo(id);
            case OVNIS:
                db.insertOrThrow(DatabaseHelper.Tablas.OVNI, null, values);
                id = values.getAsString(Ovnis.OBJETO_ID);
                notificarCambio(uri);
                return Ovnis.crearUriOvni(id);
            case FANTASMAS:
                db.insertOrThrow(DatabaseHelper.Tablas.FANTASMA, null, values);
                id = values.getAsString(Fantasmas.OBJETO_ID);
                notificarCambio(uri);
                return Fantasmas.crearUriFantasma(id);
            case HISTORICOS:
                db.insertOrThrow(DatabaseHelper.Tablas.HISTORICO, null, values);
                id = values.getAsString(Historicos.OBJETO_ID);
                notificarCambio(uri);
                return Historicos.crearUriHistorico(id);
            case SIN_RESOLVER:
                db.insertOrThrow(DatabaseHelper.Tablas.SIN_RESOLVER, null, values);
                id = values.getAsString(SinResolver.OBJETO_ID);
                notificarCambio(uri);
                return SinResolver.crearUriSinResolver(id);
            case COMENTARIOS:
                db.insertOrThrow(DatabaseHelper.Tablas.COMENTARIO, null, values);
                id = values.getAsString(Comentarios.ID);
                notificarCambio(uri);
                return Comentarios.crearUriComentario(id);
            case FOTOS:
                db.insertOrThrow(DatabaseHelper.Tablas.FOTO, null, values);
                id = values.getAsString(Fotos.ID);
                notificarCambio(uri);
                return Fotos.crearUriFoto(id);
            case PSICOFONIAS:
                db.insertOrThrow(DatabaseHelper.Tablas.PSICOFONIA, null, values);
                id = values.getAsString(Psicofonias.ID);
                notificarCambio(uri);
                return Psicofonias.crearUriPsicofonia(id);
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA + " =>" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: " + uri);
        //Obtener base de datos
        SQLiteDatabase db = helper.getReadableDatabase();

        //String auxiliares para id y cursor
        String id;
        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)) {
            case OBJETOS_MAPA:
                // Obtener filtro
                String filtro = Objetos_mapa.tieneFiltro(uri)
                        ? uri.getQueryParameter("filtro") : sortOrder;
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.OBJETO_MAPA, projection,
                        selection, selectionArgs, null, null, filtro);
                break;
            case OBJETOS_MAPA_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_USUARIO_Y_TIPO);
                //Consultando todos los objetos
                c = builder.query(db, proyObjetos,
                        DatabaseHelper.Tablas.OBJETO_MAPA + "." + Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_OVNIS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_OVNI_JOIN_USUARIO_Y_TIPO);
                //Consultando todos los objetos
                c = builder.query(db, proyOvni, DatabaseHelper.Tablas.OBJETO_MAPA + "." +
                        Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_FANTASMAS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_FANTASMA_JOIN_USUARIO_Y_TIPO);
                //Consultando todos los objetos
                c = builder.query(db, proyFantasma, DatabaseHelper.Tablas.OBJETO_MAPA + "." +
                        Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_HISTORICOS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_HISTORICO_JOIN_USUARIO_Y_TIPO);
                //Consultando todos los objetos
                c = builder.query(db, proyHistorico, DatabaseHelper.Tablas.OBJETO_MAPA + "." +
                        Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_SIN_RESOLVER:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_SIN_RESOLVER_JOIN_USUARIO_Y_TIPO);
                //Consultando todos los objetos
                c = builder.query(db, proySinResolver, DatabaseHelper.Tablas.OBJETO_MAPA + "." +
                        Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_COMENTARIOS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_COMENTARIO_JOIN_USUARIO);
                //Consultando todos los objetos
                c = builder.query(db, proyObjComentario,
                        Comentarios.OBJETO_ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_FOTOS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_FOTO);
                //Consultando todos los objetos
                c = builder.query(db, proyFoto, DatabaseHelper.Tablas.OBJETO_MAPA + "." +
                        Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case OBJETOS_ID_PSICOFONIAS:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                builder.setTables(OBJETO_MAPA_JOIN_PSICOFONIA);
                //Consultando todos los objetos
                c = builder.query(db, proyPsicofonia, DatabaseHelper.Tablas.OBJETO_MAPA + "." +
                        Objetos_mapa.ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case COMENTARIOS:
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.COMENTARIO, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case COMENTARIOS_ID://ESTO DEVUELVE UN COMENTARIO SEGUN SU ID
                id = Comentarios.obtenerIdComentario(uri);
                builder.setTables(COMENTARIO_JOIN_USUARIO);
                c = builder.query(db, proyComentario, DatabaseHelper.Tablas.COMENTARIO + "." +
                        Comentarios.ID + "=?", new String[]{id}, null, null, null);
                break;
            case COMENTARIOS_ID_COMENTARIOS:// ESTO DEVOLVE TODOS LOS COMENTARIOS CON REFERENCIA A OTRO COMENTARIO ORDENADO POR FECHA
                id = Comentarios.obtenerIdComentario(uri);
                String[] claves1 = new String[]{id};
                String seleccion1 = String.format("%s=?", Comentarios.COMENTARIO_ID);
                builder.setTables(COMENTARIO_JOIN_USUARIO);
                c = builder.query(db, proyComentario, seleccion1, claves1, null, null,
                        DatabaseHelper.Tablas.COMENTARIO + "." + Comentarios.FECHA);
                break;
            case OVNIS:
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.OVNI, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case FANTASMAS:
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.FANTASMA, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case HISTORICOS:
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.HISTORICO, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case SIN_RESOLVER:
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.SIN_RESOLVER, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case OVNIS_ID:
                id = Ovnis.obtenerIdOvni(uri);
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.OVNI, projection,
                        Ovnis.OBJETO_ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs, null, null, sortOrder);
                break;
            case FANTASMAS_ID:
                id = Fantasmas.obtenerIdFantasma(uri);
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.FANTASMA
                        , projection,
                        Fantasmas.OBJETO_ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs, null, null, sortOrder);
                break;
            case HISTORICOS_ID:
                id = Historicos.obtenerIdHistorico(uri);
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.HISTORICO, projection,
                        Historicos.OBJETO_ID + "=?", new String[]{id}, null, null, sortOrder);
                break;
            case SIN_RESOLVER_ID:
                id = SinResolver.obtenerIdSinResolver(uri);
                //Consultando todos los objetos
                c = db.query(DatabaseHelper.Tablas.SIN_RESOLVER, projection,
                        SinResolver.OBJETO_ID + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs, null, null, sortOrder);
                break;
            case FOTOS:
                c = db.query(DatabaseHelper.Tablas.FOTO, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case FOTOS_ID:
                id = Fotos.obtenerIdFoto(uri);
                builder.setTables(FOTO_JOIN_USUARIO);
                c = builder.query(db, proyFoto, DatabaseHelper.Tablas.FOTO + "." +
                        Fotos.ID + "=?", new String[]{id}, null,
                        null, null);
            case PSICOFONIAS:
                c = db.query(DatabaseHelper.Tablas.PSICOFONIA, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case PSICOFONIAS_ID:
                id = Psicofonias.obtenerIdPsicofonia(uri);
                builder.setTables(PSICOFONIA_JOIN_USUARIO);
                c = builder.query(db, proyPsicofonia, DatabaseHelper.Tablas.PSICOFONIA + "." +
                                Psicofonias.ID + "=?", new String[]{id}, null,
                        null, null);
                break;
            case USUARIOS:
                c = db.query(DatabaseHelper.Tablas.USUARIO, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USUARIOS_ID:
                id = Usuarios.obtenerIdUsuario(uri);
                c = db.query(DatabaseHelper.Tablas.USUARIO, projection, selection, selectionArgs, null,
                        null, null);
                break;
            case TIPOS:
                c = db.query(DatabaseHelper.Tablas.TIPO, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA + " =>" + uri);
        }
        c.setNotificationUri(resolver, uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update: " + uri);

        SQLiteDatabase db = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (uriMatcher.match(uri)) {
            case OBJETOS_MAPA:
                afectados = db.update(DatabaseHelper.Tablas.OBJETO_MAPA, values, selection, selectionArgs);
                notificarCambio(uri);
                break;
            case USUARIOS:
                afectados = db.update(DatabaseHelper.Tablas.USUARIO, values, selection, selectionArgs);
                notificarCambio(uri);
                break;
            case COMENTARIOS:
                afectados = db.update(DatabaseHelper.Tablas.COMENTARIO, values, selection, selectionArgs);
                notificarCambio(uri);
                break;
            case PSICOFONIAS:
                afectados = db.update(DatabaseHelper.Tablas.PSICOFONIA, values, selection, selectionArgs);
                notificarCambio(uri);
                break;
            case FOTOS:
                afectados = db.update(DatabaseHelper.Tablas.FOTO, values, selection, selectionArgs);
                notificarCambio(uri);
                break;
            case OBJETOS_MAPA_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.update(DatabaseHelper.Tablas.OBJETO_MAPA, values,
                        Objetos_mapa.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs);
                notificarCambio(uri);
                break;
            case OVNIS_ID:
                id = Ovnis.obtenerIdOvni(uri);
                afectados = db.update(DatabaseHelper.Tablas.OVNI, values,
                        Ovnis.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case FANTASMAS_ID:
                id = Fantasmas.obtenerIdFantasma(uri);
                afectados = db.update(DatabaseHelper.Tablas.FANTASMA, values,
                        Fantasmas.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case HISTORICOS_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.update(DatabaseHelper.Tablas.HISTORICO, values,
                        Historicos.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case SIN_RESOLVER_ID:
                id = Objetos_mapa.obtenerIdObjetoMapa(uri);
                afectados = db.update(DatabaseHelper.Tablas.SIN_RESOLVER, values,
                        SinResolver.OBJETO_ID + "=?", new String[]{id});
                notificarCambio(uri);
                break;
            case COMENTARIOS_ID:
                id = Comentarios.obtenerIdComentario(uri);
                afectados = db.update(DatabaseHelper.Tablas.COMENTARIO, values,
                        Comentarios.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs);
                notificarCambio(uri);
                break;
            case PSICOFONIAS_ID:
                id = Psicofonias.obtenerIdPsicofonia(uri);
                afectados = db.update(DatabaseHelper.Tablas.PSICOFONIA, values
                        , Psicofonias.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs);
                notificarCambio(uri);
                break;
            case FOTOS_ID:
                id = Fotos.obtenerIdFoto(uri);
                afectados = db.update(DatabaseHelper.Tablas.FOTO, values
                        , Fotos.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs);
                notificarCambio(uri);
                break;
            case USUARIOS_ID:
                id = Usuarios.obtenerIdUsuario(uri);
                afectados = db.update(DatabaseHelper.Tablas.USUARIO, values,
                        Usuarios.ID + "=" + "\"" + id + "\""
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs);
                notificarCambio(uri);
                break;
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA + " =>" + uri);
        }
        return afectados;
    }

    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }
}
