package com.marluki.misterymap.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ObjetoMapa2;

import java.util.ArrayList;

/**
 * Created by Markel on 26/05/2017.
 */

public class ObjetosAdapter extends RecyclerView.Adapter<ObjetosAdapter.ListaViewHolder> {

    private ArrayList<ObjetoMapa2> objetos;
    private Context context;

    public ObjetosAdapter(Context context, ArrayList<ObjetoMapa2> objetos) {
        this.objetos = objetos;
        this.context = context;
    }


    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recicler_item, parent, false);
        ObjetosAdapter.ListaViewHolder listaViewHolder = new ObjetosAdapter.ListaViewHolder(itemView);
        return listaViewHolder;
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int position) {
        ObjetoMapa2 objetoMapa = objetos.get(position);
        holder.bindObjeto(objetoMapa, position);
    }

    @Override
    public int getItemCount() {
        return objetos.size();
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {

        private TextView user;
        private TextView fecha;
        private TextView comentario;
        private NetworkImageView foto;

        public ListaViewHolder(View itemView) {
            super(itemView);

            user = (TextView) itemView.findViewById(R.id.txtUserComentario);
            foto = (NetworkImageView) itemView.findViewById(R.id.imageUserComent);
            fecha = (TextView) itemView.findViewById(R.id.txtFechaComent);
            comentario = (TextView) itemView.findViewById(R.id.txtComent);
            comentario.setTextSize(24);
        }

        public void bindObjeto(final ObjetoMapa2 objeto, final int position) {
            user.setText(objeto.getCiudad() + ", " + objeto.getPais());
            fecha.setText("LatLong: " + objeto.getLatitud() + " ,  " + objeto.getLongitud());
            comentario.setText(objeto.getNombre_objeto());
            int id =0;
            switch (objeto.getTipo_id()){
                case 1:
                    id = R.drawable.ic_alien;
                    break;
                case 2:
                    id = R.drawable.ic_ghost;
                    break;
                case 3:
                    id = R.drawable.ic_universidad;
                    break;
                case 4:
                    id = R.drawable.ic_question;
                    break;
            }
            foto.setDefaultImageResId(id);
        }

    }

}
