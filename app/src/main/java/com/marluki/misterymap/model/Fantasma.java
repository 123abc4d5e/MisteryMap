package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Fantasma {

      private  String objeto_id;
      private  String visto;
      private String fake;

    public Fantasma(String objeto_id, String visto, String fake) {
        this.objeto_id = objeto_id;
        this.visto = visto;
        this.fake = fake;
    }

    public Fantasma() {
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
    }

    public String getVisto() {
        return visto;
    }

    public void setVisto(String visto) {
        this.visto = visto;
    }

    public String getFake() {
        return fake;
    }

    public void setFake(String fake) {
        this.fake = fake;
    }
}
