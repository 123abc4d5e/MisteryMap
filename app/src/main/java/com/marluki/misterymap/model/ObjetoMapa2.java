package com.marluki.misterymap.model;

import java.util.Comparator;

/**
 * Created by charl on 08/05/2017.
 */

public class ObjetoMapa2 implements Comparator<ObjetoMapa2> {

    private int _id;
    private String id;
    private int tipo_id;
    private Double latitud;
    private Double longitud;
    private String usuario_id;
    private String nombre_objeto;
    private String detalles;
    private String pais;
    private String ciudad;

    public ObjetoMapa2() {
    }

    public ObjetoMapa2(int _id, String id, int tipo_id, Double latitud, Double longitud, String usuario_id, String nombre_objeto, String detalles, String pais, String ciudad) {
        this._id = _id;
        this.id = id;
        this.tipo_id = tipo_id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.usuario_id = usuario_id;
        this.nombre_objeto = nombre_objeto;
        this.detalles = detalles;
        this.pais = pais;
        this.ciudad = ciudad;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public int compare(ObjetoMapa2 o1, ObjetoMapa2 o2) {
        return o1.getId().compareTo(o2.getId());
    }

    @Override
    public boolean equals(Object obj) {
        ObjetoMapa2 o = (ObjetoMapa2)obj;
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