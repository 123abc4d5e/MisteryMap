package com.marluki.misterymap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.model.Marker;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.sync.SyncHelper;
import com.marluki.misterymap.ui.BlankFragment;
import com.marluki.misterymap.ui.FirstMapFragment;
import com.marluki.misterymap.ui.FragmentMapa;
import com.marluki.misterymap.ui.InsertActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnClickListener,
        BlankFragment.OnFragmentInteractionListener, FirstMapFragment.OnFragmentInteractionListener, FragmentMapa.OnMarkerClickListener,
        FragmentMapa.OnUpdateUIListener, FragmentMapa.OnMapLongClickListener {

    private AutoCompleteTextView autoCompleteTextView;
    private SearchAdapter searchAdapter;
    private ContentResolver resolver;
    private ObjetoMapa objetoMapa;

    private FragmentMapa fragmentMapa;
    private BlankFragment mBlankFragment;
    private Marker longMarker;
    private ArrayList<ObjetoMapa> ArrayObjeto;

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

        //mGoogleApi = new GoogleApi(this, this, this);

        //mMap = new Map(this, mGoogleApi);

        //mMap.getMapAsync(mFirstFirstMapFragment);
        //mFirstFirstMapFragment.getMapAsync(this);

        Uri uri=DatuBaseKontratua.Objetos_mapa.URI_CONTENT;

        Cursor c=resolver.query(uri,null,null,null,null);

        ArrayObjeto=new ArrayList<ObjetoMapa>();

        while (c.moveToNext()){
            objetoMapa =new ObjetoMapa();
            objetoMapa.setNombre_objeto(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)));
            objetoMapa.setId(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)));
            objetoMapa.setLatitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)));
            objetoMapa.setLongitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)));
            ArrayObjeto.add(objetoMapa);
        }

        searchAdapter=new SearchAdapter(getApplicationContext(),ArrayObjeto);
        autoCompleteTextView=(AutoCompleteTextView)toolbar.findViewById(R.id.search_box);
        autoCompleteTextView.setAdapter(searchAdapter);



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
        mBlankFragment = (BlankFragment) fm.findFragmentByTag("fragmentA");
        Bundle bundle = new Bundle();
        bundle.putString("name", id);
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

    }
}
