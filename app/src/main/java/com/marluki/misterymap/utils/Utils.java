package com.marluki.misterymap.utils;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.provider.MisteryProvider;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by charl on 13/05/2017.
 */

public class Utils {

    public static JSONObject deCursorAJSONObject(Cursor c, Uri uri, String token) {
        String id, tipo_id, usuario_id, nombre_objeto, objeto_id, fecha, dia, hora, url;
        JSONObject objetos = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (MisteryProvider.uriMatcher.match(uri)) {
            case MisteryProvider.OBJETOS_MAPA:
                id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID));
                tipo_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID));
                Float latitud = c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD));
                Float longitud = c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD));
                usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.USUARIO_ID));
                nombre_objeto = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO));
                String detalles = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.DETALLES));

                try {
                    objetos.put(DatuBaseKontratua.Objetos_mapa.ID, id);
                    objetos.put(DatuBaseKontratua.Objetos_mapa.TIPO_ID, tipo_id);
                    objetos.put(DatuBaseKontratua.Objetos_mapa.LATITUD, latitud);
                    objetos.put(DatuBaseKontratua.Objetos_mapa.LONGITUD, longitud);
                    objetos.put(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, usuario_id);
                    objetos.put(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO, nombre_objeto);
                    objetos.put(DatuBaseKontratua.Objetos_mapa.DETALLES, detalles);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.OVNIS:
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Ovnis.OBJETO_ID));
                dia = c.getString(c.getColumnIndex(DatuBaseKontratua.Ovnis.DIA));
                hora = c.getString(c.getColumnIndex(DatuBaseKontratua.Ovnis.HORA));

                try {
                    objetos.put(DatuBaseKontratua.Ovnis.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.Ovnis.DIA, dia);
                    objetos.put(DatuBaseKontratua.Ovnis.HORA, hora);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.FANTASMAS:
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fantasmas.OBJETO_ID));
                int visto = c.getInt(c.getColumnIndex(DatuBaseKontratua.Fantasmas.VISTO));
                int fake = c.getInt(c.getColumnIndex(DatuBaseKontratua.Fantasmas.FAKE));

                try {
                    objetos.put(DatuBaseKontratua.Fantasmas.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.Fantasmas.VISTO, visto);
                    objetos.put(DatuBaseKontratua.Fantasmas.FAKE, fake);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.HISTORICOS:
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Historicos.OBJETO_ID));
                fecha = c.getString(c.getColumnIndex(DatuBaseKontratua.Historicos.FECHA));

                try {
                    objetos.put(DatuBaseKontratua.Historicos.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.Historicos.FECHA, fecha);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.SIN_RESOLVER:
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.SinResolver.OBJETO_ID));
                fecha = c.getString(c.getColumnIndex(DatuBaseKontratua.SinResolver.FECHA));

                try {
                    objetos.put(DatuBaseKontratua.SinResolver.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.SinResolver.FECHA, fecha);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.COMENTARIOS:
                id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.ID));
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.OBJETO_ID));
                String comentario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.COMENTARIO_ID));
                String texto = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.TEXTO));
                usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.USUARIO_ID));
                dia = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.DIA));
                hora = c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.HORA));

                try {
                    objetos.put(DatuBaseKontratua.Comentarios.ID, id);
                    objetos.put(DatuBaseKontratua.Comentarios.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.Comentarios.COMENTARIO_ID, comentario_id);
                    objetos.put(DatuBaseKontratua.Comentarios.TEXTO, texto);
                    objetos.put(DatuBaseKontratua.Comentarios.USUARIO_ID, usuario_id);
                    objetos.put(DatuBaseKontratua.Comentarios.DIA, dia);
                    objetos.put(DatuBaseKontratua.Comentarios.HORA, hora);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.PSICOFONIAS:
                id = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.ID));
                String nombre_psicofonia = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA));
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.OBJETO_ID));
                url = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.URL));
                usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Psicofonias.USUARIO_ID));

                try {
                    objetos.put(DatuBaseKontratua.Psicofonias.ID, id);
                    objetos.put(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA, nombre_psicofonia);
                    objetos.put(DatuBaseKontratua.Psicofonias.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.Psicofonias.URL, url);
                    objetos.put(DatuBaseKontratua.Psicofonias.USUARIO_ID, usuario_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.FOTOS:
                id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.ID));
                String nombre_foto = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.NOMBRE_FOTO));
                objeto_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.OBJETO_ID));
                url = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.URL));
                usuario_id = c.getString(c.getColumnIndex(DatuBaseKontratua.Fotos.USUARIO_ID));

                try {
                    objetos.put(DatuBaseKontratua.Fotos.ID, id);
                    objetos.put(DatuBaseKontratua.Fotos.NOMBRE_FOTO, nombre_foto);
                    objetos.put(DatuBaseKontratua.Fotos.OBJETO_ID, objeto_id);
                    objetos.put(DatuBaseKontratua.Fotos.URL, url);
                    objetos.put(DatuBaseKontratua.Fotos.USUARIO_ID, usuario_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MisteryProvider.USUARIOS:
                id = c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.ID));
                String nombre = c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.NOMBRE));
                Float foto = c.getFloat(c.getColumnIndex(DatuBaseKontratua.Usuarios.FOTO));

                try {
                    objetos.put(DatuBaseKontratua.Usuarios.ID, id);
                    objetos.put(DatuBaseKontratua.Usuarios.NOMBRE, nombre);
                    objetos.put(DatuBaseKontratua.Usuarios.FOTO, foto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

        Log.i("Cursor a JSONObject", String.valueOf(objetos));
        try {
            objetos.put("datos", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objetos;
    }
}
