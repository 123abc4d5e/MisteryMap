package com.marluki.misterymap;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.marluki.misterymap.model.Comentario;
import com.marluki.misterymap.model.ComentarioViewModel;
import com.marluki.misterymap.volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu_lu_000 on 25/05/2017.
 */

public class CursorRecyclerAdapter extends RecyclerView.Adapter<CursorRecyclerAdapter.ListaViewHolder>{

    private ArrayList<ComentarioViewModel> comentarios;

    public CursorRecyclerAdapter(ArrayList<ComentarioViewModel> comentarios){
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

        public void bindComentario(final ComentarioViewModel comentarioViewModel,final int position){
            user.setText(comentarioViewModel.getNombre_usuario());
            fecha.setText(comentarioViewModel.getFecha());
            comentario.setText(comentarioViewModel.getTexto());
            foto.setImageUrl(comentarioViewModel.getFoto(), imageLoader);

        }

    }


}
