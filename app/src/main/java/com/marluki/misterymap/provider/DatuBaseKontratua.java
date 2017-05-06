package com.marluki.misterymap.provider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by charl on 07/04/2017.
 */

public class DatuBaseKontratua {

    /**
     * Taulak izango dituzten zutabeak
     */
    interface ColumnasObjetos {
        String ID = "id";
        String TIPO_ID = "tipo_id";
        String LATITUD = "latitud";
        String LONGITUD = "longitud";
        String USUARIO_ID = "usuario_id";
        String NOMBRE = "nombre";
        String DETALLES = "detalles";

        String ESTADO = "estado";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }

    interface ColumnasOvnis {
        String ID = "id";
        String OBJETO_ID = "objeto_id";
        String DIA = "dia";
        String HORA = "hora";
    }

    interface ColumnasFantasmas {
        String ID = "id";
        String OBJETO_ID = "objeto_id";
        String VISTO = "visto";
        String FAKE = "fake";
    }

    interface ColumnasHistorico {
        String ID = "id";
        String OBJETO_ID = "id";
        String FECHA = "fecha";
    }

    interface ColumnasSinResolver {
        String ID = "id";
        String OBJETO_ID = "objeto_id";
        String FECHA = "fecha";
    }

    interface ColumnasComentarios {
        String ID = "id";
        String OBJETO_ID = "objeto_id";
        String USUARIO_ID = "usuario_id";
        String TEXTO = "texto";
        String COMENTARIO_ID = "comentario_id";
        String DIA = "dia";
        String HORA = "hora";

        String ESTADO = "estado";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }

    interface ColumnasPsicofonia {
        String ID = "id";
        String NOMBRE = "nombre";
        String OBJETO_ID = "objetp_id";
        String URL = "url";
        String USUARIO_ID = "usuario_id";

        String ESTADO = "estado";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }

    interface ColumnasTipo {
        String ID = "id";
        String NOMBRE = "nombre";
    }

    interface ColumnasUsuario {
        String ID = "id";
        String NOMBRE = "nombre";
    }

    interface ColumnasFotos {
        String ID = "id";
        String NOMBRE = "nombre";
        String URL = "url";
        String OBJETO_ID = "objeto_id";
        String USUARIO_ID = "usuario_id";

        String ESTADO = "estado";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }
    /**
     * Content provider-aren autoritatea
     */
    public final static String AUTHORITY =
            "com.marluki.mysterymap";

    /**
     * Egoerak
     */
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;

    /**
     * Uriak sortzeko basea
     */
    public static final Uri URI_BASE = Uri.parse("content://" + AUTHORITY);

    /**
     * RUTAS "URIS"
     */
    private static final String RUTA_OBJEKTUAK_MAPA = "objektuak_mapa";
    private static final String RUTA_OVNIAK = "ovniak";
    private static final String RUTA_FANTASMAK = "fantasmak";
    private static final String RUTA_HISTORIKOAK = "historikoak";
    private static final String RUTA_SIN_RESOLVER = "sin_resolverlos";
    private static final String RUTA_COMENTARIOS = "comentarios";
    private static final String RUTA_PSICOFONIAS = "psicofonias";
    private static final String RUTA_TIPOS = "tipos";
    private static final String RUTA_USUARIOS = "usuarios";
    private static final String RUTA_FOTOS = "fotos";


    /**
     * Erabiliko ditugun MIME motak
     */
    public static final String BASE_CONTENIDOS = "misterioak.";
    /**
     * Content mota orokorra
     */
    public final static String TIPO_CONTENIDO =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + BASE_CONTENIDOS;

    /**
     * Content item mota orokorra
     */
    public static final String TIPO_CONTENIDO_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/vnd."
            + BASE_CONTENIDOS;

    /**
     * Metodo hau MIME orokor bat (taula osokoa) sortzen du.
     *
     * @param id eskuratu nahi den taularen identifikatzailea.
     * @return MIME 'string' formatuan.
     */
    public static String generarMime(String id) {
        if (id != null)
            return TIPO_CONTENIDO + id;
        else
            return null;
    }

    /**
     * Metodo hau MIME espezifikoa (taula bateko erregistro bat) sortzen du.
     *
     * @param id eskuratu nahi den taularen identifikatzailea.
     * @return MIME 'string' formatuan.
     */
    public static String generarMimeItem(String id) {
        if (id != null)
            return TIPO_CONTENIDO_ITEM + id;
        else
            return null;
    }
    /**
     * Objektu_Mapa taula
     */
    public static class Objetos_mapa implements ColumnasObjetos {
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_OBJEKTUAK_MAPA).build();

        /**
         *
         *
         * @param uri
         * @return
         */
        public static  String obtenerIdObjetoMapa(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        /**
         *
         *
         * @param id
         * @return
         */
        public static Uri crearUriObjetoMapa(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

    /**
     * Ovni taula
     */
    public static class Ovnis implements ColumnasOvnis {
        public static final Uri URI_CONTENIDO =
            URI_BASE.buildUpon().appendPath(RUTA_OVNIAK).build();

        /**
         *
         *
         */
        public static String obtenerIdOvni(Uri uri) {
            return uri.getLastPathSegment();
        }

        /**
         *
         *
         */
        public static Uri crearUriOvni(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

    public static class Fantasmas implements ColumnasFantasmas {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_FANTASMAK).build();

        /**
         *
         *
         */
        public static String obtenerIdFantasma(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriFantasma(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class Historicos implements ColumnasHistorico {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_HISTORIKOAK).build();

        /**
         *
         *
         */
        public static String obtenerIdHistoico(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriHistorico(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class SinResolver implements ColumnasSinResolver {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_SIN_RESOLVER).build();

        /**
         *
         *
         */
        public static String obtenerIdSinResolver(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriSinResolver(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class Comentarios implements ColumnasComentarios {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_COMENTARIOS).build();

        /**
         *
         *
         */
        public static String obtenerIdComentario(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriComentario(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class Psicofonias implements ColumnasPsicofonia {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_PSICOFONIAS).build();

        /**
         *
         *
         */
        public static String obtenerIdPsicofonia(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriPsicofonia(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class Tipos implements ColumnasTipo {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_TIPOS).build();

        /**
         *
         *
         */
        public static String obtenerIdTipo(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriTipo(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class Usuarios implements ColumnasUsuario {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_USUARIOS).build();

        /**
         *
         *
         */
        public static String obtenerIdUsuario(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriUsuario(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }

    public static class Fotos implements ColumnasFotos {
        public static final Uri URI_CONTENT =
                URI_BASE.buildUpon().appendPath(RUTA_FOTOS).build();

        /**
         *
         *
         */
        public static String obtenerIdFoto(Uri uri) {return uri.getLastPathSegment();}

        /**
         *
         *
         */
        public static Uri crearUriFoto(String id) {
            return URI_CONTENT.buildUpon().appendPath(id).build();
        }
    }
}
