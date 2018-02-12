package coid.customer.pickupondemand.jet.notification;

import android.content.Intent;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import coid.customer.pickupondemand.jet.JETApplication;

public class JETNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler
{
    public static final String NOTIFICATION_OPENED_PARAM = "notificationOpened";
    private INotificationOpenedListener mNotificationOpenedListener;
    private Intent mStartActivityIntent;

    @Override
    public void notificationOpened(OSNotificationOpenResult result)
    {
        JSONObject data = result.notification.payload.additionalData;
        if (data == null)
            return;

        if (result.notification.isAppInFocus)
        {
            if (mNotificationOpenedListener != null)
                mNotificationOpenedListener.onNotificationOpenedAppInFocus(data.toString());
        }
        else
        {
            if (mStartActivityIntent != null)
            {
                mStartActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mStartActivityIntent.putExtra(NOTIFICATION_OPENED_PARAM, data.toString());
                JETApplication.getContext().startActivity(mStartActivityIntent);
            }
        }
    }

    public void setOnOpenedListener(INotificationOpenedListener notificationOpenedListener)
    {
        mNotificationOpenedListener = notificationOpenedListener;
    }

    public void setStartActivityIntent(Intent startActivityIntent)
    {
        mStartActivityIntent = startActivityIntent;
    }

    public void clearListener()
    {
        mNotificationOpenedListener = null;
    }

    public interface INotificationOpenedListener
    {
        void onNotificationOpenedAppInFocus(String notificationData);
    }
}
