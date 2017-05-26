package com.marluki.misterymap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marluki.misterymap.model.ObjetoMapa;

import java.util.ArrayList;

/**
 * Created by Markel on 26/05/2017.
 */

public class ObjetosAdapter extends RecyclerView.Adapter<ObjetosAdapter.ListaViewHolder> {

    private ArrayList<ObjetoMapa> objetos;

    public ObjetosAdapter(ArrayList<ObjetoMapa>objetos){
        this.objetos=objetos;
    }


    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recicler_item,parent,false);
        ObjetosAdapter.ListaViewHolder listaViewHolder=new ObjetosAdapter.ListaViewHolder(itemView);
        return listaViewHolder;
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int position) {
        ObjetoMapa objetoMapa=objetos.get(position);
        holder.bindObjeto(objetoMapa,position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder{

        private TextView user;
        private TextView fecha;
        private TextView comentario;
        private ImageView foto;

        public ListaViewHolder(View itemView) {
            super(itemView);

            user=(TextView)itemView.findViewById(R.id.txtUserComentario);
            foto=(ImageView)itemView.findViewById(R.id.imageUserComent);
            fecha=(TextView)itemView.findViewById(R.id.txtFechaComent);
            comentario=(TextView)itemView.findViewById(R.id.txtComent);
        }

        public void bindObjeto(final ObjetoMapa objeto, final int position){
            user.setText(objeto.getNombre_objeto());
            fecha.setText(objeto.getLatitud()+" , "+objeto.getLongitud());
            this.comentario.setText(objeto.getDetalles());
        }

    }

}
