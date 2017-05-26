package com.marluki.misterymap.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by charl on 13/05/2017.
 */

public class VolleySingleton {

    private static VolleySingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;
    private ImageLoader imageLoader;

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(this.requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(30);
            //30 -> the maximum number of entries in the cache.

            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
                Log.d("CachedItems", String.valueOf(lruCache.size()));
            }

            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }
        });
    }


    /**
     * Singleton-aren instantzia bakarra bueltatzen du
     *
     * @param context Eskaerak exakutatuko diren kontextua
     * @return Instantzia
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleySingleton(context.getApplicationContext());
        }
        return singleton;
    }

    /**
     * Eskaeren hilararen instantzia lortzen du
     * @return Eskaeren hilara
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * Hilarara eskaera bat gehitzen du
     *
     * @param req eskaera
     * @param <T> T motatako emaitza finala
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
