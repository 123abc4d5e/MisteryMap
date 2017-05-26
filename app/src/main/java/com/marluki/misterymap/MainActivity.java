package com.marluki.misterymap;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.model.ObjetoMapa2;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.sync.SyncHelper;
import com.marluki.misterymap.ui.FirstMapFragment;
import com.marluki.misterymap.ui.FragmentList;
import com.marluki.misterymap.ui.FragmentMapa;
import com.marluki.misterymap.ui.InsertActivity;
import com.marluki.misterymap.ui.ObjectFragment;
import com.marluki.misterymap.view.GoogleApi;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnClickListener,
        ObjectFragment.OnFragmentInteractionListener, FirstMapFragment.OnFragmentInteractionListener, FragmentMapa.OnMarkerClickListener,
        FragmentMapa.OnUpdateUIListener, FragmentMapa.OnMapLongClickListener {

    private AutoCompleteTextView autoCompleteTextView;
    private ContentResolver resolver;
    private ObjetoMapa2 objetoMapa;


    private FragmentList fragmentLista;
    private FragmentMapa fragmentMapa;
    private ObjectFragment mBlankFragment;
    private Marker longMarker;
    private GoogleApi mGoogleApi;
    private ArrayList<ObjetoMapa2> arrayObjeto;
    private SimpleCursorAdapter mAdapter;
    private Cursor c;

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

        fragmentMapa = new FragmentMapa();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, fragmentMapa)
                .commitAllowingStateLoss();

        mGoogleApi = new GoogleApi(this, this, this);

        if (savedInstanceState!=null){
            getSupportFragmentManager().getFragment(savedInstanceState, "Fragment");
        }


        Uri uri=DatuBaseKontratua.Objetos_mapa.URI_CONTENT;

        resolver=getContentResolver();

        c=resolver.query(uri,null,null,null,null);
        arrayObjeto=new ArrayList<ObjetoMapa2>();

        while (c.moveToNext()){
            objetoMapa = new ObjetoMapa2();
            objetoMapa.set_id(c.getInt(c.getColumnIndex("_id")));
            objetoMapa.setNombre_objeto(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)));
            objetoMapa.setId(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)));
            objetoMapa.setLatitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)));
            objetoMapa.setLongitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)));
            arrayObjeto.add(objetoMapa);
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

                fragmentMapa.moveToLocation(lastLocation.getLatitude(), lastLocation.getLongitude());


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

        mAdapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.suggestion_layout,null, new String[]{DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO},new int[]{R.id.text1}, 0);

        autoCompleteTextView = (AutoCompleteTextView)menu.findItem(R.id.action_search).getActionView().findViewById(R.id.search_box);


        autoCompleteTextView.setAdapter(mAdapter);
        autoCompleteTextView.setThreshold(2);
        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint);
            }
        });
        mAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                int index = cursor.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO);
                return cursor.getString(index);
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Double lat = 0.0,lon = 0.0;
                String selection = "" + BaseColumns._ID + " =?";
                String[] selectionArgs = new String[]{"" +String.valueOf(id) + ""};
                Cursor cursor = resolver.query(DatuBaseKontratua.Objetos_mapa.URI_CONTENT, null, selection, selectionArgs,
                        null);
                while (cursor.moveToNext()) {
                    objetoMapa  = new ObjetoMapa2();
                    objetoMapa.setId(cursor.getString(cursor.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)));
                    objetoMapa.setLatitud(cursor.getDouble(cursor.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)));
                    objetoMapa.setLongitud(cursor.getDouble(cursor.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)));

                }
                fragmentMapa.moveToLocation(objetoMapa.getLatitud(), objetoMapa.getLongitud());
                hideKeyboard();
            }
        });
        return true;
    }
    private void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public Cursor getCursor(CharSequence str) {
        String select = "" + DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO + " LIKE ? ";
        String[] selectArgs = {"%" + str + "%"};

        Cursor c = resolver.query(DatuBaseKontratua.Objetos_mapa.URI_CONTENT,null,select,selectArgs,null);
        while (c.moveToNext()) {
            objetoMapa = new ObjetoMapa2();
            objetoMapa.set_id(c.getInt(c.getColumnIndex("_id")));
            objetoMapa.setNombre_objeto(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)));
            objetoMapa.setId(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)));
            objetoMapa.setLatitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)));
            objetoMapa.setLongitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)));
            arrayObjeto.add(objetoMapa);

        }
        return c;
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
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

            Bundle bundle=new Bundle();
            bundle.putInt("TipoID", 1);

            fragmentLista=new FragmentList();
            fragmentLista.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.map,fragmentLista);
            ft.addToBackStack(null);
            ft.commit();
        }

        else if (id == R.id.nav_fantasma) {

            Bundle bundle=new Bundle();
            bundle.putInt("TipoID", 2);

            fragmentLista=new FragmentList();
            fragmentLista.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.map,fragmentLista);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_sin_resolver) {

            Bundle bundle=new Bundle();
            bundle.putInt("TipoID", 3);

            fragmentLista=new FragmentList();
            fragmentLista.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.map,fragmentLista);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_historico) {

            Bundle bundle=new Bundle();
            bundle.putInt("TipoID", 4);

            fragmentLista=new FragmentList();
            fragmentLista.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.map,fragmentLista);
            ft.addToBackStack(null);
            ft.commit();

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
    public void onMarkerClick(Marker marker) {
        longMarker = null;
        FragmentManager fm = getSupportFragmentManager();
        mBlankFragment = (ObjectFragment) fm.findFragmentByTag("fragmentA");
        ObjetoMapa objetoMapa = (ObjetoMapa)marker.getTag();
        Bundle bundle = new Bundle();
        bundle.putString("id", objetoMapa.getId());
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
