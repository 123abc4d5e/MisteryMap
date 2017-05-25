package com.marluki.misterymap;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.sync.SyncHelper;
import com.marluki.misterymap.ui.ObjectFragment;
import com.marluki.misterymap.ui.FirstMapFragment;
import com.marluki.misterymap.ui.FragmentMapa;
import com.marluki.misterymap.ui.InsertActivity;
import com.marluki.misterymap.view.GoogleApi;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnClickListener,
        ObjectFragment.OnFragmentInteractionListener, FirstMapFragment.OnFragmentInteractionListener, FragmentMapa.OnMarkerClickListener,
        FragmentMapa.OnUpdateUIListener, FragmentMapa.OnMapLongClickListener {

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> suggestions;
    private SearchAdapter searchAdapter;
    private ContentResolver resolver;
    private ObjetoMapa objetoMapa;


    private FragmentMapa fragmentMapa;
    private ObjectFragment mBlankFragment;
    private Marker longMarker;
    private GoogleApi mGoogleApi;
    private ArrayList<ObjetoMapa> arrayObjeto;

    private FloatingActionButton fabOvni;
    private FloatingActionButton fabFantasma;
    private FloatingActionButton fabHistorico;
    private FloatingActionButton fabSinResolver;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resolver = getContentResolver();

        fabOvni = (FloatingActionButton) findViewById(R.id.fabOvni);
        fabFantasma = (FloatingActionButton) findViewById(R.id.fabFantasma);
        fabHistorico = (FloatingActionButton) findViewById(R.id.fabHistorico);
        fabSinResolver = (FloatingActionButton) findViewById(R.id.fabSinResolver);

        fabOvni.setOnClickListener(this);
        fabFantasma.setOnClickListener(this);
        fabHistorico.setOnClickListener(this);
        fabSinResolver.setOnClickListener(this);

        UpdateUI(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //mFirstFirstMapFragment = new FirstMapFragment();
        fragmentMapa = new FragmentMapa();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, fragmentMapa)
                .commitAllowingStateLoss();

        mGoogleApi = new GoogleApi(this, this, this);

        if (savedInstanceState!=null){
            getSupportFragmentManager().getFragment(savedInstanceState, "Fragment");
        }

        //mMap = new Map(this, mGoogleApi);

        //mMap.getMapAsync(mFirstFirstMapFragment);
        //mFirstFirstMapFragment.getMapAsync(this);

        Uri uri=DatuBaseKontratua.Objetos_mapa.URI_CONTENT;

        resolver=getContentResolver();

        Cursor c=resolver.query(uri,null,null,null,null);
        suggestions=new ArrayList<String>();
        arrayObjeto=new ArrayList<ObjetoMapa>();

        while (c.moveToNext()){
            objetoMapa = new ObjetoMapa();
            objetoMapa.setNombre_objeto(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)));
            objetoMapa.setId(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)));
            objetoMapa.setLatitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)));
            objetoMapa.setLongitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)));
            arrayObjeto.add(objetoMapa);
            suggestions.add(objetoMapa.getNombre_objeto());
        }

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
                UpdateUI(false);
            } else {
                if (mBlankFragment != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
                    transaction.remove(mBlankFragment);
                    transaction.commit();
                    mBlankFragment = null;
                } else
                    super.onBackPressed();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 60) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                Toast.makeText(getApplicationContext(), "Permiso Denegado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,suggestions);
        autoCompleteTextView=(AutoCompleteTextView)menu.findItem(R.id.action_search).getActionView().findViewById(R.id.search_box);
        searchAdapter = new SearchAdapter(getApplicationContext(),arrayObjeto);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
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

        if (id == R.id.nav_alien) {
            // Handle the camera action
        } else if (id == R.id.nav_fantasma) {

        } else if (id == R.id.nav_sin_resolver) {

        } else if (id == R.id.nav_historico) {

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

    private void UpdateUI(Boolean x) {
        if (x) {
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

    @Override
    public void onUpdateUI(Marker marker) {
        if (marker != null) {
            UpdateUI(true);
        } else {
            UpdateUI(false);
        }
    }

    @Override
    public void onMapLongClick(Marker marker) {
        longMarker = marker;
        if (mBlankFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera, R.anim.pop_exit, R.anim.pop_enter);
            transaction.remove(mBlankFragment);
            transaction.commit();
            mBlankFragment = null;
        }

    }

    @Override
    public void onMarkerClick(String id) {
        longMarker = null;
        FragmentManager fm = getSupportFragmentManager();
        mBlankFragment = (ObjectFragment) fm.findFragmentByTag("fragmentA");
        Bundle bundle = new Bundle();
        bundle.putString("name", id);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_gora, R.anim.slide_behera);
        if (mBlankFragment != null) {

            mBlankFragment = ObjectFragment.newInstance();
            mBlankFragment.setArguments(bundle);
            transaction.replace(R.id.content_main, mBlankFragment, "fragmentA");
        } else {

            mBlankFragment = ObjectFragment.newInstance();
            mBlankFragment.setArguments(bundle);
            transaction.add(R.id.content_main, mBlankFragment, "fragmentA");

        }
        transaction.commit();

    }
}
