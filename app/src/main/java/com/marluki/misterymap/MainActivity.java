package com.marluki.misterymap;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.sync.SyncHelper;
import com.marluki.misterymap.ui.BlankFragment;
import com.marluki.misterymap.ui.FirstMapFragment;
import com.marluki.misterymap.ui.InsertActivity;
import com.marluki.misterymap.view.GoogleApi;
import com.marluki.misterymap.view.Map;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnClickListener,
        BlankFragment.OnFragmentInteractionListener, FirstMapFragment.OnFragmentInteractionListener {


    private FirstMapFragment mFirstFirstMapFragment;
    private BlankFragment mBlankFragment;
    private GoogleApi mGoogleApi;
    private Map mMap;
    private boolean mapStyleisLight = true;
    private Marker longMarker;
    private ContentResolver resolver;
    private GoogleMap googleMap;
    private ArrayList<Marker> markers;

    private FloatingActionButton fabOvni;
    private FloatingActionButton fabFantasma;
    private FloatingActionButton fabHistorico;
    private FloatingActionButton fabSinResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resolver = getContentResolver();
        markers = new ArrayList<Marker>();

        fabOvni = (FloatingActionButton) findViewById(R.id.fabOvni);
        fabFantasma = (FloatingActionButton) findViewById(R.id.fabFantasma);
        fabHistorico = (FloatingActionButton) findViewById(R.id.fabHistorico);
        fabSinResolver = (FloatingActionButton) findViewById(R.id.fabSinResolver);

        fabOvni.setOnClickListener(this);
        fabFantasma.setOnClickListener(this);
        fabHistorico.setOnClickListener(this);
        fabSinResolver.setOnClickListener(this);

        UpdateUI();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFirstFirstMapFragment = new FirstMapFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mFirstFirstMapFragment)
                .commit();

        mGoogleApi = new GoogleApi(this, this, this);

        //mMap = new Map(this, mGoogleApi);

        //mMap.getMapAsync(mFirstFirstMapFragment);
        mFirstFirstMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (mapStyleisLight)
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.formato_mapa_light));
        else
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.formato_mapa_dark));

        if (mGoogleApi.getLastKnownLocation() != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mGoogleApi.getLastKnownLocation().getLatitude(), mGoogleApi.getLastKnownLocation().getLongitude()), 5));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 200, null);
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (longMarker != null) {
                    longMarker.remove();
                    longMarker = null;
                } else {
                    if (mapStyleisLight) {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.formato_mapa_light));
                    } else {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.formato_mapa_dark));
                    }
                    mapStyleisLight = !mapStyleisLight;
                }
                UpdateUI();

            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (longMarker != null)
                    longMarker.remove();
                longMarker = googleMap.addMarker(new MarkerOptions().title("Nueva posición").position(latLng).draggable(true));
                if (mBlankFragment != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
                    transaction.remove(mBlankFragment);
                    transaction.commit();
                    mBlankFragment=null;
                }
                UpdateUI();
            }
        });

        final LoadMarkers loadMarkers = new LoadMarkers();
        loadMarkers.execute();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (longMarker != null) {
                    longMarker.remove();
                    longMarker = null;
                }

                UpdateUI();
                Log.d("onMarkerClick", "Click en marker " + marker.getTitle());
                FragmentManager fm = getSupportFragmentManager();
                mBlankFragment = (BlankFragment) fm.findFragmentByTag("fragmentA");
                Bundle bundle = new Bundle();
                bundle.putString("name", marker.getTitle());
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera);
                if (mBlankFragment != null) {

                    mBlankFragment = BlankFragment.newInstance();
                    mBlankFragment.setArguments(bundle);
                    transaction.replace(R.id.content_main, mBlankFragment, "fragmentA");
                } else {

                    mBlankFragment = BlankFragment.newInstance();
                    mBlankFragment.setArguments(bundle);
                    transaction.add(R.id.content_main, mBlankFragment, "fragmentA");

                }
                transaction.commit();


                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (longMarker != null) {
                longMarker.remove();
                longMarker = null;
                UpdateUI();
            } else {
                if (mBlankFragment != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
                    transaction.remove(mBlankFragment);
                    transaction.commit();
                    mBlankFragment=null;
                } else
                    super.onBackPressed();
            }
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
            SyncHelper.syncNow(getApplicationContext(), true);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabOvni:
                startActivity(InsertActivity.newStartIntent(getApplicationContext(), 1,
                        getIntent().getStringExtra(DatuBaseKontratua.Usuarios.ID),
                        longMarker.getPosition().latitude, longMarker.getPosition().longitude));
                break;
            case R.id.fabFantasma:
                startActivity(InsertActivity.newStartIntent(getApplicationContext(), 2,
                        getIntent().getStringExtra(DatuBaseKontratua.Usuarios.ID),
                        longMarker.getPosition().latitude, longMarker.getPosition().longitude));
                break;
            case R.id.fabHistorico:
                startActivity(InsertActivity.newStartIntent(getApplicationContext(), 3,
                        getIntent().getStringExtra(DatuBaseKontratua.Usuarios.ID),
                        longMarker.getPosition().latitude, longMarker.getPosition().longitude));
                break;
            case R.id.fabSinResolver:
                startActivity(InsertActivity.newStartIntent(getApplicationContext(), 4,
                        getIntent().getStringExtra(DatuBaseKontratua.Usuarios.ID),
                        longMarker.getPosition().latitude, longMarker.getPosition().longitude));
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 60) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                Toast.makeText(this, "Permiso Denegado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {

        mGoogleApi.connect();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApi.disconnect();
    }

    private void UpdateUI() {
        if (longMarker != null) {
            fabOvni.show();
            fabFantasma.show();
            fabHistorico.show();
            fabSinResolver.show();
        } else {
            fabOvni.hide();
            fabFantasma.hide();
            fabHistorico.hide();
            fabSinResolver.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                            marker = addMarkerOvni(objetoMapa);
                            break;
                        case 2:
                            marker = addMarkerFantasma(objetoMapa);
                            break;
                        case 3:
                            marker = addMarkerHistorico(objetoMapa);
                            break;
                        case 4:
                            marker = addMarkerSinResolver(objetoMapa);
                            break;
                    }
                    marker.setTag(objetoMapa);
                    markers.add(marker);
                }
            }
        }

        public Marker addMarkerOvni(ObjetoMapa objeto) {
            return googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_ovni)).title(objeto.getNombre_objeto()).draggable(false));

        }

        public Marker addMarkerHistorico(ObjetoMapa objeto) {
            return googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_historico)).title(objeto.getNombre_objeto()).draggable(false));
        }

        public Marker addMarkerFantasma(ObjetoMapa objeto) {
            return googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_fantasma)).title(objeto.getNombre_objeto()).draggable(false));
        }

        public Marker addMarkerSinResolver(ObjetoMapa objeto) {
            return googleMap.addMarker(new MarkerOptions().position(new LatLng(objeto.getLatitud(), objeto.getLongitud())).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_sin_resolver)).title(objeto.getNombre_objeto()).draggable(false));
        }
    }
}
