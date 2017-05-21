package com.marluki.misterymap.google;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by charl on 16/05/2017.
 */

public class App extends Application {
    private GoogleApiClient mGoogleApiClient;
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized App getmInstance() {
        return mInstance;
    }


    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }
}
