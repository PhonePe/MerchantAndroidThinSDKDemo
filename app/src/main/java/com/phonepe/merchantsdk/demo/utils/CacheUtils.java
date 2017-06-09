package com.phonepe.merchantsdk.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * @author Sharath Pandeshwar
 * @since 28/09/16.
 */
public class CacheUtils {
    private static final String TAG = "CacheUtils";

    /* Reference to shared preference */
    private SharedPreferences mSharedPreferences;
    private static CacheUtils sCacheUtils;

    /**
     * Keys
     */
    private static String sUserIdKey = "key_user_id";
    private static String sAmountKey = "key_amount";
    private static String sMobileKey = "key_mobile";
    private static String sEmailKey = "key_email";
    private static String sNameKey = "key_name";


    private CacheUtils(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CacheUtils getInstance(Context context) {
        if (sCacheUtils == null) {
            synchronized (CacheUtils.class) {
                sCacheUtils = new CacheUtils(context);
            }
        }
        return sCacheUtils;
    }

    //*********************************************************************
    // APIs
    //*********************************************************************


    public void saveUserId(String userId) {
        mSharedPreferences.edit().putString(sUserIdKey, userId).apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(sUserIdKey, "unknown143");
    }


    public void saveMobile(String mobile) {
        mSharedPreferences.edit().putString(sMobileKey, mobile).apply();
    }

    public String getMobile() {
        return mSharedPreferences.getString(sMobileKey, "");
    }


    public void saveName(String mobile) {
        mSharedPreferences.edit().putString(sNameKey, mobile).apply();
    }

    public String getName() {
        return mSharedPreferences.getString(sNameKey, "");
    }

    public void saveEmail(String mobile) {
        mSharedPreferences.edit().putString(sEmailKey, mobile).apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString(sEmailKey, "test@test.com");
    }


    public void saveAmountForTransaction(Long amount) {
        mSharedPreferences.edit().putLong(sAmountKey, amount).apply();
    }

    public Long getAmountForTransaction() {
        return mSharedPreferences.getLong(sAmountKey, 2L);
    }

    //*********************************************************************
    // End of the class
    //*********************************************************************

}
