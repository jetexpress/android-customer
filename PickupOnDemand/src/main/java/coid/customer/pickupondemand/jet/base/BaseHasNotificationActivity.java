package coid.customer.pickupondemand.jet.base;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.custom.DeleteConfirmationDialog;
import coid.customer.pickupondemand.jet.custom.PickupNotificationMenu;
import coid.customer.pickupondemand.jet.fragment.PickupNotificationFragment;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.db.DBQuery;

public abstract class BaseHasNotificationActivity extends BaseActivity
        implements PickupNotificationMenu.INotificationMenuListener
{
    private PickupNotificationMenu mPickupNotificationMenu;
    private Menu mOptionMenu;

    @Override
    protected void onDestroy()
    {
        if (mPickupNotificationMenu != null)
            mPickupNotificationMenu.clear();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.pickup_main_menu, menu);
        mOptionMenu = menu;
        mPickupNotificationMenu = new PickupNotificationMenu(this, menu.findItem(R.id.action_notification), this);
        if (getSupportLoaderManager().getLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID) != null)
            getSupportLoaderManager().restartLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID, null, mPickupNotificationMenu.getLoader());
        else
            getSupportLoaderManager().initLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID, null, mPickupNotificationMenu.getLoader());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_clear_all)
        {
            String title = JETApplication.getContext().getString(R.string.pod_clear_all_title);
            String message = JETApplication.getContext().getString(R.string.pod_clear_all_confirmation);
            DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(this, title, message)
            {
                @Override
                public void onOKClicked()
                {
                    DBQuery.deleteAll(NotificationPayload.class);
                    DBQuery.truncate(NotificationPayload.class);
                }
            };
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotificationMenuClicked()
    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(getFragmentContainerId());
        if (!(currentFragment instanceof PickupNotificationFragment))
            getNavigator().showFragment(new PickupNotificationFragment());
    }
}
