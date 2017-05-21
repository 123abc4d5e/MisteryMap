package com.marluki.misterymap.view;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marluki.misterymap.R;
import com.marluki.misterymap.ui.FirstMapFragment;

/**
 * Created by Markel on 18/05/2017.
 */

public class Map implements OnMapReadyCallback {

    protected Context context;
    private GoogleMap mMap;
    private GoogleApi googleApi;
    private boolean mapStyleisLight = true;


    public Map(Context context, GoogleApi googleApi)
    {
        this.context = context;
        this.googleApi = googleApi;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //  Ponemos el estilo al mapa
        //  Según el valor de mapStyleisLight
        if(mapStyleisLight)
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.formato_mapa_light));
        else
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.formato_mapa_dark));


        // Add a marker in Sydney, Australia, and move the camera.
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_ovni)).title("Marker Ovni"));
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(mapStyleisLight)
                {
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.formato_mapa_dark));
                    mapStyleisLight = false;
                }
                else
                {
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.formato_mapa_light));
                    mapStyleisLight = true;
                }

            }
        });

        addMarkerFantasma(new LatLng(40.00000, -3.00), "Fantasma");
        addMarkerHistorico(new LatLng(41.0000, 4.00), "Historico");
        addMarkerOvni(new LatLng(39.000, -2.00), "Ovni");
        addMarkerSinResolver(new LatLng(38.000, -2.00), "Sin Resolver");

        if(googleApi.getLastKnownLocation()!=null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googleApi.getLastKnownLocation().getLatitude(), googleApi.getLastKnownLocation().getLongitude()), 5));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 200, null);
        }

    }






    /*****************
     //
     //     Markers Personalizados
     //
    *******************/
    public void addMarkerOvni(LatLng latLng, String titulo)
    {
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_ovni)).title(titulo));

    }

    public void addMarkerHistorico(LatLng latLng, String titulo)
    {
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_historico)).title(titulo));
    }

    public void addMarkerFantasma(LatLng latLng, String titulo)
    {
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_fantasma)).title(titulo));
    }

    public void addMarkerSinResolver(LatLng latLng, String titulo)
    {
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_sin_resolver)).title(titulo));
    }


    //

    public void moveCamera(LatLng latLng, int zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void getMapAsync(FirstMapFragment firstMapFragment)
    {
        firstMapFragment.getMapAsync(this);
    }
}
