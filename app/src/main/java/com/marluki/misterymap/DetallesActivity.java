package com.marluki.misterymap;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.marluki.misterymap.model.Comentario;
import com.marluki.misterymap.model.ComentarioViewModel;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.ui.ComentarioFragment;

import java.util.ArrayList;

public class DetallesActivity extends AppCompatActivity implements ComentarioFragment.OnComentInsertListener{

    private ArrayList<String> arrayComentarios;
    private SearchAdapter searchAdapter;
    private ContentResolver resolver;
    private Comentario comentario;
    private FloatingActionButton fav;
    private ComentarioFragment comentarioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        comentarioFragment=new ComentarioFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_gora,R.anim.slide_behera,R.anim.pop_exit,R.anim.pop_enter);
                transaction.add(R.id.containerDetalles,comentarioFragment);
                transaction.commit();
            }
        });
        String id=getIntent().getStringExtra("id");
        Uri uri= DatuBaseKontratua.Objetos_mapa.crearUriParaComentario(id);

        resolver=getContentResolver();

        Cursor c=resolver.query(uri,null,null,null,null);
        ArrayList<ComentarioViewModel> comentarioViewModelArrayList=new ArrayList<ComentarioViewModel>();

        while(c.moveToNext()){
            ComentarioViewModel comentarioViewModel=new ComentarioViewModel(
                    c.getString(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("objeto_id")),
                    c.getString(c.getColumnIndex("usuario_id")),
                    c.getString(c.getColumnIndex("comentario_id")),
                    c.getString(c.getColumnIndex("texto")),
                    c.getString(c.getColumnIndex("fecha")),
                    c.getString(c.getColumnIndex("nombre_usuario"))
            );
            comentarioViewModelArrayList.add(comentarioViewModel);

        }
        CursorRecyclerAdapter adapter=new CursorRecyclerAdapter(comentarioViewModelArrayList);

        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.objetoContentRecyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public void onComentInsert() {
        if(comentarioFragment!=null){
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_gora,R.anim.slide_behera,R.anim.pop_exit,R.anim.pop_enter);
            transaction.remove(comentarioFragment);
            transaction.commit();
            comentarioFragment=null;
        }
    }

    @Override
    public void onBackPressed() {
        if(comentarioFragment!=null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
            transaction.remove(comentarioFragment);
            transaction.commit();
            comentarioFragment = null;
        }else
        super.onBackPressed();
    }
}
