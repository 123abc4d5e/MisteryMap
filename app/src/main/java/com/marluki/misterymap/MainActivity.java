package com.marluki.misterymap;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.marluki.misterymap.provider.DatuBaseKontratua;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public class Prueba extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ContentResolver r = getContentResolver();
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Tipos.URI_CONTENT)
                    .withValue(DatuBaseKontratua.Tipos.ID, 1)
                    .withValue(DatuBaseKontratua.Tipos.NOMBRE_TIPO, "Ovni")
                    .build());
            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Tipos.URI_CONTENT)
                    .withValue(DatuBaseKontratua.Tipos.ID, 2)
                    .withValue(DatuBaseKontratua.Tipos.NOMBRE_TIPO, "Fantasma")
                    .build());
            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Tipos.URI_CONTENT)
                    .withValue(DatuBaseKontratua.Tipos.ID, 3)
                    .withValue(DatuBaseKontratua.Tipos.NOMBRE_TIPO, "Historico")
                    .build());
            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Tipos.URI_CONTENT)
                    .withValue(DatuBaseKontratua.Tipos.ID, 4)
                    .withValue(DatuBaseKontratua.Tipos.NOMBRE_TIPO, "Sin Resolver")
                    .build());

            String usuario1 = DatuBaseKontratua.Usuarios.generarIdUsuario();
            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Usuarios.URI_CONTENT)
                    .withValue(DatuBaseKontratua.Usuarios.ID, usuario1)
                    .withValue(DatuBaseKontratua.Usuarios.NOMBRE, "Fulanito")
                    .withValue(DatuBaseKontratua.Usuarios.APELLIDO, "Huerfanito")
                    .withValue(DatuBaseKontratua.Usuarios.CORREO, "huerfanito@gmail.com")
                    .build());

            String obj1 = DatuBaseKontratua.Objetos_mapa.generarIdObjetoMapa();
            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Objetos_mapa.URI_CONTENIDO)
                    .withValue(DatuBaseKontratua.Objetos_mapa.ID, obj1)
                    .withValue(DatuBaseKontratua.Objetos_mapa.TIPO_ID, 1)
                    .withValue(DatuBaseKontratua.Objetos_mapa.LATITUD, 43.293999f)
                    .withValue(DatuBaseKontratua.Objetos_mapa.LONGITUD, -1.988579f)
                    .withValue(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, usuario1)
                    .withValue(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO, "3 Ovnis en Donosti")
                    .withValue(DatuBaseKontratua.Objetos_mapa.DETALLES, "3 Ovnis vistos en las inmediaciones de San Sebastian.")
                    .build());

            ops.add(ContentProviderOperation.newInsert(DatuBaseKontratua.Objetos_mapa.crearUriParaOvni(obj1))
                    .withValue(DatuBaseKontratua.Ovnis.DIA, "24-04-2017")
                    .withValue(DatuBaseKontratua.Ovnis.HORA, "15:45:00")
                    .build());

            try {
                r.applyBatch(DatuBaseKontratua.AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getApplicationContext().deleteDatabase("mistery.db");
        new Prueba().execute(); // se hacen uns insert en segundo plano

        //PRUEBA DE UN QUERY
        Cursor c = getContentResolver().query(DatuBaseKontratua.Objetos_mapa.URI_CONTENIDO, null, null, null, null);
        String emaitza = "";
        Uri uri_detalles = null;
        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < c.getColumnNames().length; i++) {
                    emaitza += " | " + c.getColumnName(i) + ": " + c.getString(i);
                }
                uri_detalles = DatuBaseKontratua.Objetos_mapa.crearUriParaOvni(c.getString(1));
            } while (c.moveToNext());

        }

        Cursor e = getContentResolver().query(uri_detalles, null, null, null, null);

        while (e.moveToNext()) {
            emaitza += "\n\nRESPUESTA CON TABLA OVNI\n";
            for (int i = 0; i < e.getColumnNames().length; i++) {
                emaitza += " | " + e.getColumnName(i) + ": " + e.getString(i);
            }
        }
        //colocamos el string para ver las columnas y valores que ha devuelto cada query en el cursor
        ((TextView) findViewById(R.id.txtAlgo)).setText(emaitza);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
