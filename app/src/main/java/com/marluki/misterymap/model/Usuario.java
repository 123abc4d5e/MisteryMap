package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Usuario {

    String id;
    String nombre;
    String foto;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object obj) {
        Usuario c = (Usuario) obj;
        boolean b = !c.getId().equals(id) && c.getId() != null;
        boolean b1 = !c.getNombre().equals(nombre) && c.getNombre() != null;
        boolean b2 = !c.getFoto().equals(foto) && c.getFoto() != null;

        if (b || b1 || b2) return false;
        else return true;
    }
}
