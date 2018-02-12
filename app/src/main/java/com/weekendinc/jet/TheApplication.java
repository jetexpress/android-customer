package com.weekendinc.jet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;

import org.acra.ACRA;

import coid.customer.pickupondemand.jet.JETApplication;

/**
 * Created by Fadhlan on 2/9/15.
 */
//@ReportsCrashes( // will not be used
//        mailTo = "antoni@weekendinc.com,andoko@weekendinc.com,art@weekendinc.com",
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.acra_error_text)
public class TheApplication extends JETApplication
{
    private static TheApplication instance = null;

    public TheApplication() {
        instance = this;
    }

    public static TheApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean _isonline = netInfo != null && netInfo.isConnectedOrConnecting();
        return _isonline;
    }

    @Override
    public Intent getStartActivityIntent()
    {
        return new Intent(this, MainActivity.class);
    }
}
