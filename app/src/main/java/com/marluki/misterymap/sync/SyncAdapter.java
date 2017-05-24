package com.marluki.misterymap.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.gson.Gson;
import com.marluki.misterymap.google.App;
import com.marluki.misterymap.model.Comentario;
import com.marluki.misterymap.model.Fantasma;
import com.marluki.misterymap.model.Foto;
import com.marluki.misterymap.model.Historico;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.model.Ovni;
import com.marluki.misterymap.model.Psicofonia;
import com.marluki.misterymap.model.SinResolver;
import com.marluki.misterymap.model.Tipo;
import com.marluki.misterymap.model.Usuario;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.provider.MisteryProvider;
import com.marluki.misterymap.ui.SignInActivity;
import com.marluki.misterymap.utils.Cons;
import com.marluki.misterymap.utils.Utils;
import com.marluki.misterymap.view.SignInResolutionActivity;
import com.marluki.misterymap.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by charl on 12/05/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "SyncAdapter";

    private static boolean x = false;
    private static boolean isUser = false;
    private static String token;
    private static GoogleSignInAccount account;

    private ContentResolver resolver;
    private GoogleApiClient mGoogleApiClient;
    private Gson gson = new Gson();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
        mGoogleApiClient = App.getmInstance().getmGoogleApiClient();
    }


    private void initGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            /**
             * If the user's cached credentials are valid, the OptionalPendingResult will be "done"
             * and the GoogleSignInResult will be available instantly.
             */
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            Log.d(TAG, "Got cached sign-in");
        } else {
            /**
             * If the user has not previously signed in on this device or the sign-in has expired,
             * this asynchronous branch will attempt to sign in the user silently.  Cross-device
             * single sign-on will occur in this branch.
             */
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully.
            account = result.getSignInAccount();
            if (account != null) {
                token = account.getIdToken();
            }
        }
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync()");
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = App.getmInstance().getmGoogleApiClient();
            initGoogleApiClient(mGoogleApiClient);
            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
        }
        if (!mGoogleApiClient.isConnected()) {
            return;
        }

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
        boolean createUser = extras.getBoolean(Cons.createUser, false);
        boolean deleteUsern = extras.getBoolean(Cons.deleteUser, false);

        if (createUser)
            if (this.account != null)
                insertUser(getContext(), this.account);
        if (deleteUsern)
            if (this.account != null)
                deleteUser(getContext(), this.account.getId(), this.account.getIdToken());

        if (SyncHelper.isInternetConnection(getContext())) {
            Log.d(TAG, "Existe conexion a internet! Empezando Sincronización.");
            if (!soloSubida) {
                // sincronizacion local
                doSyncLocal(syncResult);
            } else {
                updateRemoteData(token);
            }
        } else {
            Log.d(TAG, "No hay conexion a internet!");
        }

    }

    private void doSyncLocal(final SyncResult syncResult) {
        String[] urls = Cons.GET_URLS;
        for (int i = 0; i < urls.length; i++) {
            Log.i(TAG, "Iniciando peticion GET a " + urls[i]);

            VolleySingleton.getInstance(getContext()).addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.GET,
                            urls[i],
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    procesarGetResponse(response, syncResult);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //if(error.getMessage()!=null)
                                        //Log.d(TAG, error.getMessage().toString());
                                }
                            }
                    )
            );
        }
    }

    private void procesarGetResponse(JSONObject response, SyncResult syncResult) {
        try {
            String estado = response.getString(Cons.ESTADO);

            switch (estado) {
                case Cons.SUCCESS: // EXITO
                    updateLocalData(response, syncResult);
                    break;
                case Cons.FAILED: // FALLIDO
                    String mensaje = response.getString(Cons.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateLocalData(JSONObject response, SyncResult syncResult) {
        String tabla = null;
        JSONArray objetos = null;
        Uri uri = null;
        String id;

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try {
            tabla = response.getString(Cons.TABLA);
            objetos = response.getJSONArray(Cons.DATOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (tabla) {
            case Cons.TABLA_OBJETO_MAPA:
                // Gson erabiliz parseatzen
                ObjetoMapa[] res = gson.fromJson(objetos != null ? objetos.toString() : null, ObjetoMapa[].class);
                List<ObjetoMapa> data = Arrays.asList(res);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, ObjetoMapa> expenseMap = new HashMap<String, ObjetoMapa>();
                for (ObjetoMapa o : data)
                    expenseMap.put(o.getId(), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Objetos_mapa.URI_CONTENT;
                Cursor c = resolver.query(uri, null, null, null, null);
                assert c != null;

                Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales!");

                while (c.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID));
                    ObjetoMapa objetoMapa = new ObjetoMapa(id, c.getInt(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID)),
                            c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)),
                            c.getFloat(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.USUARIO_ID)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.DETALLES)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.PAIS)),
                            c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.CIUDAD)));

                    ObjetoMapa match = expenseMap.get(id);

                    if (match != null) {
                        expenseMap.remove(id);
                        Uri existingUri = DatuBaseKontratua.Objetos_mapa.crearUriObjetoMapa(id);

                        if (!objetoMapa.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Objetos_mapa.TIPO_ID, match.getTipo_id())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.LATITUD, match.getLatitud())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.LONGITUD, match.getLongitud())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, match.getUsuario_id())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO, match.getNombre_objeto())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.DETALLES, match.getDetalles())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.PAIS, match.getPais())
                                    .withValue(DatuBaseKontratua.Objetos_mapa.CIUDAD, match.getCiudad())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Objetos_mapa.crearUriObjetoMapa(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (ObjetoMapa obj : expenseMap.values()) {
                    Log.i(TAG, "Programando insercion de: " + obj.getId());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Objetos_mapa.ID, obj.getId())
                            .withValue(DatuBaseKontratua.Objetos_mapa.TIPO_ID, obj.getTipo_id())
                            .withValue(DatuBaseKontratua.Objetos_mapa.LATITUD, obj.getLatitud())
                            .withValue(DatuBaseKontratua.Objetos_mapa.LONGITUD, obj.getLongitud())
                            .withValue(DatuBaseKontratua.Objetos_mapa.USUARIO_ID, obj.getUsuario_id())
                            .withValue(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO, obj.getNombre_objeto())
                            .withValue(DatuBaseKontratua.Objetos_mapa.DETALLES, obj.getDetalles())
                            .withValue(DatuBaseKontratua.Objetos_mapa.PAIS, obj.getPais())
                            .withValue(DatuBaseKontratua.Objetos_mapa.CIUDAD, obj.getCiudad())
                            .withValue(DatuBaseKontratua.Psicofonias.PENDIENTE_INSERCION, 0)
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_OVNI:
                // Gson erabiliz parseatzen
                Ovni[] resOvni = gson.fromJson(objetos != null ? objetos.toString() : null, Ovni[].class);
                List<Ovni> dataOvni = Arrays.asList(resOvni);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Ovni> expenseMapOvni = new HashMap<String, Ovni>();
                for (Ovni o : dataOvni)
                    expenseMapOvni.put(o.getObjeto_id(), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Ovnis.URI_CONTENT;
                Cursor c1 = resolver.query(uri, null, null, null, null);
                assert c1 != null;

                Log.i(TAG, "Se encontraron " + c1.getCount() + " registros locales!");

                while (c1.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c1.getString(c1.getColumnIndex(DatuBaseKontratua.Ovnis.OBJETO_ID));
                    Ovni ovni = new Ovni(id, c1.getString(c1.getColumnIndex(DatuBaseKontratua.Ovnis.FECHA)));

                    Ovni match = expenseMapOvni.get(id);

                    if (match != null) {
                        expenseMapOvni.remove(id);
                        Uri existingUri = DatuBaseKontratua.Ovnis.crearUriOvni(id);

                        if (!ovni.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Ovnis.FECHA, match.getFecha())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Ovnis.crearUriOvni(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c1.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Ovni obj : expenseMapOvni.values()) {
                    Log.i(TAG, "Programando insercion de: " + obj.getObjeto_id());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Ovnis.OBJETO_ID, obj.getObjeto_id())
                            .withValue(DatuBaseKontratua.Ovnis.FECHA, obj.getFecha())
                            .build());
                    syncResult.stats.numInserts++;
                }
                break;
            case Cons.TABLA_FANTASMA:
                // Gson erabiliz parseatzen
                Fantasma[] resFant = gson.fromJson(objetos != null ? objetos.toString() : null, Fantasma[].class);
                List<Fantasma> dataFant = Arrays.asList(resFant);

                // Hash taula, jaso diren datuak gordetzeko
                HashMap<String, Fantasma> expenseMapFant = new HashMap<String, Fantasma>();
                for (Fantasma f : dataFant)
                    expenseMapFant.put(f.getObjeto_id(), f);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Fantasmas.URI_CONTENT;
                Cursor c2 = resolver.query(uri, null, null, null, null);
                assert c2 != null;

                Log.i(TAG, "Se encontraron " + c2.getCount() + " registros locales!");

                while (c2.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c2.getString(c2.getColumnIndex(DatuBaseKontratua.Fantasmas.OBJETO_ID));
                    Fantasma fantasma = new Fantasma(id, c2.getInt(c2.getColumnIndex(DatuBaseKontratua.Fantasmas.VISTO)),
                            c2.getInt(c2.getColumnIndex(DatuBaseKontratua.Fantasmas.FAKE)));

                    Fantasma match = expenseMapFant.get(id);

                    if (match != null) {
                        expenseMapFant.remove(id);
                        Uri existingUri = DatuBaseKontratua.Fantasmas.crearUriFantasma(id);

                        if (!fantasma.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Fantasmas.VISTO, match.getVisto())
                                    .withValue(DatuBaseKontratua.Fantasmas.FAKE, match.getFake())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Fantasmas.crearUriFantasma(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c2.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Fantasma fantasma : expenseMapFant.values()) {
                    Log.i(TAG, "Programando insercion de: " + fantasma.getObjeto_id());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Fantasmas.OBJETO_ID, fantasma.getObjeto_id())
                            .withValue(DatuBaseKontratua.Fantasmas.VISTO, fantasma.getVisto())
                            .withValue(DatuBaseKontratua.Fantasmas.FAKE, fantasma.getFake())
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_HISTORICO:
                // Gson erabiliz parseatzen
                Historico[] resHis = gson.fromJson(objetos != null ? objetos.toString() : null, Historico[].class);
                List<Historico> dataHist = Arrays.asList(resHis);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Historico> expenseMapHist = new HashMap<String, Historico>();
                for (Historico h : dataHist)
                    expenseMapHist.put(h.getObjeto_id(), h);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Historicos.URI_CONTENT;
                Cursor c3 = resolver.query(uri, null, null, null, null);
                assert c3 != null;

                Log.i(TAG, "Se encontraron " + c3.getCount() + " registros locales!");

                while (c3.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c3.getString(c3.getColumnIndex(DatuBaseKontratua.Historicos.OBJETO_ID));
                    Historico historico = new Historico(id, c3.getString(c3.getColumnIndex(DatuBaseKontratua.Historicos.FECHA)));

                    Historico match = expenseMapHist.get(id);

                    if (match != null) {
                        expenseMapHist.remove(id);
                        Uri existingUri = DatuBaseKontratua.Historicos.crearUriHistorico(id);

                        if (!historico.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Historicos.FECHA, match.getFecha())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Historicos.crearUriHistorico(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c3.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Historico historico : expenseMapHist.values()) {
                    Log.i(TAG, "Programando insercion de: " + historico.getObjeto_id());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Historicos.OBJETO_ID, historico.getObjeto_id())
                            .withValue(DatuBaseKontratua.Historicos.FECHA, historico.getFecha())
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_SIN_RESOLVER:
                // Gson erabiliz parseatzen
                SinResolver[] resSin = gson.fromJson(objetos != null ? objetos.toString() : null, SinResolver[].class);
                List<SinResolver> dataSin = Arrays.asList(resSin);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, SinResolver> expenseMapSin = new HashMap<String, SinResolver>();
                for (SinResolver s : dataSin)
                    expenseMapSin.put(s.getObjeto_id(), s);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.SinResolver.URI_CONTENT;
                Cursor c4 = resolver.query(uri, null, null, null, null);
                assert c4 != null;

                Log.i(TAG, "Se encontraron " + c4.getCount() + " registros locales!");

                while (c4.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c4.getString(c4.getColumnIndex(DatuBaseKontratua.SinResolver.OBJETO_ID));
                    SinResolver sinResolver = new SinResolver(id, c4.getString(c4.getColumnIndex(DatuBaseKontratua.SinResolver.FECHA)));

                    SinResolver match = expenseMapSin.get(id);

                    if (match != null) {
                        expenseMapSin.remove(id);
                        Uri existingUri = DatuBaseKontratua.SinResolver.crearUriSinResolver(id);

                        if (!sinResolver.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.SinResolver.FECHA, match.getFecha())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.SinResolver.crearUriSinResolver(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c4.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (SinResolver sinResolver : expenseMapSin.values()) {
                    Log.i(TAG, "Programando insercion de: " + sinResolver.getObjeto_id());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.SinResolver.OBJETO_ID, sinResolver.getObjeto_id())
                            .withValue(DatuBaseKontratua.SinResolver.FECHA, sinResolver.getFecha())
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_COMENTARIO:
                // Gson erabiliz parseatzen
                Comentario[] resCom = gson.fromJson(objetos != null ? objetos.toString() : null, Comentario[].class);
                List<Comentario> dataCom = Arrays.asList(resCom);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Comentario> expenseMapCom = new HashMap<String, Comentario>();
                for (Comentario o : dataCom)
                    expenseMapCom.put(o.getId(), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Comentarios.URI_CONTENT;
                Cursor c5 = resolver.query(uri, null, null, null, null);
                assert c5 != null;

                Log.i(TAG, "Se encontraron " + c5.getCount() + " registros locales!");

                while (c5.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c5.getString(c5.getColumnIndex(DatuBaseKontratua.Comentarios.ID));
                    Comentario comentario = new Comentario(id, c5.getString(c5.getColumnIndex(DatuBaseKontratua.Comentarios.OBJETO_ID)),
                            c5.getString(c5.getColumnIndex(DatuBaseKontratua.Comentarios.USUARIO_ID)),
                            c5.getString(c5.getColumnIndex(DatuBaseKontratua.Comentarios.COMENTARIO_ID)),
                            c5.getString(c5.getColumnIndex(DatuBaseKontratua.Comentarios.TEXTO)),
                            c5.getString(c5.getColumnIndex(DatuBaseKontratua.Comentarios.FECHA)));

                    Comentario match = expenseMapCom.get(id);

                    if (match != null) {
                        expenseMapCom.remove(id);
                        Uri existingUri = DatuBaseKontratua.Comentarios.crearUriComentario(id);

                        if (!comentario.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Comentarios.OBJETO_ID, match.getObjeto_id())
                                    .withValue(DatuBaseKontratua.Comentarios.USUARIO_ID, match.getUsuario_id())
                                    .withValue(DatuBaseKontratua.Comentarios.COMENTARIO_ID, match.getComentario_id())
                                    .withValue(DatuBaseKontratua.Comentarios.TEXTO, match.getTexto())
                                    .withValue(DatuBaseKontratua.Comentarios.FECHA, match.getFecha())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Comentarios.crearUriComentario(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c5.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Comentario comentario : expenseMapCom.values()) {
                    Log.i(TAG, "Programando insercion de: " + comentario.getId());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Comentarios.ID, comentario.getId())
                            .withValue(DatuBaseKontratua.Comentarios.OBJETO_ID, comentario.getObjeto_id())
                            .withValue(DatuBaseKontratua.Comentarios.USUARIO_ID, comentario.getUsuario_id())
                            .withValue(DatuBaseKontratua.Comentarios.COMENTARIO_ID, comentario.getComentario_id())
                            .withValue(DatuBaseKontratua.Comentarios.TEXTO, comentario.getTexto())
                            .withValue(DatuBaseKontratua.Comentarios.FECHA, comentario.getFecha())
                            .withValue(DatuBaseKontratua.Comentarios.PENDIENTE_INSERCION, 0)
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_FOTO:
                // Gson erabiliz parseatzen
                Foto[] resFo = gson.fromJson(objetos != null ? objetos.toString() : null, Foto[].class);
                List<Foto> dataFo = Arrays.asList(resFo);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Foto> expenseMapFo = new HashMap<String, Foto>();
                for (Foto o : dataFo)
                    expenseMapFo.put(o.getId(), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Fotos.URI_CONTENT;
                Cursor c6 = resolver.query(uri, null, null, null, null);
                assert c6 != null;

                Log.i(TAG, "Se encontraron " + c6.getCount() + " registros locales!");

                while (c6.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c6.getString(c6.getColumnIndex(DatuBaseKontratua.Fotos.ID));
                    Foto foto = new Foto(id, c6.getString(c6.getColumnIndex(DatuBaseKontratua.Fotos.NOMBRE_FOTO)),
                            c6.getString(c6.getColumnIndex(DatuBaseKontratua.Fotos.URL)),
                            c6.getString(c6.getColumnIndex(DatuBaseKontratua.Fotos.OBJETO_ID)),
                            c6.getString(c6.getColumnIndex(DatuBaseKontratua.Fotos.USUARIO_ID)));

                    Foto match = expenseMapFo.get(id);

                    if (match != null) {
                        expenseMapFo.remove(id);
                        Uri existingUri = DatuBaseKontratua.Fotos.crearUriFoto(id);

                        if (!foto.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Fotos.NOMBRE_FOTO, match.getNombre_foto())
                                    .withValue(DatuBaseKontratua.Fotos.URL, match.getUrl())
                                    .withValue(DatuBaseKontratua.Fotos.OBJETO_ID, match.getObjeto_id())
                                    .withValue(DatuBaseKontratua.Fotos.USUARIO_ID, match.getUsuario_id())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Fotos.crearUriFoto(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c6.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Foto foto : expenseMapFo.values()) {
                    Log.i(TAG, "Programando insercion de: " + foto.getId());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Fotos.ID, foto.getId())
                            .withValue(DatuBaseKontratua.Fotos.NOMBRE_FOTO, foto.getNombre_foto())
                            .withValue(DatuBaseKontratua.Fotos.URL, foto.getUrl())
                            .withValue(DatuBaseKontratua.Fotos.OBJETO_ID, foto.getObjeto_id())
                            .withValue(DatuBaseKontratua.Fotos.USUARIO_ID, foto.getUsuario_id())
                            .withValue(DatuBaseKontratua.Fotos.PENDIENTE_INSERCION, 0)
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_PSICOFONIA:
                // Gson erabiliz parseatzen
                Psicofonia[] resPs = gson.fromJson(objetos != null ? objetos.toString() : null, Psicofonia[].class);
                List<Psicofonia> dataPs = Arrays.asList(resPs);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Psicofonia> expenseMapPs = new HashMap<String, Psicofonia>();
                for (Psicofonia o : dataPs)
                    expenseMapPs.put(o.getId(), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Psicofonias.URI_CONTENT;
                Cursor c7 = resolver.query(uri, null, null, null, null);
                assert c7 != null;

                Log.i(TAG, "Se encontraron " + c7.getCount() + " registros locales!");

                while (c7.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c7.getString(c7.getColumnIndex(DatuBaseKontratua.Psicofonias.ID));
                    Psicofonia psicofonia = new Psicofonia(id, c7.getString(c7.getColumnIndex(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA)),
                            c7.getString(c7.getColumnIndex(DatuBaseKontratua.Psicofonias.URL)),
                            c7.getString(c7.getColumnIndex(DatuBaseKontratua.Psicofonias.OBJETO_ID)),
                            c7.getString(c7.getColumnIndex(DatuBaseKontratua.Psicofonias.USUARIO_ID)));

                    Psicofonia match = expenseMapPs.get(id);

                    if (match != null) {
                        expenseMapPs.remove(id);
                        Uri existingUri = DatuBaseKontratua.Psicofonias.crearUriPsicofonia(id);

                        if (!psicofonia.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA, match.getNombre_psicofonia())
                                    .withValue(DatuBaseKontratua.Psicofonias.URL, match.getUrl())
                                    .withValue(DatuBaseKontratua.Psicofonias.OBJETO_ID, match.getObjeto_id())
                                    .withValue(DatuBaseKontratua.Psicofonias.USUARIO_ID, match.getUsuario_id())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Psicofonias.crearUriPsicofonia(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c7.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Psicofonia psicofonia : expenseMapPs.values()) {
                    Log.i(TAG, "Programando insercion de: " + psicofonia.getId());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Psicofonias.ID, psicofonia.getId())
                            .withValue(DatuBaseKontratua.Psicofonias.NOMBRE_PSICOFONIA, psicofonia.getNombre_psicofonia())
                            .withValue(DatuBaseKontratua.Psicofonias.URL, psicofonia.getUrl())
                            .withValue(DatuBaseKontratua.Psicofonias.OBJETO_ID, psicofonia.getObjeto_id())
                            .withValue(DatuBaseKontratua.Psicofonias.USUARIO_ID, psicofonia.getUsuario_id())
                            .withValue(DatuBaseKontratua.Psicofonias.PENDIENTE_INSERCION, 0)
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_TIPO:
                // Gson erabiliz parseatzen
                Tipo[] resTi = gson.fromJson(objetos != null ? objetos.toString() : null, Tipo[].class);
                List<Tipo> dataTi = Arrays.asList(resTi);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Tipo> expenseMapTi = new HashMap<String, Tipo>();
                for (Tipo o : dataTi)
                    expenseMapTi.put(String.valueOf(o.getId()), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Tipos.URI_CONTENT;
                Cursor c8 = resolver.query(uri, null, null, null, null);
                assert c8 != null;

                Log.i(TAG, "Se encontraron " + c8.getCount() + " registros locales!");

                while (c8.moveToNext()) {
                    syncResult.stats.numEntries++;

                    int tipo_id = c8.getInt(c8.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID));
                    Tipo tipo = new Tipo(tipo_id, c8.getString(c8.getColumnIndex(DatuBaseKontratua.Tipos.NOMBRE_TIPO)));

                    Tipo match = expenseMapTi.get(String.valueOf(tipo_id));

                    if (match != null) {
                        expenseMapTi.remove(String.valueOf(tipo_id));
                        Uri existingUri = DatuBaseKontratua.Tipos.crearUriTipo(String.valueOf(tipo_id));

                        if (!tipo.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Tipos.NOMBRE_TIPO, match.getNombre_tipo())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Tipos.crearUriTipo(String.valueOf(tipo_id));
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c8.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Tipo tipo : expenseMapTi.values()) {
                    Log.i(TAG, "Programando insercion de: " + tipo.getId());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Tipos.ID, tipo.getId())
                            .withValue(DatuBaseKontratua.Tipos.NOMBRE_TIPO, tipo.getNombre_tipo())
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            case Cons.TABLA_USUARIO:
                // Gson erabiliz parseatzen
                Usuario[] resUs = gson.fromJson(objetos != null ? objetos.toString() : null, Usuario[].class);
                List<Usuario> dataUs = Arrays.asList(resUs);

                // Hash taula jaso diren datuak gordetzeko
                HashMap<String, Usuario> expenseMapUs = new HashMap<String, Usuario>();
                for (Usuario o : dataUs)
                    expenseMapUs.put(o.getId(), o);

                // SQLite databasean dauden datuekin konparatzen
                uri = DatuBaseKontratua.Usuarios.URI_CONTENT;
                Cursor c9 = resolver.query(uri, null, null, null, null);
                assert c9 != null;

                Log.i(TAG, "Se encontraron " + c9.getCount() + " registros locales!");

                while (c9.moveToNext()) {
                    syncResult.stats.numEntries++;

                    id = c9.getString(c9.getColumnIndex(DatuBaseKontratua.Usuarios.ID));
                    Usuario usuario = new Usuario(id, c9.getString(c9.getColumnIndex(DatuBaseKontratua.Usuarios.NOMBRE)),
                            c9.getString(c9.getColumnIndex(DatuBaseKontratua.Usuarios.FOTO)));

                    Usuario match = expenseMapUs.get(id);

                    if (match != null) {
                        expenseMapUs.remove(id);
                        Uri existingUri = DatuBaseKontratua.Usuarios.crearUriUsuario(id);

                        if (!usuario.equals(match)) {
                            Log.i(TAG, "Programando actualizacion de " + existingUri);

                            ops.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(DatuBaseKontratua.Usuarios.NOMBRE, match.getNombre())
                                    .withValue(DatuBaseKontratua.Usuarios.FOTO, match.getFoto())
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                        }
                    } else {
                        // Ez denez existitzen datu basetik(sqlite) ezabatzen da
                        Uri deleteUri = DatuBaseKontratua.Usuarios.crearUriUsuario(id);
                        Log.i(TAG, "Programando eliminacion de: " + deleteUri);
                        ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
                c9.close();
                // Geratzen diren datuak sartzen dira datu basera
                for (Usuario usuario : expenseMapUs.values()) {
                    Log.i(TAG, "Programando insercion de: " + usuario.getId());
                    ops.add(ContentProviderOperation.newInsert(uri)
                            .withValue(DatuBaseKontratua.Usuarios.ID, usuario.getId())
                            .withValue(DatuBaseKontratua.Usuarios.NOMBRE, usuario.getNombre())
                            .withValue(DatuBaseKontratua.Usuarios.FOTO, usuario.getFoto())
                            .build());
                    syncResult.stats.numInserts++;
                }

                break;
            default:
                break;
        }
        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(DatuBaseKontratua.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException | SQLiteConstraintException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    uri,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
    }

    private void updateRemoteData(final String token) {
        if (token == null) {
            Log.i(TAG, "El token es null!!");
            return;
        }

        Log.i(TAG, "Actualizando el servidor...");
        startUpdate();
        insertRequest(getInsertData(), token);
        updateRequest(getUpdateData(), token);
        deleteRequest(getDeleteData(), token);


    }

    /**
     * @param cursors
     * @param token
     */
    private void insertRequest(Cursor[] cursors, final String token) {
        for (int i = 0; i < cursors.length; i++) {
            Cursor c = cursors[i];
            Uri uri = Cons.INSERT_URIS[i];
            String url = Cons.INSERT_URLS[i];
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Log.i(TAG, "Peticion POST a " + url);
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    Utils.deCursorAJSONObject(c, uri, token),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            procesarInsertResponse(response, token);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if(error!=null)
                                                Log.d(TAG, "Error Voley: " + error.getMessage().toString());
                                        }
                                    }
                            ) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8" + getParamsEncoding();
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    headers.put("Accept", "application/json");
                                    return headers;
                                }
                            }
                    );
                }
            } else {
                Log.i(TAG, "No se requiere sincronización!!");
            }
            c.close();
        }
    }

    /**
     * @param cursors
     * @param token
     */
    private void updateRequest(Cursor[] cursors, final String token) {
        for (int i = 0; i < cursors.length; i++) {
            Cursor c = cursors[i];
            Uri uri = Cons.UPDATE_URIS[i];
            String url = Cons.UPDATE_URLS[i];
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Log.i(TAG, "Peticion POST a " + url);
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    Utils.deCursorAJSONObject(c, uri, token),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            procesarUpdateResponse(response, token);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if(error!=null)
                                                Log.d(TAG, "Error Voley: " + error.getMessage().toString());
                                        }
                                    }
                            ) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8" + getParamsEncoding();
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    headers.put("Accept", "application/json");
                                    return headers;
                                }
                            }
                    );
                }
            } else {
                Log.i(TAG, "No se requiere sincronización en " + uri);
            }
            c.close();
        }
    }

    /**
     * @param cursors
     * @param token
     */
    private void deleteRequest(Cursor[] cursors, final String token) {
        for (int i = 0; i < cursors.length; i++) {
            Cursor c = cursors[i];
            Uri uri = Cons.DELETE_URIS[i];
            String url = Cons.DELETE_URLS[i];
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Log.i(TAG, "Peticion POST a " + url);
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    Utils.deCursorAJSONObject(c, uri, token),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            procesarDeleteResponse(response, token);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if(error!=null)
                                                Log.d(TAG, "Error Voley: " + error.getMessage().toString());
                                        }
                                    }
                            ) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8" + getParamsEncoding();
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    headers.put("Accept", "application/json");
                                    return headers;
                                }
                            }
                    );
                }
            } else {
                Log.i(TAG, "No se requiere sincronización en uri");
            }
            c.close();
        }
    }


    /**
     * Estado zutabea sinkronizatzera aldatzen du
     */
    private void startUpdate() {
        int results = 0;
        Uri[] uris = Cons.INSERT_URIS;

        for (int i = 0; i < uris.length; i++) {

            String selection = "(" + DatuBaseKontratua.Objetos_mapa.PENDIENTE_INSERCION + "=? OR " +
                    DatuBaseKontratua.Objetos_mapa.PENDIENTE_ACTUALIZACION + "=? OR " +
                    DatuBaseKontratua.Objetos_mapa.PENDIENTE_ELIMINACION + "=?) " +
                    "AND " + DatuBaseKontratua.Objetos_mapa.ESTADO + "=?";

            String[] selectionArgs = new String[]{"1", "1", "1", DatuBaseKontratua.ESTADO_OK + ""};

            if (MisteryProvider.uriMatcher.match(uris[i]) == MisteryProvider.USUARIOS) {
                selection = DatuBaseKontratua.Objetos_mapa.PENDIENTE_ACTUALIZACION + "=? " +
                        "AND " + DatuBaseKontratua.Objetos_mapa.ESTADO + "=?";
                selectionArgs = new String[]{"1", DatuBaseKontratua.ESTADO_SYNC + ""};
            }

            ContentValues v = new ContentValues();
            v.put(DatuBaseKontratua.Objetos_mapa.ESTADO, DatuBaseKontratua.ESTADO_SYNC);

            results += resolver.update(uris[i], v, selection, selectionArgs);

        }
        Log.i(TAG, "Registros puestos en cola para insertar, actualizar o eliminar:" + results);

    }

    /**
     * @return
     */
    private Cursor[] getUpdateData() {
        int registrosSucios = 0;
        Uri[] uris = Cons.UPDATE_URIS;
        Cursor[] cursors = new Cursor[uris.length];
        String selection = DatuBaseKontratua.Objetos_mapa.PENDIENTE_ACTUALIZACION + "=? AND "
                + DatuBaseKontratua.Objetos_mapa.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", DatuBaseKontratua.ESTADO_SYNC + ""};

        for (int i = 0; i < uris.length; i++) {
            cursors[i] = resolver.query(uris[i], null, selection, selectionArgs, null);
            registrosSucios += cursors[i].getCount();
        }
        Log.i(TAG, "Se encontraron " + registrosSucios + " registros sucios pendientes para actualizar!!");
        return cursors;
    }

    /**
     * @return
     */
    private Cursor[] getDeleteData() {
        int registrosSucios = 0;
        Uri[] uris = Cons.DELETE_URIS;
        Cursor[] cursors = new Cursor[uris.length];
        String selection = DatuBaseKontratua.Objetos_mapa.PENDIENTE_ELIMINACION + "=? AND "
                + DatuBaseKontratua.Objetos_mapa.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", DatuBaseKontratua.ESTADO_SYNC + ""};

        for (int i = 0; i < uris.length; i++) {
            cursors[i] = resolver.query(uris[i], null, selection, selectionArgs, null);
            registrosSucios += cursors[i].getCount();
        }
        Log.i(TAG, "Se encontraron " + registrosSucios + " registros sucios pendientes para eliminar!!");
        return cursors;
    }

    /**
     * @return
     */
    private Cursor[] getInsertData() {
        int registrosSucios = 0;
        Uri[] uris = Cons.INSERT_URIS;
        Cursor[] cursors = new Cursor[uris.length];
        String selection = DatuBaseKontratua.Objetos_mapa.PENDIENTE_INSERCION + "=? AND "
                + DatuBaseKontratua.Objetos_mapa.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", DatuBaseKontratua.ESTADO_SYNC + ""};

        for (int i = 0; i < uris.length; i++) {
            cursors[i] = resolver.query(uris[i], null, selection, selectionArgs, null);
            registrosSucios += cursors[i].getCount();
        }
        Log.i(TAG, "Se encontraron " + registrosSucios + " registros sucios pendientes para insertar!!");
        return cursors;
    }

    /**
     * @param id
     * @param tabla
     */
    private void finishUpdate(String id, String tabla, int idOperation) {
        // Eguneraketa amaitzeko uri-a
        Uri uri = null;
        switch (tabla) {
            case Cons.TABLA_USUARIO:
                uri = DatuBaseKontratua.Usuarios.crearUriUsuario(id);
                break;
            case Cons.TABLA_OBJETO_MAPA:
                uri = DatuBaseKontratua.Objetos_mapa.crearUriObjetoMapa(id);
                break;
            case Cons.TABLA_COMENTARIO:
                uri = DatuBaseKontratua.Comentarios.crearUriComentario(id);
                break;
            case Cons.TABLA_PSICOFONIA:
                uri = DatuBaseKontratua.Psicofonias.crearUriPsicofonia(id);
                break;
            case Cons.TABLA_FOTO:
                uri = DatuBaseKontratua.Fotos.crearUriFoto(id);
                break;
            default:
                break;
        }

        String selection = DatuBaseKontratua.Objetos_mapa.ID + "=?";
        String[] selectionArgs = new String[]{id};

        ContentValues v = new ContentValues();
        switch (idOperation) {
            case Cons.ID_INSERT:
                v.put(DatuBaseKontratua.Objetos_mapa.PENDIENTE_INSERCION, "0");
                break;
            case Cons.ID_UPDATE:
                v.put(DatuBaseKontratua.Objetos_mapa.PENDIENTE_ACTUALIZACION, "0");
                break;
            case Cons.ID_DELETE:
                v.put(DatuBaseKontratua.Objetos_mapa.PENDIENTE_ELIMINACION, "0");
                break;
        }
        v.put(DatuBaseKontratua.Objetos_mapa.ESTADO, DatuBaseKontratua.ESTADO_OK);

        resolver.update(uri, v, selection, selectionArgs);
    }

    public void procesarInsertResponse(JSONObject response, String token) {

        try {
            // Egoera jaso
            String estado = response.getString(Cons.ESTADO);
            // Mezua jaso
            String mensaje = response.getString(Cons.MENSAJE);
            // Identifikatzailea jaso
            String id = response.getString(Cons.ID);
            // Taula izena jaso
            String tabla = response.getString(Cons.TABLA);

            switch (estado) {
                case Cons.SUCCESS:
                    Log.i(TAG, mensaje);
                    if (tabla.equals(Cons.TABLA_OBJETO_MAPA)) {

                        int tipo = response.getInt(DatuBaseKontratua.Objetos_mapa.TIPO_ID);

                        updateDetailData(id, tabla, tipo, token, Cons.ID_INSERT);
                    } else {
                        finishUpdate(id, tabla, Cons.ID_INSERT);
                    }
                    break;

                case Cons.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void procesarUpdateResponse(JSONObject response, String token) {

        try {
            // Egoera jaso
            String estado = response.getString(Cons.ESTADO);
            // Mezua jaso
            String mensaje = response.getString(Cons.MENSAJE);
            // Identifikatzailea jaso
            String id = response.getString(Cons.ID);
            // Taula izena jaso
            String tabla = response.getString(Cons.TABLA);

            switch (estado) {
                case Cons.SUCCESS:
                    Log.i(TAG, mensaje);
                    if (tabla.equals(Cons.TABLA_OBJETO_MAPA)) {
                        JSONArray array = response.getJSONArray(Cons.TABLA_OBJETO_MAPA);
                        ObjetoMapa[] res = gson.fromJson(array != null ? array.toString() : null, ObjetoMapa[].class);

                        int tipo = res[0].getTipo_id();

                        updateDetailData(id, tabla, tipo, token, Cons.ID_UPDATE);

                    } else {
                        finishUpdate(id, tabla, Cons.ID_UPDATE);
                    }
                    break;

                case Cons.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void procesarDeleteResponse(JSONObject response, String token) {

        try {
            // Egoera jaso
            String estado = response.getString(Cons.ESTADO);
            // Mezua jaso
            String mensaje = response.getString(Cons.MENSAJE);
            // Taula izena jaso
            String tabla = response.getString(Cons.TABLA);
            // Identifikatzailea jaso
            String id = response.getString(Cons.ID);

            switch (estado) {
                case Cons.SUCCESS:
                    finishUpdate(id, tabla, Cons.ID_DELETE);
                    break;

                case Cons.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateDetailData(final String id, final String tabla, int tipo, String token, final int idOperation) {
        x = false;
        Uri uri;
        String url;

        if (idOperation == Cons.ID_INSERT) {
            switch (tipo) {
                case 1:
                    uri = DatuBaseKontratua.Ovnis.crearUriOvni(id);
                    url = Cons.INSERT_OVNI_URL;
                    break;
                case 2:
                    uri = DatuBaseKontratua.Fantasmas.crearUriFantasma(id);
                    url = Cons.INSERT_FANT_URL;
                    break;
                case 3:
                    uri = DatuBaseKontratua.Historicos.crearUriHistorico(id);
                    url = Cons.INSERT_HIST_URL;
                    break;
                case 4:
                    uri = DatuBaseKontratua.SinResolver.crearUriSinResolver(id);
                    url = Cons.INSERT_SIN_URL;
                    break;
                default:
                    throw new UnsupportedOperationException("Tipo no soportado: " + tipo);
            }
        } else {
            switch (tipo) {
                case 1:
                    uri = DatuBaseKontratua.Ovnis.crearUriOvni(id);
                    url = Cons.UPDATE_OVNI_URL;
                    break;
                case 2:
                    uri = DatuBaseKontratua.Fantasmas.crearUriFantasma(id);
                    url = Cons.UPDATE_FANT_URL;
                    break;
                case 3:
                    uri = DatuBaseKontratua.Historicos.crearUriHistorico(id);
                    url = Cons.UPDATE_HIST_URL;
                    break;
                case 4:
                    uri = DatuBaseKontratua.SinResolver.crearUriSinResolver(id);
                    url = Cons.UPDATE_SIN_URL;
                    break;
                default:
                    throw new UnsupportedOperationException("Tipo no soportado: " + tipo);
            }
        }
        Cursor c = resolver.query(uri, null, null, null, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {

                Log.d(TAG, "Peticion POST a " + url);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                Utils.deCursorAJSONObject(c, uri, token),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        String mensaje = "Mensaje vacio!";
                                        try {
                                            mensaje = response.getString(Cons.MENSAJE);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d(TAG , mensaje);
                                        finishUpdate(id, tabla, idOperation);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if(error!=null)
                                            Log.d(TAG, "Error Voley: " + error.getMessage().toString());
                                    }
                                }
                        ) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }
                        }
                );
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected()...");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        if (connectionResult.hasResolution()) {
            getContext().startActivity(SignInResolutionActivity.newStartIntent(getContext(), connectionResult));
        } else {
            Log.e(TAG, "onConnectionFAiled() no resolution");
        }
    }

    public static void insertUser(final Context context, final GoogleSignInAccount account) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", account.getIdToken());
            object.put(DatuBaseKontratua.Usuarios.ID, account.getId());
            object.put(DatuBaseKontratua.Usuarios.NOMBRE, account.getDisplayName());
            object.put("correo", account.getEmail());
            object.put(DatuBaseKontratua.Usuarios.FOTO, account.getPhotoUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Peticion POST a " + Cons.INSERT_USER_URL);
        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Cons.INSERT_USER_URL,
                        object,
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
                                //Log.i(TAG, error.getMessage());
                            }
                        }
                )
        );

    }

    public static void deleteUser(final Context context, final String id, final String token) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = DatuBaseKontratua.Usuarios.crearUriUsuario(id);
        Cursor c = resolver.query(uri, null, null, null, null);

        Log.i(TAG, "Peticion POST a " + Cons.DELETE_USER_URL);
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
                                if(error!=null)
                                    Log.d(TAG, "Error Voley: " + error.getMessage().toString());
                            }
                        }
                )
        );
    }
}
