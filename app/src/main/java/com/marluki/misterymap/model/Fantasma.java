package com.marluki.misterymap.model;

/**
 * Created by charl on 08/05/2017.
 */

public class Fantasma {

      private  String objeto_id;
      private  int visto;
      private int fake;

    public Fantasma(String objeto_id, int visto, int fake) {
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

    public int getVisto() {
        return visto;
    }

    public void setVisto(int visto) {
        this.visto = visto;
    }

    public int getFake() {
        return fake;
    }

    public void setFake(int fake) {
        this.fake = fake;
    }

    @Override
    public boolean equals(Object obj) {
        Fantasma f = (Fantasma)obj;
        boolean b = !f.getObjeto_id().equals(objeto_id) && f.getObjeto_id() != null;
        boolean b1 = f.getVisto() != visto;
        boolean b2 = f.getFake() != fake;

        if (b || b1 || b2) return false;
        else return true;
    }
}
