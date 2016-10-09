package com.tikal.fooservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by shaul on 09/10/2016.
 */
public class FooService extends Service {
    private IFooServiceImpl service;
    @Override
    public void onCreate() {
        super.onCreate();
        this.service = new IFooServiceImpl();
    }

    @Override
    public void onDestroy() {
        this.service = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.service;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
