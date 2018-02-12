package coid.customer.pickupondemand.jet.notification;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.Pickup;

public class JETNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler
{
    private INotificationReceivedListener mNotificationReceivedListener;

    @Override
    public void notificationReceived(OSNotification notification)
    {
        JSONObject data = notification.payload.additionalData;

        if (data == null)
        {
//            OneSignal.cancelNotification(notification.androidNotificationId);
            Log.d("JET_127", "NOTIFICATION DATA NULL");
            return;
        }

        Log.d("JET_127", "NOTIFICATION DATA : " + data.toString());

        NotificationPayload notificationPayload = NotificationPayload.createFromString(data.toString());
        if (notificationPayload == null || !notificationPayload.isCustomer())
        {
//            OneSignal.cancelNotification(notification.androidNotificationId);
            Log.d("JET_127", "NOTIFICATION NOT FOR CUSTOMER");
            return;
        }

        if (!notificationPayload.getStatus().equals(Pickup.STATUS_DRAFTED) &&
                !notificationPayload.getStatus().equals(Pickup.STATUS_ASSIGNED) &&
                !notificationPayload.getStatus().equals(Pickup.STATUS_TRIP_STARTED) &&
                !notificationPayload.getStatus().equals(Pickup.STATUS_ARRIVED) &&
                !notificationPayload.getStatus().equals(Pickup.STATUS_COMPLETED))
        {
            Log.d("JET_127", "NOTIFICATION INVALID STATUS");
//            OneSignal.cancelNotification(notification.androidNotificationId);
            return;
        }

        NotificationPayload localNotificationPayload = NotificationPayload.getByCode(notificationPayload.getCode());
        if (localNotificationPayload != null)
        {
            localNotificationPayload.update(notificationPayload);
            localNotificationPayload.setUnread(true);
            localNotificationPayload.save();
        }
        else
        {
            notificationPayload.setUnread(true);
            notificationPayload.save();
        }

        if (mNotificationReceivedListener != null)
            mNotificationReceivedListener.onNotificationReceived(notificationPayload, notification.isAppInFocus);
    }

    public void setOnReceivedListener(INotificationReceivedListener notificationReceivedListener)
    {
        mNotificationReceivedListener = notificationReceivedListener;
    }

    public void clearListener()
    {
        mNotificationReceivedListener = null;
    }

    public interface INotificationReceivedListener
    {
        void onNotificationReceived(NotificationPayload notificationPayload, Boolean isAppInFocus);
    }
}
