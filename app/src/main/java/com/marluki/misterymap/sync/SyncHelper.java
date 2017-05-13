package com.marluki.misterymap.sync;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;

import com.marluki.misterymap.R;
import com.marluki.misterymap.provider.DatuBaseKontratua;

/**
 * Created by charl on 12/05/2017.
 */

public class SyncHelper {

    private static final String TAG = "SyncHelper";

    private static final int SYNC_INTERVAL = (int) (DateUtils.DAY_IN_MILLIS / 1000); // Once per day
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final int SYNCABLE_TRUE = 1;
    private static final int SYNCABLE_FALSE = 0;

    public SyncHelper() {
    }

    /**
     * Edozein klasetik sinkronizazioa hasteko metodo laguntzailea
     *
     * @param context
     * @return
     */
    public static boolean initializeSync(Context context) {
        Account syncAccount = getSyncAccount(context);
        if (syncAccount != null) {
            onAccountCreated(context,syncAccount);
            Log.v(TAG, "Syncing initialized");
            return true;
        }
        Log.v(TAG, "Syncing failed, no account permissions.");
        return false;
    }


    /**
     * Dummy account bat sortzen dogu sync adapterra erabiltzeko
     * @param context
     * @return google-eko komntua bueltatzen du
     */
    private static Account getSyncAccount(Context context) {
       if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
           Log.e(TAG, "Ez dago kontuak eskuratzeko baimena!!");
           return null;
       }
       Account[] accounts = AccountManager.get(context)
               .getAccountsByType(context.getString(R.string.sync_account_type));
       if (accounts.length==0) {
           Log.d(TAG, "Ez daude google-eko konturik!   MOLTO RARUNO->pa eso pillate un ipone!!");
           return null;
       }
       return accounts[0];
    }

    private static void onAccountCreated(Context context, Account newAccount) {
        /*
         * Sistemari informatzen dio kontu honek sinkronizazioa honartzen dula
         */
        ContentResolver.setIsSyncable(newAccount, DatuBaseKontratua.AUTHORITY, SYNCABLE_TRUE);
        /*
         * Sinkorizazio periodikoa esartzen
         */
        ContentResolver.setSyncAutomatically(newAccount, DatuBaseKontratua.AUTHORITY, true);
        configurePeriodicSync(context, newAccount);

        /*
         * Sinkronizazioa hasi
         */
        syncNow(context);
    }

    /**
     * Sinkronizazio periodikoa konfiguratzeko metodo laguntzailea
     *
     * @param context
     * @param account
     */
    private static void configurePeriodicSync(Context context, Account account) {
        final String authority = DatuBaseKontratua.AUTHORITY;
        // Ahal dugu aktibatu sinkronizazio perikoaren zehaztu gabeko denbora
        SyncRequest request = new SyncRequest.Builder()
                .setExtras(Bundle.EMPTY)
                .syncPeriodic(SYNC_INTERVAL, SYNC_FLEXTIME)
                .setSyncAdapter(account, authority)
                .build();

        ContentResolver.requestSync(request);

        Log.v(TAG, "Sinkronizazio periodikoa konfiguratua " + SYNC_INTERVAL + " denbora eta " + SYNC_FLEXTIME + "denbora tartean.");
    }

    public static void cancelSyncService(Context context) {
        ContentResolver.setIsSyncable(getSyncAccount(context), DatuBaseKontratua.AUTHORITY, SYNCABLE_FALSE);
    }

    /**
     * Sync adapter-a momentuan eguneratzeko metodo laguntzailea
     *
     * @param context
     */
    public static void syncNow(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getSyncAccount(context), DatuBaseKontratua.AUTHORITY, bundle);
    }

    /**
     * Sync adapter-a momentuan eguneratzeko metodo laguntzailea
     *
     * @param context
     */
    public static void syncNow(Context context, boolean onlyUpload) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(getSyncAccount(context), DatuBaseKontratua.AUTHORITY, bundle);
    }

    /**
     * Sync adapter-a momentuan eguneratzeko metodo laguntzailea
     *
     * @param context
     * @param extras
     */
    public static void syncNow(Context context, Bundle extras) {
        Bundle bundle = new Bundle(extras);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getSyncAccount(context), DatuBaseKontratua.AUTHORITY, bundle);
    }

    public static boolean isInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
