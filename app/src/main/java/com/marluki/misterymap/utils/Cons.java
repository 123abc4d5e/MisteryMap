package com.marluki.misterymap.utils;

import android.net.Uri;

import com.marluki.misterymap.provider.DatuBaseKontratua;

/**
 * Created by charl on 13/05/2017.
 */

public class Cons {

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta característica.
     */
    private static final String PUERTO_HOST = "";

    private static final String VERSION = "/MisteryMap_web/v1/web/";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://62.151.226.216" + PUERTO_HOST + VERSION;

    /**
     * URLs del Web Service
     */
    public static final String GET_OBJ_URL = IP  + "Objetos/getById.php";
    public static final String GET_ALL_OBJ_URL = IP + "Objetos/getAll.php";
    public static final String INSERT_OBJ_URL = IP + "Objetos/insert.php";
    public static final String DELETE_OBJ_URL = IP + "Objetos/delete.php";
    public static final String UPDATE_OBJ_URL = IP + "Objetos/update.php";

    public static final String GET_ALL_OVNI_URL = IP + "Ovnis/getAll.php";
    public static final String GET_OVNI_URL = IP + "Ovnis/getById.php";
    public static final String UPDATE_OVNI_URL = IP + "Ovnis/update.php";
    public static final String INSERT_OVNI_URL = IP + "Ovnis/insert.php";

    public static final String GET_ALL_FANT_URL = IP + "Fantasmas/getAll.php";
    public static final String GET_FANT_URL = IP + "Fantasmas/getById.php";
    public static final String UPDATE_FANT_URL = IP + "Fantasmas/update.php";
    public static final String INSERT_FANT_URL = IP + "Fantasmas/insert.php";

    public static final String GET_ALL_HIST_URL = IP + "Historicos/getAll.php";
    public static final String GET_HIST_URL = IP + "Historicos/getById.php";
    public static final String UPDATE_HIST_URL = IP + "Historicos/update.php";
    public static final String INSERT_HIST_URL = IP + "Historicos/insert.php";

    public static final String GET_ALL_SIN_URL = IP + "SinResolver/getAll.php";
    public static final String GET_SIN_URL = IP + "SinResolver/getById.php";
    public static final String UPDATE_SIN_URL = IP + "SinResolver/update.php";
    public static final String INSERT_SIN_URL = IP + "SinResolver/insert.php";

    public static final String GET_ALL_COMENT_URL = IP + "Comentarios/getAll.php";
    public static final String GET_COMENT_URL = IP + "Comentarios/getById.php";
    public static final String INSERT_COMENT_URL = IP + "Comentarios/insert.php";
    public static final String UPDATE_COMENT_URL = IP + "Comentarios/update.php";
    public static final String DELETE_COMENT_URL = IP + "Comentarios/delete.php";

    public static final String GET_ALL_FOTO_URL = IP + "Fotos/getAll.php";
    public static final String GET_FOTO_URL = IP + "Fotos/getById.php";
    public static final String INSERT_FOTO_URL = IP + "Fotos/insert.php";
    public static final String UPDATE_FOTO_URL = IP + "Fotos/update.php";
    public static final String DELETE_FOTO_URL = IP + "Fotos/delete.php";

    public static final String GET_ALL_PSICO_URL = IP + "Psicofonias/getAll.php";
    public static final String GET_PSICO_URL = IP + "Psicofonias/getById.php";
    public static final String INSERT_PSICO_URL = IP + "Psicofonias/insert.php";
    public static final String UPDATE_PSICO_URL = IP + "Psicofonias/update.php";
    public static final String DELETE_PSICO_URL = IP + "Psicofonias/delete.php";

    public static final String GET_ALL_USER_URL = IP + "Usuarios/getAll.php";
    public static final String GET_USER_URL = IP + "Usuarios/getById.php";
    public static final String INSERT_USER_URL = IP + "Usuarios/insert.php";
    public static final String UPDATE_USER_URL = IP + "Usuarios/update.php";
    public static final String DELETE_USER_URL = IP + "Usuarios/delete.php";


    public static final String GET_ALL_TYPE_URL = IP + "Tipos/getAll.php";


    public static final String[] GET_URLS = new String[]{
            GET_ALL_TYPE_URL,
            GET_ALL_USER_URL,
            GET_ALL_OBJ_URL,
            GET_ALL_OVNI_URL,
            GET_ALL_FANT_URL,
            GET_ALL_HIST_URL,
            GET_ALL_SIN_URL,
            GET_ALL_PSICO_URL,
            GET_ALL_FOTO_URL,
            GET_ALL_COMENT_URL
    };

    public static final String[] INSERT_URLS = new String[]{
            INSERT_OBJ_URL,
            INSERT_COMENT_URL,
            INSERT_PSICO_URL,
            INSERT_FOTO_URL
    };

    public static final String[] UPDATE_URLS = new String[]{
            UPDATE_USER_URL,
            UPDATE_OBJ_URL,
            UPDATE_COMENT_URL,
            UPDATE_PSICO_URL,
            UPDATE_FOTO_URL
    };

    public static final String[] DELETE_URLS = new String[]{
            DELETE_OBJ_URL,
            DELETE_COMENT_URL,
            DELETE_PSICO_URL,
            DELETE_FOTO_URL
    };

    public static final Uri[] INSERT_URIS = new Uri[]{
            DatuBaseKontratua.Objetos_mapa.URI_CONTENT,
            DatuBaseKontratua.Comentarios.URI_CONTENT,
            DatuBaseKontratua.Psicofonias.URI_CONTENT,
            DatuBaseKontratua.Fotos.URI_CONTENT
    };

    public static final Uri[] UPDATE_URIS = new Uri[]{
            DatuBaseKontratua.Usuarios.URI_CONTENT,
            DatuBaseKontratua.Objetos_mapa.URI_CONTENT,
            DatuBaseKontratua.Comentarios.URI_CONTENT,
            DatuBaseKontratua.Psicofonias.URI_CONTENT,
            DatuBaseKontratua.Fotos.URI_CONTENT
    };

    public static final Uri[] DELETE_URIS = new Uri[]{
            DatuBaseKontratua.Objetos_mapa.URI_CONTENT,
            DatuBaseKontratua.Comentarios.URI_CONTENT,
            DatuBaseKontratua.Psicofonias.URI_CONTENT,
            DatuBaseKontratua.Fotos.URI_CONTENT
    };

    /**
     * Campos de las respuestas Json
     */
    public static final String ESTADO = "estado";
    public static final String TABLA = "tabla";
    public static final String DATOS = "datos";
    public static final String MENSAJE = "mensaje";
    public static final String ID = "id";


    public static final String TABLA_OBJETO_MAPA = "objeto_mapa";
    public static final String TABLA_OVNI = "ovni";
    public static final String TABLA_FANTASMA = "fantasma";
    public static final String TABLA_HISTORICO = "historico";
    public static final String TABLA_SIN_RESOLVER = "sin_resolver";
    public static final String TABLA_COMENTARIO = "comentario";
    public static final String TABLA_PSICOFONIA = "psicofonia";
    public static final String TABLA_TIPO = "tipo";
    public static final String TABLA_USUARIO = "usuario";
    public static final String TABLA_FOTO = "foto";

    /**
     * Códigos del campo
     */
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    public static final int ID_INSERT = 1;
    public static final int ID_UPDATE = 2;
    public static final int ID_DELETE = 3;

    public static final String createUser = "createUser";
    public static final String deleteUser = "deleteUser";

    public static final String idCliente = "ClienteID";
    public static final String stiloMapa = "MapStyleLight";
    public static final String misPreferencias = "MisPreferencias";


}
