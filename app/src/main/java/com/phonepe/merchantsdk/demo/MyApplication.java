package com.phonepe.merchantsdk.demo;

import android.app.Application;

import com.phonepe.android.sdk.api.PhonePe;

/**
 * @author Sharath Pandeshwar
 * @since 26/09/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PhonePe.init(this);
    }
}
