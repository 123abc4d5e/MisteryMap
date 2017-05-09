package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Psicofonia {

    String id;
    String nombre_psicofonia;
    String objetp_id;
    String url;
    String usuario_id;

    public Psicofonia() {
    }

    public Psicofonia(String id, String nombre_psicofonia, String objetp_id, String url, String usuario_id) {
        this.id = id;
        this.nombre_psicofonia = nombre_psicofonia;
        this.objetp_id = objetp_id;
        this.url = url;
        this.usuario_id = usuario_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_psicofonia() {
        return nombre_psicofonia;
    }

    public void setNombre_psicofonia(String nombre_psicofonia) {
        this.nombre_psicofonia = nombre_psicofonia;
    }

    public String getObjetp_id() {
        return objetp_id;
    }

    public void setObjetp_id(String objetp_id) {
        this.objetp_id = objetp_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }
}
