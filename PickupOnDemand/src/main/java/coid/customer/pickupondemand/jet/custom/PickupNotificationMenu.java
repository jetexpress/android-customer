package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.db.DBContract;
import coid.customer.pickupondemand.jet.model.db.ModelLoader;

public class PickupNotificationMenu
{
    private Context mContext;
    private MenuItem mNotificationMenuItem;
    private INotificationMenuListener mNotificationMenuListener;
    private TextView tv_badge_count;

    public PickupNotificationMenu(Context context, MenuItem notificationMenuItem)
    {
        this(context, notificationMenuItem, null);
    }

    public PickupNotificationMenu(Context context, MenuItem notificationMenuItem, @Nullable INotificationMenuListener notificationMenuListener)
    {
        mContext = context;
        mNotificationMenuItem = notificationMenuItem;
        mNotificationMenuListener = notificationMenuListener;

        tv_badge_count = mNotificationMenuItem.getActionView().findViewById(R.id.tv_badge_count);

        mNotificationMenuItem.getActionView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mNotificationMenuListener != null)
                    mNotificationMenuListener.onNotificationMenuClicked();
            }
        });
    }

    public void update(int unreadCount)
    {
        if (tv_badge_count == null)
            return;

        if (unreadCount > 9)
            tv_badge_count.setText("9+");
        else
            tv_badge_count.setText(String.valueOf(unreadCount));

        if (unreadCount <= 0)
            tv_badge_count.setVisibility(View.GONE);
        else
            tv_badge_count.setVisibility(View.VISIBLE);
    }

    public void clear()
    {
        mContext = null;
        mNotificationMenuItem = null;
        mNotificationMenuListener = null;
    }

    public LoaderManager.LoaderCallbacks<List<NotificationPayload>> getLoader()
    {
        return new LoaderManager.LoaderCallbacks<List<NotificationPayload>>()
        {
            @Override
            public Loader<List<NotificationPayload>> onCreateLoader(int id, Bundle args)
            {
                return new ModelLoader<>(mContext, NotificationPayload.class, new Select()
                        .from(NotificationPayload.class)
                        .where(DBContract.NotificationPayloadEntry.COLUMN_IS_UNREAD + "= ?", true), false);
            }

            @Override
            public void onLoadFinished(Loader<List<NotificationPayload>> loader, List<NotificationPayload> data)
            {
                update(data.size());
//                update(12);
            }

            @Override
            public void onLoaderReset(Loader<List<NotificationPayload>> loader)
            {

            }
        };
    }

    public interface INotificationMenuListener
    {
        void onNotificationMenuClicked();
    }
}