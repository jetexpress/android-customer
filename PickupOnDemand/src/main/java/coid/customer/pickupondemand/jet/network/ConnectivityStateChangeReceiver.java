package coid.customer.pickupondemand.jet.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import coid.customer.pickupondemand.jet.Utility;

public class ConnectivityStateChangeReceiver extends BroadcastReceiver
{
    private static Integer mConnectivityState;

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
            mConnectivityState = Utility.NetworkConnectivity.getConnectivityStatus(intent);
    }

    public static Integer getConnectivityState() {
        return mConnectivityState;
    }
}