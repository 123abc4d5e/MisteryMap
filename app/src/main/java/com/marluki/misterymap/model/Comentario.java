package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Comentario {

    private String id;
    private String objeto_id;
    private String usuario_id;
    private String comentario_id;
    private String texto;
    private String dia;
    private String hora;

    public Comentario(String id, String objeto_id, String usuario_id, String comentario_id, String texto, String dia, String hora) {
        this.id = id;
        this.objeto_id = objeto_id;
        this.usuario_id = usuario_id;
        this.comentario_id = comentario_id;
        this.texto = texto;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
        Comentario c = (Comentario) obj;
        boolean b = !c.getId().equals(id) && c.getId() != null;
        boolean b1 = !c.getObjeto_id().equals(objeto_id) && c.getObjeto_id() != null;
        boolean b2 = !c.getUsuario_id().equals(usuario_id) && c.getUsuario_id() != null;
        boolean b3 = !c.getComentario_id().equals(comentario_id);
        boolean b4 = !c.getDia().equals(dia) && c.getDia() != null;
        boolean b5 = !c.getHora().equals(hora) && c.getHora() != null;
        boolean b6 = !c.getTexto().equals(texto) && c.getTexto() != null;

        if (b || b1 || b2 || b3 || b4 || b5 || b6) return false;
        else return true;
    }
}
