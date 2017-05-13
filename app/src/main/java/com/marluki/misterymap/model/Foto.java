package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Foto {

    String id;
    String nombre_foto;
    String url;
    String objeto_id;
    String usuario_id;

    public Foto() {
    }

    public Foto(String id, String nombre_foto, String url, String objeto_id, String usuario_id) {
        this.id = id;
        this.nombre_foto = nombre_foto;
        this.url = url;
        this.objeto_id = objeto_id;
        this.usuario_id = usuario_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    @Override
    public boolean equals(Object obj) {
        Foto c = (Foto) obj;
        boolean b = !c.getId().equals(id) && c.getId() != null;
        boolean b1 = !c.getObjeto_id().equals(objeto_id) && c.getObjeto_id() != null;
        boolean b2 = !c.getUsuario_id().equals(usuario_id) && c.getUsuario_id() != null;
        boolean b3 = !c.getNombre_foto().equals(nombre_foto) && c.getNombre_foto() != null;
        boolean b4 = !c.getUrl().equals(url) && c.getUrl() != null;

        if (b || b1 || b2 || b3 || b4) return false;
        else return true;
    }
}
