package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Tipo {

    int id;
    String nombre_tipo;

    public Tipo() {
    }

    public Tipo(int id, String nombre_tipo) {
        this.id = id;
        this.nombre_tipo = nombre_tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }

    @Override
    public boolean equals(Object obj) {
        Tipo h = (Tipo) obj;
        boolean b = h.getId() != id && h.getId() != 0;
        boolean b1 = !h.getNombre_tipo().equals(nombre_tipo) && h.getNombre_tipo() != null;

        if (b || b1) return false;
        else return true;
    }
}
