package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.Pickup;

public abstract class PickupHistoryPopupMenu extends PopupMenu
{
    private Pickup mPickup;

    public PickupHistoryPopupMenu(Context context, View anchorView, Pickup pickup)
    {
        super(context, anchorView);
        mPickup = pickup;
        initialize();
    }

    private void initialize()
    {
        getMenuInflater().inflate(R.menu.pickup_history_menu, getMenu());
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
                else if (id == R.id.menu_pickup_courier_detail)
                {
                    onCourierView();
                }

                return true;
            }
        });
    }

    public abstract void onView();
    public abstract void onCourierView();
}
