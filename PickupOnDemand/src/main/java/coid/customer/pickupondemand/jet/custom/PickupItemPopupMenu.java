package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.PickupItem;

public abstract class PickupItemPopupMenu extends PopupMenu
{
    private PickupItem mPickupItem;
    private Boolean mIsDraft;

    public PickupItemPopupMenu(Context context, View anchorView, PickupItem pickupItem, Boolean isDraft)
    {
        super(context, anchorView);
        mPickupItem = pickupItem;
        mIsDraft = isDraft;
        initialize();
        filterByPickupStatus();
    }

    private void initialize()
    {
        getMenuInflater().inflate(R.menu.pickup_item_menu, getMenu());
        setOnMenuItemClickListener(new OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                int id = item.getItemId();
                if (id == R.id.menu_view_pickup_item)
                {
                    onView();
                }
                else if (id == R.id.menu_edit_pickup_item)
                {
                    onEdit();
                }
                else if (id == R.id.menu_delete_pickup_item)
                {
                    onDelete();
                }

                return true;
            }
        });
    }

    private void filterByPickupStatus()
    {
        MenuItem viewMenuItem = getMenu().findItem(R.id.menu_view_pickup_item);
        MenuItem editMenuItem = getMenu().findItem(R.id.menu_edit_pickup_item);
        MenuItem deleteMenuItem = getMenu().findItem(R.id.menu_delete_pickup_item);

        viewMenuItem.setVisible(!mIsDraft);
        editMenuItem.setVisible(mIsDraft);
        deleteMenuItem.setVisible(mIsDraft);
    }

    public abstract void onEdit();
    public abstract void onView();
    public abstract void onDelete();
}
