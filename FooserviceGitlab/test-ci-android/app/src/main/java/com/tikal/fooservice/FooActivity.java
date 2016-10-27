package com.tikal.fooservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FooActivity extends AppCompatActivity implements ServiceConnection, View.OnClickListener {

    private static final String TAG = "FooActivity";
    private IFooService service;
    private Button fooButton;
    private TextView asyncResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foo);
        fooButton = (Button)findViewById(R.id.fooButton);
        fooButton.setOnClickListener(this);
        asyncResponse = (TextView)findViewById(R.id.asyncResponse);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!super.bindService(new Intent(this, FooService.class), this, BIND_AUTO_CREATE)) {
            Log.w(TAG, "Failed to bind to service");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = IFooService.Stub.asInterface(service);
        fooButton.setEnabled(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.service = null;
        fooButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        try {
            service.save(new Bar((int)(SystemClock.elapsedRealtime()/1000), "bar " + (int)(SystemClock.elapsedRealtime()/1000)));

            service.asyncGetList(new IFooResponseListner.Stub() {
                @Override
                public void onResponse(final String response) throws RemoteException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            asyncResponse.setText(response);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
