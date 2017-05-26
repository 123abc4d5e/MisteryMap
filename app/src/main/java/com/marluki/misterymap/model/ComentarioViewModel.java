package com.marluki.misterymap.model;

/**
 * Created by lu_lu_000 on 25/05/2017.
 */

public class ComentarioViewModel {

    private int _id;
    private String id;
    private String objeto_id;
    private String usuario_id;
    private String comentario_id;
    private String texto;
    private String fecha;
    private String nombre_usuario;
    private String foto;

    public ComentarioViewModel(int _id, String id, String objeto_id, String usuario_id, String comentario_id, String texto, String fecha, String nombre_usuario, String foto) {
        this._id = _id;
        this.id = id;
        this.objeto_id = objeto_id;
        this.usuario_id = usuario_id;
        this.comentario_id = comentario_id;
        this.texto = texto;
        this.fecha = fecha;
        this.nombre_usuario = nombre_usuario;
        this.foto = foto;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
