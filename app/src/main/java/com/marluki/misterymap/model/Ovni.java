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
}
