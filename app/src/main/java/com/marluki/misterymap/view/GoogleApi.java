package com.marluki.misterymap.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Markel on 18/05/2017.
 */

public class GoogleApi implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected Context context;

    private GoogleApiClient mGoogleApiClient;
    private Location lastKnownLocation;
    private static final int PETICION_PERMISO_LOCALIZACION = 60;
    private FragmentActivity activity;
    private final String TAG = "GoogleLocationApi";


    public GoogleApi(Context context, FragmentActivity fragmentActivity, Activity activity)
    {
        //this.activity = activity;
        this.context = context.getApplicationContext();
        this.activity = fragmentActivity;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .enableAutoManage(fragmentActivity, this)
                .addApiIfAvailable(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else            {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if(lastLocation != null)
                updateLocation(lastLocation);
            else
                Log.d(TAG, "lastLocation está vacío");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.d(TAG, "Error grave al conectar con Google Play Services");
    }


    public void updateLocation(Location loc)
    {
        lastKnownLocation = loc;

        Log.d(TAG,"Latitud: " + lastKnownLocation.getLatitude() + "Longitud: " + lastKnownLocation.getLongitude());
    }


    public Location getLastKnownLocation(){
        return lastKnownLocation;
    }

    public GoogleApiClient getGoogleApiClient(){return mGoogleApiClient;}

    public void connect()
    {
        if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    public void disconnect()
    {
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
}
