package com.hotmart.dragonfly;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * @author anapsil
 */
public class DragonFlyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
