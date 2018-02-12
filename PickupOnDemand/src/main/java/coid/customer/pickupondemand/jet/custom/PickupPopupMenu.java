package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.Pickup;

public abstract class PickupPopupMenu extends PopupMenu
{
    private Pickup mPickup;

    public PickupPopupMenu(Context context, View anchorView, Pickup pickup)
    {
        super(context, anchorView);
        mPickup = pickup;
        initialize();
        filterByPickupStatus();
    }

    private void initialize()
    {
        getMenuInflater().inflate(R.menu.pickup_current_menu, getMenu());
        setOnMenuItemClickListener(new OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                int id = item.getItemId();
                if (id == R.id.menu_pickup_edit)
                {
                    onEdit();
                }
                else if (id == R.id.menu_pickup_detail)
                {
                    onView();
                }
                else if (id == R.id.menu_pickup_courier_detail)
                {
                    onCourierView();
                }
                else if (id == R.id.menu_observe_pickup_via_map)
                {
                    onObserve();
                }
                else if (id == R.id.menu_delete_pickup)
                {
                    onDelete();
                }
                else if (id == R.id.menu_cancel_pickup)
                {
                    onCancel();
                }

                return true;
            }
        });
    }

    private void filterByPickupStatus()
    {
        MenuItem editMenuItem = getMenu().findItem(R.id.menu_pickup_edit);
        MenuItem detailMenuItem = getMenu().findItem(R.id.menu_pickup_detail);
        MenuItem courierDetailMenuItem = getMenu().findItem(R.id.menu_pickup_courier_detail);
        MenuItem observeMenuItem = getMenu().findItem(R.id.menu_observe_pickup_via_map);
        MenuItem deleteMenuItem = getMenu().findItem(R.id.menu_delete_pickup);
        MenuItem cancelMenuItem = getMenu().findItem(R.id.menu_cancel_pickup);

        editMenuItem.setVisible(mPickup.isDraft());
        detailMenuItem.setVisible(mPickup.isRequested() || mPickup.isAssigned() || mPickup.isTripStarted() || mPickup.hasArrived());
        courierDetailMenuItem.setVisible(mPickup.isAssigned() || mPickup.isTripStarted() || mPickup.hasArrived());
        observeMenuItem.setVisible(mPickup.isTripStarted() || mPickup.hasArrived());
        deleteMenuItem.setVisible(mPickup.isDraft());
        cancelMenuItem.setVisible(!mPickup.isDraft() && !mPickup.hasArrived());
    }

    public abstract void onEdit();
    public abstract void onView();
    public abstract void onCourierView();
    public abstract void onObserve();
    public abstract void onDelete();
    public abstract void onCancel();
}
