package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class ObjetoMapa {

    private String id;
    private String tipo_id;
    private String latitud;
    private String longitud;
    private String usuario_id;
    private String nombre_objeto;
    private String detalles;

    public ObjetoMapa() {
    }

    public ObjetoMapa(String id, String tipo_id, String latitud, String longitud, String usuario_id, String nombre_objeto, String detalles) {
        this.id = id;
        this.tipo_id = tipo_id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.usuario_id = usuario_id;
        this.nombre_objeto = nombre_objeto;
        this.detalles = detalles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo_id() {
        return tipo_id;
    }

    public void setTipo_id(String tipo_id) {
        this.tipo_id = tipo_id;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getNombre_objeto() {
        return nombre_objeto;
    }

    public void setNombre_objeto(String nombre_objeto) {
        this.nombre_objeto = nombre_objeto;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
