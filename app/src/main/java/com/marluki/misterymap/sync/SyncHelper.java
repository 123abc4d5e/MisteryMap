package com.marluki.misterymap.sync;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.Usuario;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.ui.SignInActivity;
import com.marluki.misterymap.utils.Cons;
import com.marluki.misterymap.utils.Utils;
import com.marluki.misterymap.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

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
    public static boolean initializeSync(Context context, String token) {
        Account syncAccount = getSyncAccount(context);
        if (syncAccount != null) {
            onAccountCreated(context,syncAccount, token);
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
           Log.d(TAG, "Ez daude google-eko konturik!");
           return null;
       }
       return accounts[0];
    }

    private static void onAccountCreated(Context context, Account newAccount, String token) {
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
        if(TextUtils.isEmpty(token))
        syncNow(context);
        else {
            Bundle extras = new Bundle();
            extras.putString("token", token);
            syncNow(context, extras);
        }
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

    public static void insertUser(final Context context,final Usuario usuario, final String token) {
        JSONObject object = new JSONObject();
        JSONObject postData = new JSONObject();
        try {
            object.put(DatuBaseKontratua.Usuarios.ID, usuario.getId());
            object.put(DatuBaseKontratua.Usuarios.NOMBRE, usuario.getNombre());
            object.put(DatuBaseKontratua.Usuarios.FOTO, usuario.getFoto());

            postData.put("token", token);
            postData.put("datos", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Cons.INSERT_USER_URL,
                        postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String estado = "";
                                String mensaje = "";
                                try {
                                    estado = response.getString(Cons.ESTADO);
                                    mensaje = response.getString(Cons.MENSAJE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                switch (estado) {
                                    case Cons.SUCCESS:
                                        Log.i(TAG, mensaje);
                                        break;
                                    case Cons.FAILED:
                                        Log.i(TAG, mensaje);
                                        break;
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.getMessage());
                                insertUser(context, usuario, token);
                            }
                        }
                )
        );
    }

    public static void deleteUser(final Context context, final String id, final String token) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = DatuBaseKontratua.Usuarios.crearUriUsuario(id);
        Cursor c = resolver.query(uri, null, null, null, null);

        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Cons.DELETE_USER_URL,
                        Utils.deCursorAJSONObject(c, uri, token),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String estado = "";
                                String mensaje = "";
                                try {
                                    estado = response.getString(Cons.ESTADO);
                                    mensaje = response.getString(Cons.MENSAJE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                switch (estado) {
                                    case Cons.SUCCESS:
                                        Log.i(TAG, mensaje);
                                        context.startActivity(new Intent(context, SignInActivity.class));
                                        break;
                                    case Cons.FAILED:
                                        Log.i(TAG, mensaje);
                                        break;
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                deleteUser(context, id, token);
                            }
                        }
                )
        );
    }

}
