package coid.customer.pickupondemand.jet;

import android.content.Context;
import android.content.Intent;

import com.activeandroid.app.Application;
import com.onesignal.OneSignal;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import coid.customer.pickupondemand.jet.notification.JETNotificationOpenedHandler;
import coid.customer.pickupondemand.jet.notification.JETNotificationReceivedHandler;

public abstract class JETApplication extends Application
{
    private static JETApplication instance;
    private static ThreadPoolExecutor mPool;
    public static JETNotificationReceivedHandler notificationReceivedHandler;
    public static JETNotificationOpenedHandler notificationOpenedHandler;

    public JETApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationReceivedHandler = new JETNotificationReceivedHandler();
        notificationOpenedHandler = new JETNotificationOpenedHandler();
        notificationOpenedHandler.setStartActivityIntent(getStartActivityIntent());

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(notificationReceivedHandler)
                .setNotificationOpenedHandler(notificationOpenedHandler)
                .init();

        mPool =  new ThreadPoolExecutor(5, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
    }

    public static Context getContext() {
        return instance;
    }

    public static ThreadPoolExecutor getThreadPoolExecutor()
    {
        return mPool;
    }

    public abstract Intent getStartActivityIntent();
}


