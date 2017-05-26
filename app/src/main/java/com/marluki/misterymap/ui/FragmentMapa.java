package com.marluki.misterymap.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.utils.AddMarker;
import com.marluki.misterymap.view.GoogleApi;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMapa.OnMapLongClickListener,FragmentMapa.OnMarkerClickListener,
 * FragmentMapa.OnUpdateUIListener} interfaces
 * to handle interaction events.
 */
public class FragmentMapa extends SupportMapFragment implements OnMapReadyCallback {

    private OnUpdateUIListener uiListener;
    private OnMapLongClickListener longClickListener;
    private OnMarkerClickListener markerClickListener;

    private GoogleApi mGoogleApi;
    private boolean mapStyleisLight = true;
    private Marker longMarker;
    private ContentResolver resolver;
    private GoogleMap googleMap;
    private ArrayList<Marker> markers;
    private static LoadMarkers loadMarkers;

    public FragmentMapa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        markers = new ArrayList<Marker>();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resolver = getActivity().getContentResolver();
        getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateUIListener) {
            uiListener = (OnUpdateUIListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUpdateUIListener");
        }
        if (context instanceof OnMapLongClickListener) {
            longClickListener = (OnMapLongClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapLongClickListener");
        }
        if (context instanceof OnMarkerClickListener) {
            markerClickListener = (OnMarkerClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMarkerClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        uiListener = null;
        longClickListener = null;
        markerClickListener = null;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
//        if (mGoogleApi.getLastKnownLocation() != null) {
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mGoogleApi.getLastKnownLocation().getLatitude(), mGoogleApi.getLastKnownLocation().getLongitude()), 12));
//            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 200, null);
//        }

        if (mapStyleisLight)
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.formato_mapa_light));
        else
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.formato_mapa_dark));


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (longMarker != null) {
                    longMarker.remove();
                    longMarker = null;
                } else {
                    if (mapStyleisLight) {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.formato_mapa_light));
                    } else {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.formato_mapa_dark));
                    }
                    mapStyleisLight = !mapStyleisLight;
                }
                if (uiListener != null)
                    uiListener.onUpdateUI(longMarker);

            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (longMarker != null)
                    longMarker.remove();
                longMarker = googleMap.addMarker(new MarkerOptions().title("Nueva posición").position(latLng).draggable(true));

                if (longClickListener != null)
                    longClickListener.onMapLongClick(longMarker);

                if (uiListener != null)
                    uiListener.onUpdateUI(longMarker);
            }
        });

        loadMarkers = new LoadMarkers();
        loadMarkers.execute();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("onMarkerClick", "Click en marker " + marker.getTitle());
                if (longMarker != null) {
                    longMarker.remove();
                    longMarker = null;
                }
                if(uiListener!=null)
                    uiListener.onUpdateUI(longMarker);

                ObjetoMapa objetoMapa = (ObjetoMapa) marker.getTag();
                markerClickListener.onMarkerClick(marker);

                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public interface OnUpdateUIListener {
        void onUpdateUI(Marker marker);
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(Marker marker);
    }

    public interface OnMarkerClickListener {
        void onMarkerClick(Marker marker);
    }

    public void moveToLocation(Double lat, Double lon) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    public void moveToLocation(Double lat, Double lon, boolean bol) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        if(bol) {
            longMarker = googleMap.addMarker(new MarkerOptions().title("Nueva Posición").position(new LatLng(lat, lon)).draggable(true));
            uiListener.onUpdateUI(longMarker);
        }
    }







    public class LoadMarkers extends AsyncTask<Void, Void, Void> {
        Cursor c;

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            c = resolver.query(DatuBaseKontratua.Objetos_mapa.URI_CONTENT, null, null, null, null);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Marker marker = null;
                    ObjetoMapa objetoMapa = new ObjetoMapa(
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)),
                            c.getInt(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID)),
                            c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)),
                            c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.USUARIO_ID)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.DETALLES)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.PAIS)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.CIUDAD))
                    );

                    switch (objetoMapa.getTipo_id()) {
                        case 1:
                            marker = AddMarker.addMarkerOvni(objetoMapa, googleMap);
                            break;
                        case 2:
                            marker = AddMarker.addMarkerFantasma(objetoMapa, googleMap);
                            break;
                        case 3:
                            marker = AddMarker.addMarkerHistorico(objetoMapa, googleMap);
                            break;
                        case 4:
                            marker = AddMarker.addMarkerSinResolver(objetoMapa, googleMap);
                            break;
                    }
                    markers.add(marker);
                }
            }
        }
    }
}
