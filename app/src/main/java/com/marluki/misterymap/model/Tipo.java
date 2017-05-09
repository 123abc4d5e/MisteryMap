package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Tipo {

    String id;
    String nombre_tipo;

    public Tipo() {
    }

    public Tipo(String id, String nombre_tipo) {
        this.id = id;
        this.nombre_tipo = nombre_tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }
}
