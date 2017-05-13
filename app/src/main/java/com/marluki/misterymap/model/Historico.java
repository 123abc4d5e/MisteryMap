package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Historico {

    String objeto_id;
    String fecha;

    public Historico() {
    }

    public Historico(String objeto_id, String fecha) {
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
        Historico h = (Historico)obj;
        boolean b = !h.getObjeto_id().equals(objeto_id) && h.getObjeto_id() != null;
        boolean b1 = !h.getFecha().equals(fecha) && h.getFecha() != null;

        if (b || b1) return false;
        else return true;
    }
}
