package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.NotificationPayload;

public abstract class PickupNotificationPopupMenu extends PopupMenu
{
    private NotificationPayload mNotificationPayload;
    private Context mContext;

    public PickupNotificationPopupMenu(Context context, View anchorView, NotificationPayload notificationPayload)
    {
        super(context, anchorView);
        mContext = context;
        mNotificationPayload = notificationPayload;
        initialize();
    }

    private void initialize()
    {
        getMenuInflater().inflate(R.menu.notification_menu, getMenu());
        setOnMenuItemClickListener(new OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                int id = item.getItemId();
                if (id == R.id.menu_pickup_detail)
                {
                    onView();
                }
                else if (id == R.id.menu_delete_notification)
                {
                    String title = JETApplication.getContext().getString(R.string.pod_delete_notification_title);
                    String message = JETApplication.getContext().getString(R.string.pod_delete_notification_confirmation);
                    DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(mContext, title, message)
                    {
                        @Override
                        public void onOKClicked()
                        {
                            onDelete();
                        }
                    };
                    dialog.show();
                }

                return true;
            }
        });
    }

    public abstract void onView();
    public abstract void onDelete();
}
