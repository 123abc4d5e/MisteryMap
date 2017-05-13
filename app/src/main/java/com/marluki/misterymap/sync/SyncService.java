package com.marluki.misterymap.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {
    // Sync adapter-aren instantzia gordetzen
    private static SyncAdapter syncAdapter = null;
    // Hariak modu seguruan(blokeatuak) gortzeko objektua
    private static Object ssyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (ssyncAdapterLock) {
            if (syncAdapter == null) {
                ssyncAdapterLock = new SyncAdapter(this, true);
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return syncAdapter.getSyncAdapterBinder();
    }
}
