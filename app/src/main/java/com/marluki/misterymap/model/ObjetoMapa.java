package com.marluki.misterymap.model;

import java.util.Comparator;

/**
 * Created by charl on 08/05/2017.
 */

public class ObjetoMapa implements Comparator<ObjetoMapa> {

    private String id;
    private int tipo_id;
    private Float latitud;
    private Float longitud;
    private String usuario_id;
    private String nombre_objeto;
    private String detalles;

    public ObjetoMapa() {

    }

    public ObjetoMapa(String id, int tipo_id, Float latitud, Float longitud, String usuario_id, String nombre_objeto, String detalles) {
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

    public int getTipo_id() {
        return tipo_id;
    }

    public void setTipo_id(int tipo_id) {
        this.tipo_id = tipo_id;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
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

    @Override
    public int compare(ObjetoMapa o1, ObjetoMapa o2) {
        return o1.getId().compareTo(o2.getId());
    }

    @Override
    public boolean equals(Object obj) {
        ObjetoMapa o = (ObjetoMapa)obj;
        boolean b = !o.getId().equals(id) && o.getId() != null;
        boolean b1 = o.getTipo_id() != tipo_id && o.getTipo_id() != 0;
        boolean b2 = o.getLatitud() != latitud && o.getLatitud() != null;
        boolean b3 = o.getLongitud() != longitud && o.getLongitud() != null;
        boolean b4 = !o.getUsuario_id().equals(usuario_id) && o.getUsuario_id() != null;
        boolean b5 = !o.getNombre_objeto().equals(nombre_objeto) && o.getNombre_objeto() != null;
        boolean b6 = !o.getDetalles().equals(detalles) && o.getDetalles() != null;

        if (b || b1 || b2 || b3 || b4 || b5 || b6)
            return false;
        else return true;
    }
}