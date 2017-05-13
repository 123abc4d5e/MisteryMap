package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Psicofonia {

    String id;
    String nombre_psicofonia;
    String objeto_id;
    String url;
    String usuario_id;

    public Psicofonia() {
    }

    public Psicofonia(String id, String nombre_psicofonia, String objeto_id, String url, String usuario_id) {
        this.id = id;
        this.nombre_psicofonia = nombre_psicofonia;
        this.objeto_id = objeto_id;
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

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
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

    @Override
    public boolean equals(Object obj) {
        Psicofonia c = (Psicofonia) obj;
        boolean b = !c.getId().equals(id) && c.getId() != null;
        boolean b1 = !c.getObjeto_id().equals(objeto_id) && c.getObjeto_id() != null;
        boolean b2 = !c.getUsuario_id().equals(usuario_id) && c.getUsuario_id() != null;
        boolean b3 = !c.getNombre_psicofonia().equals(nombre_psicofonia) && c.getNombre_psicofonia() != null;
        boolean b4 = !c.getUrl().equals(url) && c.getUrl() != null;

        if (b || b1 || b2 || b3 || b4) return false;
        else return true;
    }
}
