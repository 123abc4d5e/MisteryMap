package com.marluki.misterymap.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ObjetoMapa;

/**
 * Created by charl on 23/05/2017.
 */

public class AddMarker {



    public static Marker addMarkerOvni(ObjetoMapa objeto, GoogleMap googleMap) {
        Marker m = googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_ovni)).title(objeto.getNombre_objeto()).draggable(false));
        m.setTag(objeto);
        return m;

    }

    public static Marker addMarkerHistorico(ObjetoMapa objeto, GoogleMap googleMap) {
        Marker m = googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_historico)).title(objeto.getNombre_objeto()).draggable(false));
        m.setTag(objeto);
        return m;
    }

    public static Marker addMarkerFantasma(ObjetoMapa objeto, GoogleMap googleMap) {
        Marker m = googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_fantasma)).title(objeto.getNombre_objeto()).draggable(false));
        m.setTag(objeto);
        return m;
    }

    public static Marker addMarkerSinResolver(ObjetoMapa objeto, GoogleMap googleMap) {
        Marker m = googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_sin_resolver)).title(objeto.getNombre_objeto()).draggable(false));
        m.setTag(objeto);
        return m;
    }
}
