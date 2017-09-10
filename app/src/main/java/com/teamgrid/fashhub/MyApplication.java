package com.teamgrid.fashhub;

import android.app.Application;

import com.teamgrid.fashhub.utils.ConnectivityReceiver;


public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        MyApplication appObj;
        synchronized (MyApplication.class) {
            appObj = mInstance;
        }
        return appObj;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
