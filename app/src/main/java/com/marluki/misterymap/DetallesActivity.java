package com.marluki.misterymap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.marluki.misterymap.model.Comentario;
import com.marluki.misterymap.model.ComentarioViewModel;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.volley.VolleySingleton;

import java.util.ArrayList;

public class DetallesActivity extends AppCompatActivity {

    private ArrayList<String> arrayComentarios;
    private SearchAdapter searchAdapter;
    private ContentResolver resolver;
    private Comentario comentario;
    private ObjetoMapa objetoMapa;
    private String nombre, foto;
    private ImageLoader imageLoader;

    private RecyclerView recyclerView;
    private TextView txtPaisCiudad, txtDetalles, txtNombreUser;
    private NetworkImageView imgUser;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resolver = getContentResolver();
        objetoMapa = (ObjetoMapa)getIntent().getSerializableExtra("objeto");
        nombre = getIntent().getStringExtra(DatuBaseKontratua.Usuarios.NOMBRE);
        foto = getIntent().getStringExtra(DatuBaseKontratua.Usuarios.FOTO);

        toolbar.setTitle(objetoMapa.getNombre_objeto());

        recyclerView = (RecyclerView) findViewById(R.id.objetoContentRecyclerView);
        txtPaisCiudad = (TextView) findViewById(R.id.txtObjetoLocalozacionsTxt);
        txtDetalles = (TextView)findViewById(R.id.txtDetallesLugar);
        txtNombreUser = (TextView)findViewById(R.id.txtNombre_user);
        imgUser = (NetworkImageView) findViewById(R.id.imageUser);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        if(objetoMapa.getPais()!=null && objetoMapa.getCiudad()!=null) {
            txtPaisCiudad.setText(objetoMapa.getCiudad() + ", " + objetoMapa.getPais());
        }
        txtNombreUser.setText(nombre);
        imageLoader = VolleySingleton.getInstance(getApplicationContext()).getImageLoader();
        imgUser.setImageUrl(foto, imageLoader);

        txtDetalles.setText(objetoMapa.getDetalles());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String id = getIntent().getStringExtra("id");
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
        CursorRecyclerAdapter adapter = new CursorRecyclerAdapter(comentarioViewModelArrayList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
