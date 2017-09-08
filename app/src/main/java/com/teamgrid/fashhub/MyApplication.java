package com.teamgrid.fashhub;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.teamgrid.fashhub.utils.ConnectivityReceiver;


public class MyApplication extends Application { //extends MultiDexApplication {

    private static final String TAG = "Application";
    private static MyApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        MyApplication appObj;
        synchronized (MyApplication.class) {
            appObj = mInstance;
        }
        return appObj;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
