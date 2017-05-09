package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Comentario {

    private String id;
    private String objeto_id;
    private String usuario_id;
    private String comentario_id;
    private String dia;
    private String hora;

    public Comentario(String id, String objeto_id, String usuario_id, String comentario_id, String dia, String hora) {
        this.id = id;
        this.objeto_id = objeto_id;
        this.comentario_id = comentario_id;
        this.dia = dia;
        this.hora = hora;
    }

    public Comentario() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getComentario_id() {
        return comentario_id;
    }

    public void setComentario_id(String comentario_id) {
        this.comentario_id = comentario_id;
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
