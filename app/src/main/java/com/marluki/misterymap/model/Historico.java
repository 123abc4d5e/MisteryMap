package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Historico {

    String id;
    String fecha;

    public Historico() {
    }

    public Historico(String id, String fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
