package com.marluki.misterymap.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ComentarioViewModel;
import com.marluki.misterymap.volley.VolleySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lu_lu_000 on 25/05/2017.
 */

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ListaViewHolder>{

    private ArrayList<ComentarioViewModel> comentarios;

    public ComentarioAdapter(ArrayList<ComentarioViewModel> comentarios){
        this.comentarios=comentarios;
    }

    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recicler_item,parent,false);
        ListaViewHolder listaViewHolder=new ListaViewHolder(itemView);
        return listaViewHolder;
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int position) {
        ComentarioViewModel comentarioViewModel=comentarios.get(position);
        holder.bindComentario(comentarioViewModel,position);
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder{

        private TextView user;
        private TextView fecha;
        private TextView comentario;
        private NetworkImageView foto;
        private ImageLoader imageLoader;

        public ListaViewHolder(View itemView) {
            super(itemView);

            imageLoader = VolleySingleton.getInstance(itemView.getContext())
                    .getImageLoader();

            user=(TextView)itemView.findViewById(R.id.txtUserComentario);
            foto=(NetworkImageView) itemView.findViewById(R.id.imageUserComent);
            fecha=(TextView)itemView.findViewById(R.id.txtFechaComent);
            comentario=(TextView)itemView.findViewById(R.id.txtComent);
        }

        public void bindComentario(final ComentarioViewModel comentarioViewModel,final int position) {
            user.setText(comentarioViewModel.getNombre_usuario());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = simpleDateFormat.parse(comentarioViewModel.getFecha());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            fecha.setText(simpleDateFormat2.format(date));
            comentario.setText(comentarioViewModel.getTexto());
            foto.setImageUrl(comentarioViewModel.getFoto(), imageLoader);

        }

    }


}
