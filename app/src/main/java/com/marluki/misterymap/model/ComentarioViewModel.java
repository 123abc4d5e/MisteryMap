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

    public ComentarioViewModel(String id, String objeto_id, String usuario_id, String comentario_id, String texto, String fecha, String nombre_usuario) {
        this.id = id;
        this.objeto_id = objeto_id;
        this.usuario_id = usuario_id;
        this.comentario_id = comentario_id;
        this.texto = texto;
        this.fecha = fecha;
        this.nombre_usuario = nombre_usuario;
    }
    public int get_id(){
        return _id;
    }
    public int set_id(){
        return _id;
    }

    public String getId() {
        return id;
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public String getComentario_id() {
        return comentario_id;
    }

    public String getTexto() {
        return texto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }
}
