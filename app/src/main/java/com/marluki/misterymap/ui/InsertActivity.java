package com.marluki.misterymap.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.marluki.misterymap.R;
import com.marluki.misterymap.provider.DatuBaseKontratua;

import java.util.Calendar;
import java.util.Date;

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
        dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar current = Calendar.getInstance();
                int year = current.get(Calendar.YEAR);
                int month = current.get(Calendar.MONTH);
                final int day = current.get(Calendar.DAY_OF_MONTH);
                Calendar cal = Calendar.getInstance();
                cal.set(0,0,1);;

                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(InsertActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        dia.setText(year + "-" + month + "-" + day);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Elige una fecha...");
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        hora = (EditText)findViewById(R.id.txtHora);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrent = Calendar.getInstance();
                int hour = mcurrent.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrent.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(InsertActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hora.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Elige una hora...");
                timePickerDialog.show();
            }
        });

        btnGuardar = (Button)findViewById(R.id.btnGuardar);


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
}
