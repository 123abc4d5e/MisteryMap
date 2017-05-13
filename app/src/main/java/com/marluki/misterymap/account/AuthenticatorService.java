package com.marluki.misterymap.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    // autentikatzailea gordetzen duen aldagaia
    private MisteryAuthenticator mmAuthenticator;

    @Override
    public void onCreate() {
        // autentikatzaile objektu berria sortzen
        mmAuthenticator = new MisteryAuthenticator(this);
    }

    /**
     * Zerbitzuari dei egiten doinean RPC-a lortzeko
     * @param intent
     * @return autentikatzailearen IBinder-a bueltatzen du
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mmAuthenticator.getIBinder();
    }
}
