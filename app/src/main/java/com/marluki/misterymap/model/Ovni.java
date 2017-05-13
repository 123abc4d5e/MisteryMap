package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Ovni {

    private String objeto_id;
    private String dia;
    private String hora;

    public Ovni() {
    }

    public Ovni(String objeto_id, String dia, String hora) {
        this.objeto_id = objeto_id;
        this.dia = dia;
        this.hora = hora;
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public boolean equals(Object obj) {
        Ovni ov = (Ovni)obj;
        boolean b = !ov.getObjeto_id().equals(objeto_id) && ov.getObjeto_id() != null;
        boolean b1 = !ov.getDia().equals(dia) && ov.getDia() != null;
        boolean b2 = !ov.getHora().equals(hora) && ov.getHora() != null;

        if (b || b1 || b2) return false;
        else return true;
    }
}
