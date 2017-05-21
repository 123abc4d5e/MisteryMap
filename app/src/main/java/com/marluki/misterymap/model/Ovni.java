package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Ovni {

    private String objeto_id;
    private String fecha;

    public Ovni() {
    }

    public Ovni(String objeto_id, String fecha) {
        this.objeto_id = objeto_id;
        this.fecha = fecha;
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object obj) {
        Ovni ov = (Ovni)obj;
        boolean b = !ov.getObjeto_id().equals(objeto_id) && ov.getObjeto_id() != null;
        boolean b1 = !ov.getFecha().equals(fecha) && ov.getFecha() != null;

        if (b || b1) return false;
        else return true;
    }
}
