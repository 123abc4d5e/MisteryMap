package com.marluki.misterymap.utils;

import android.database.Cursor;
import android.util.Log;

import com.marluki.misterymap.provider.DatuBaseKontratua;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by charl on 13/05/2017.
 */

public class Utils {

    public static JSONObject deCursorObjAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID));
        String tipo_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID));
        Float latitud = c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD));
        Float longitud = c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD));
        String usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.USUARIO_ID));
        String nombre_objeto = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO));
        String detalles = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.DETALLES));

        try {
            object.put(DatuBaseKontratua.Objetos_mapa.ID, id);
            object.put(DatuBaseKontratua.Objetos_mapa.TIPO_ID, tipo_id);
            object.put(DatuBaseKontratua.Objetos_mapa.LATITUD, latitud);
            object.put(DatuBaseKontratua.Objetos_mapa.LONGITUD, longitud);
            object.put(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, usuario_id);
            object.put(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO, nombre_objeto);
            object.put(DatuBaseKontratua.Objetos_mapa.DETALLES, detalles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorOvniAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Ovnis.OBJETO_ID));
        String dia = c.getString(c.getColumnIndex(DatuBaseKontratua.Ovnis.DIA));
        String hora = c.getString(c.getColumnIndex(DatuBaseKontratua.Ovnis.HORA));

        try {
            object.put(DatuBaseKontratua.Ovnis.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.Ovnis.DIA, dia);
            object.put(DatuBaseKontratua.Ovnis.HORA, hora);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorFantAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fantasmas.OBJETO_ID));
        int visto = c.getInt(c.getColumnIndex(DatuBaseKontratua.Fantasmas.VISTO));
        int fake = c.getInt(c.getColumnIndex(DatuBaseKontratua.Fantasmas.FAKE));

        try {
            object.put(DatuBaseKontratua.Fantasmas.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.Fantasmas.VISTO, visto);
            object.put(DatuBaseKontratua.Fantasmas.FAKE, fake);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorHistAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Historicos.OBJETO_ID));
        String fecha = c.getString(c.getColumnIndex(DatuBaseKontratua.Historicos.FECHA));

        try {
            object.put(DatuBaseKontratua.Historicos.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.Historicos.FECHA, fecha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorSinAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.SinResolver.OBJETO_ID));
        String fecha = c.getString(c.getColumnIndex(DatuBaseKontratua.SinResolver.FECHA));

        try {
            object.put(DatuBaseKontratua.SinResolver.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.SinResolver.FECHA, fecha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorComentAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.ID));
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.OBJETO_ID));
        String comentario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.COMENTARIO_ID));
        String texto = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.TEXTO));
        String usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.USUARIO_ID));
        String dia = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.DIA));
        String hora = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.HORA));

        try {
            object.put(DatuBaseKontratua.Comentarios.ID, id);
            object.put(DatuBaseKontratua.Comentarios.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.Comentarios.COMENTARIO_ID, comentario_id);
            object.put(DatuBaseKontratua.Comentarios.TEXTO, texto);
            object.put(DatuBaseKontratua.Comentarios.USUARIO_ID, usuario_id);
            object.put(DatuBaseKontratua.Comentarios.DIA, dia);
            object.put(DatuBaseKontratua.Comentarios.HORA, hora);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorPsicoAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String id = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.ID));
        String nombre_psicofonia = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA));
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.OBJETO_ID));
        String url = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.URL));
        String usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.USUARIO_ID));

        try {
            object.put(DatuBaseKontratua.Psicofonias.ID, id);
            object.put(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA, nombre_psicofonia);
            object.put(DatuBaseKontratua.Psicofonias.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.Psicofonias.URL, url);
            object.put(DatuBaseKontratua.Psicofonias.USUARIO_ID, usuario_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorFotoAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.ID));
        String nombre_foto = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.NOMBRE_FOTO));
        String objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.OBJETO_ID));
        String url = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.URL));
        String usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.USUARIO_ID));

        try {
            object.put(DatuBaseKontratua.Fotos.ID, id);
            object.put(DatuBaseKontratua.Fotos.NOMBRE_FOTO, nombre_foto);
            object.put(DatuBaseKontratua.Fotos.OBJETO_ID, objeto_id);
            object.put(DatuBaseKontratua.Fotos.URL, url);
            object.put(DatuBaseKontratua.Fotos.USUARIO_ID, usuario_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }

    public static JSONObject deCursorUserAJSONObject(Cursor c) {
        JSONObject object = new JSONObject();
        String id = c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.ID));
        String nombre = c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.NOMBRE));
        Float foto = c.getFloat(c.getColumnIndex(DatuBaseKontratua.Usuarios.FOTO));

        try {
            object.put(DatuBaseKontratua.Usuarios.ID, id);
            object.put(DatuBaseKontratua.Usuarios.NOMBRE, nombre);
            object.put(DatuBaseKontratua.Usuarios.FOTO, foto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(object));

        return object;
    }
}
