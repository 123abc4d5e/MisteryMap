package com.marluki.misterymap.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by charl on 07/04/2017.
 */

public class DatuBaseKontratua {
    /**
     * Content provider-aren autoritatea
     */
    public final static String AUTHORITY =
            "com.marluki.mysterymap";
    /**
     * Mapako objektuen taula 'objektumapa'
     */
    public static final String OBJEKTU_MAPA = "objektumapa";

    /**
     * objektu mapako fila bat itzultzeko MIME mota
     */
    public final static String OBJMAPA_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + OBJEKTU_MAPA;

    /**
     * Mapako objektu guztiak itzultzeko MIME mota
     */
    public final static String OBJMAPA_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + OBJEKTU_MAPA;

    /**
     * Objektu_mapako edukiaren URI-a
     */
    public final static Uri OBJMAPA_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + OBJEKTU_MAPA);
    /**
     * Ovni taula 'ovni'
     */
    public static final String OVNI = "ovni";

    /**
     * ovni taulako fila bat itzultzeko MIME mota
     */
    public final static String OVNI_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + OVNI;

    /**
     * Ovni guztiak itzultzeko MIME mota
     */
    public final static String OVNI_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + OVNI;

    /**
     * Ovni edukiaren URI-a
     */
    public final static Uri OVNI_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + OVNI);
    /**
     * Fantasma taula 'fantasma'
     */
    public static  final String FANTASMA = "fantasma";

    /**
     * Fantasma taulako fila bat itzultzeko MIME mota
     */
    public final static String FANTASMA_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + FANTASMA;

    /**
     * Fantasma guztiak itzultzeko MIME mota
     */
    public final static String FANTASMA_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + FANTASMA;

    /**
     * Fantasma edukiaren URI-a
     */
    public final static Uri FANTASMA_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + FANTASMA);
    /**
     * Historiko taula 'historikoa'
     */
    public static final String HISTORIKOA = "historikoa";

    /**
     * Historiko taulako fila bat itzultzeko MIME mota
     */
    public final static String HISTORIKOA_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + HISTORIKOA;

    /**
     * Historiko guztiak itzultzeko MIME mota
     */
    public final static String HISTORIKOA_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + HISTORIKOA;

    /**
     * Historiko edukiaren URI-a
     */
    public final static Uri HISTORIKOA_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + HISTORIKOA);
    /**
     * Misterius_Totalus taula 'sin_resolver'
     */
    public static final String SIN_RESOLVER = "sin_resolver";

    /**
     * Historiko taulako fila bat itzultzeko MIME mota
     */
    public final static String SIN_RESOLVER_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + SIN_RESOLVER;

    /**
     * Historiko guztiak itzultzeko MIME mota
     */
    public final static String SIN_RESOLVER_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + SIN_RESOLVER;

    /**
     * Historiko edukiaren URI-a
     */
    public final static Uri SIN_RESOLVER_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + SIN_RESOLVER);
    /**
     * Iruzkinak taula 'iruzkinak'
     */
    public static final String IRUZKINAK = "iruzkinak";

    /**
     * Iruzkinak taulako fila bat itzultzeko MIME mota
     */
    public final static String IRUZKINAK_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + IRUZKINAK;

    /**
     * Iruzkinak guztiak itzultzeko MIME mota
     */
    public final static String IRUZKINAK_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + IRUZKINAK;

    /**
     * Iruzkinak edukiaren URI-a
     */
    public final static Uri IRUZKINAK_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + IRUZKINAK);
    /**
     * Info_pista taula 'info_pista'
     */
    public static final String INFO_PISTA = "info_pista";

    /**
     * Iruzkinak taulako fila bat itzultzeko MIME mota
     */
    public final static String INFO_PISTA_SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + INFO_PISTA;

    /**
     * Iruzkinak guztiak itzultzeko MIME mota
     */
    public final static String INFO_PISTA_MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + INFO_PISTA;

    /**
     * Iruzkinak edukiaren URI-a
     */
    public final static Uri INFO_PISTA_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + INFO_PISTA);
    /**
     * Psikofonia taula 'psikofonia'
     */
    public static final String PSIKOFONIA = "psikofonia";

    /**
     * URI edukien komparatzilea
     */
    public static final UriMatcher uriMatcher;
    /**
     * Erregistro askoentzako URI kodea
     */
    public static final int ALLROWS = 1;
    /**
     * Erregistro bakarrerako URI kodea
     */
    public static final int SINGLE_ROW = 2;


    // URI-en kokapena edo izendapena(asignación)
    static { // Agian txarto!!
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, OBJEKTU_MAPA, ALLROWS);
        uriMatcher.addURI(AUTHORITY, OBJEKTU_MAPA + "/#", SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, OVNI, ALLROWS);
        uriMatcher.addURI(AUTHORITY, OVNI + "/#", SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, FANTASMA, ALLROWS);
        uriMatcher.addURI(AUTHORITY, FANTASMA + "/#", SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, HISTORIKOA, ALLROWS);
        uriMatcher.addURI(AUTHORITY, HISTORIKOA + "/#", SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, SIN_RESOLVER, ALLROWS);
        uriMatcher.addURI(AUTHORITY, SIN_RESOLVER + "/#", SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, IRUZKINAK, ALLROWS);
        uriMatcher.addURI(AUTHORITY, IRUZKINAK + "/#", SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, INFO_PISTA, ALLROWS);
        uriMatcher.addURI(AUTHORITY, INFO_PISTA + "/#", SINGLE_ROW);
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;

    /**
     * Objektu_Mapa taularen zutabeak
     */
    public static class OBJ_COLUMNAS implements BaseColumns {
        public OBJ_COLUMNAS() {
            //Instantziarik gabe
        }

        public final static String TIPO_ID = "tipo_id";
        public final static String LATITUDEA = "latitud";
        public final static String LONGITUDEA = "longitud";
        public final static String ERABILTZAILE_ID = "usuario_id";

    }

    /**
     * Ovni taularen zutabeak
     */
    public static class OVNI_COLUMNAS implements BaseColumns {
        public OVNI_COLUMNAS() {
        }
    }
}
