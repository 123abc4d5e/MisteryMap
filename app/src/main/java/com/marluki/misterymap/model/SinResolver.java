package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class SinResolver {

    String objeto_id;
    String fecha;

    public SinResolver() {
    }

    public SinResolver(String objeto_id, String fecha) {
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
}
