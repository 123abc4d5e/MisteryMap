package com.marluki.misterymap.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.marluki.misterymap.MainActivity;
import com.marluki.misterymap.R;
import com.marluki.misterymap.provider.DatuBaseKontratua;

/**
 * Created by charl on 19/05/2017.
 */

public class InsertActivity extends AppCompatActivity {

    private int tipo;
    private String usuario_id;
    private double lat;
    private double lon;

    private EditText nombre, detalles, dia, hora;
    private Button btnGuardar;

    private Toolbar toolbar;
    private ContentResolver resolver;

    public static Intent newStartIntent(Context context, int tipo, String usuarioId, double latitud, double longitud) {
        Intent startIntent = new Intent(context, InsertActivity.class);
        startIntent.putExtra(DatuBaseKontratua.Objetos_mapa.TIPO_ID, tipo);
        startIntent.putExtra(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, usuarioId);
        startIntent.putExtra(DatuBaseKontratua.Objetos_mapa.LATITUD, latitud);
        startIntent.putExtra(DatuBaseKontratua.Objetos_mapa.LONGITUD, longitud);
        return startIntent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_activity);
        resolver = getContentResolver();

        toolbar = new Toolbar(getApplicationContext());
        setSupportActionBar(toolbar);

        final String id = DatuBaseKontratua.Objetos_mapa.generarIdObjetoMapa();
        tipo = getIntent().getIntExtra(DatuBaseKontratua.Objetos_mapa.TIPO_ID, 1);
        usuario_id = getIntent().getStringExtra(DatuBaseKontratua.Objetos_mapa.USUARIO_ID);
        lat = getIntent().getDoubleExtra(DatuBaseKontratua.Objetos_mapa.LATITUD, 0.0);
        lon = getIntent().getDoubleExtra(DatuBaseKontratua.Objetos_mapa.LONGITUD, 0.0);

        nombre = (EditText)findViewById(R.id.txtNombre);
        detalles = (EditText)findViewById(R.id.txtDetalles);
        dia = (EditText)findViewById(R.id.txtDia);
        hora = (EditText)findViewById(R.id.txtHora);

        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        UpdateUI(tipo);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(DatuBaseKontratua.Objetos_mapa.ID, id);
                values.put(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, usuario_id);
                values.put(DatuBaseKontratua.Objetos_mapa.TIPO_ID, tipo);
                values.put(DatuBaseKontratua.Objetos_mapa.LATITUD, lat);
                values.put(DatuBaseKontratua.Objetos_mapa.LONGITUD, lon);
                values.put(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO, nombre.getText().toString());
                values.put(DatuBaseKontratua.Objetos_mapa.DETALLES, detalles.getText().toString());
                Uri uriTipo = null;
                resolver.insert(DatuBaseKontratua.Objetos_mapa.URI_CONTENT, values);

                ContentValues valuesTipo = new ContentValues();
                valuesTipo.put(DatuBaseKontratua.Ovnis.OBJETO_ID, id);
                switch (tipo) {
                    case 1:
                        uriTipo = DatuBaseKontratua.Ovnis.URI_CONTENT;
                        valuesTipo.put(DatuBaseKontratua.Ovnis.FECHA, dia.getText().toString() + " " + hora.getText().toString());
                        break;
                    case 2:
                        uriTipo = DatuBaseKontratua.Fantasmas.URI_CONTENT;
                        break;
                    case 3:
                        uriTipo = DatuBaseKontratua.Historicos.URI_CONTENT;
                        valuesTipo.put(DatuBaseKontratua.Historicos.FECHA, dia.getText().toString() + " " + hora.getText().toString());
                        break;
                    case 4:
                        uriTipo = DatuBaseKontratua.SinResolver.URI_CONTENT;
                        valuesTipo.put(DatuBaseKontratua.SinResolver.FECHA, dia.getText().toString() + " " + hora.getText().toString());
                        break;
                }
                resolver.insert(uriTipo, valuesTipo);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }

    private void UpdateUI(int tipo) {
        switch (tipo) {
            case 1:
                toolbar.setTitle("Insertar Ovni");
                dia.setVisibility(View.VISIBLE);
                hora.setVisibility(View.VISIBLE);
                break;
            case 2:
                toolbar.setTitle("Insertar Fantasma");
                dia.setVisibility(View.INVISIBLE);
                hora.setVisibility(View.INVISIBLE);
                break;
            case 3:
                toolbar.setTitle("Insertar Historico");
                dia.setVisibility(View.VISIBLE);
                hora.setVisibility(View.VISIBLE);
                break;
            case 4:
                toolbar.setTitle("Insertar Sin Resolver");
                dia.setVisibility(View.VISIBLE);
                hora.setVisibility(View.VISIBLE);
                break;
        }
    }
}
