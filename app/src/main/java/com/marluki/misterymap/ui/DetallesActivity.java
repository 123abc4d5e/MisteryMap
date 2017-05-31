package com.marluki.misterymap.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ComentarioViewModel;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.volley.VolleySingleton;

import java.util.ArrayList;

public class DetallesActivity extends AppCompatActivity implements ComentarioFragment.OnComentInsertListener {

    private ContentResolver resolver;
    private ComentarioFragment comentarioFragment;
    private String creatorUser, creatorImage;
    private ImageLoader imageLoader;

    private ObjetoMapa objetoMapa;
    private FloatingActionButton fab;
    private TextView txtPais, txtDetalles, txtNombreUser;
    private NetworkImageView userImage;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resolver = getContentResolver();
        imageLoader = VolleySingleton.getInstance(getApplicationContext()).getImageLoader();

        objetoMapa = (ObjetoMapa) getIntent().getSerializableExtra("objeto");
        creatorImage = getIntent().getStringExtra(DatuBaseKontratua.Usuarios.FOTO);
        creatorUser = getIntent().getStringExtra(DatuBaseKontratua.Usuarios.NOMBRE);
        setTitle(objetoMapa.getNombre_objeto());


        recyclerView = (RecyclerView) findViewById(R.id.objetoContentRecyclerView);
        txtNombreUser = (TextView) findViewById(R.id.txtNombre_user);
        txtDetalles = (TextView) findViewById(R.id.txtDetallesLugar);
        txtPais = (TextView) findViewById(R.id.txtObjetoLocalozacions);
        userImage = (NetworkImageView) findViewById(R.id.imageUser);

        if (objetoMapa.getPais() != null && objetoMapa.getCiudad() != null) {
            txtPais.setText(objetoMapa.getCiudad() + ", " + objetoMapa.getPais());
        }
        txtDetalles.setText(objetoMapa.getDetalles());
        txtNombreUser.setText(creatorUser);

        userImage.setImageUrl(creatorImage, imageLoader);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(DatuBaseKontratua.Comentarios.OBJETO_ID, objetoMapa.getId());
                comentarioFragment = new ComentarioFragment();
                comentarioFragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
                transaction.add(R.id.containerDetalles, comentarioFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        String id = objetoMapa.getId();
        Uri uri = DatuBaseKontratua.Objetos_mapa.crearUriParaComentario(id);


        Cursor c = resolver.query(uri, null, null, null, null);
        ArrayList<ComentarioViewModel> comentarioViewModelArrayList = new ArrayList<ComentarioViewModel>();

        while (c.moveToNext()) {
            ComentarioViewModel comentarioViewModel = new ComentarioViewModel(
                    c.getInt(c.getColumnIndex(BaseColumns._ID)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.ID)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.OBJETO_ID)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.USUARIO_ID)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.COMENTARIO_ID)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.TEXTO)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Comentarios.FECHA)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.NOMBRE)),
                    c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.FOTO))
            );
            comentarioViewModelArrayList.add(comentarioViewModel);

        }
        ComentarioAdapter adapter = new ComentarioAdapter(comentarioViewModelArrayList);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }


    @Override
    public void onComentInsert() {
        if (comentarioFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
            transaction.remove(comentarioFragment);
            transaction.commit();
            comentarioFragment = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (comentarioFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
            transaction.remove(comentarioFragment);
            transaction.commit();
            comentarioFragment = null;
        } else
            super.onBackPressed();
    }
}
