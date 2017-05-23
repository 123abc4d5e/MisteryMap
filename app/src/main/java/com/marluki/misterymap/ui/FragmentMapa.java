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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marluki.misterymap.MainActivity;
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

    public FragmentMapa() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mGoogleApi = new GoogleApi(getContext(), getActivity(), getActivity());
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
        //mGoogleApi = new GoogleApi(getContext(), getActivity(), getActivity());
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
        if (mGoogleApi.getLastKnownLocation() != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mGoogleApi.getLastKnownLocation().getLatitude(), mGoogleApi.getLastKnownLocation().getLongitude()), 12));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 200, null);
        }

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

        final LoadMarkers loadMarkers = new LoadMarkers();
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
                markerClickListener.onMarkerClick(objetoMapa.getId());

                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApi.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApi.getGoogleApiClient().isConnected())
            mGoogleApi.disconnect();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApi.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApi.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApi.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 60) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApi.getGoogleApiClient());

                //mMap.addMarkerHistorico(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), "Ultima Posicion Conocida");
                //mMap.moveCamera(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 12);


                if (lastLocation != null)
                    mGoogleApi.updateLocation(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Toast.makeText(getContext(), "Permiso Denegado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnUpdateUIListener {
        void onUpdateUI(Marker marker);
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(Marker marker);
    }

    public interface OnMarkerClickListener {
        void onMarkerClick(String id);
    }



    public class LoadMarkers extends AsyncTask<Void, Void, Void> {
        Cursor c;

        @Override
        protected Void doInBackground(Void... params) {
            c = resolver.query(DatuBaseKontratua.Objetos_mapa.URI_CONTENT, null, null, null, null);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Marker marker = null;
                    ObjetoMapa objetoMapa = new ObjetoMapa(
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)),
                            c.getInt(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID)),
                            c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)),
                            c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)),
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
