package coid.customer.pickupondemand.jet.fragment;

import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.View;
import android.widget.AdapterView;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.IBaseOverflowMenuListener;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.custom.PickupHistoryPopupMenu;
import coid.customer.pickupondemand.jet.custom.PickupPopupMenu;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.QueryResult;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class PickupHistoryFragment extends PickupOnDemandListFragment implements IBaseOverflowMenuListener
{
    private PickupHistoryFragmentListener mPickupHistoryFragmentListener;

    public PickupHistoryFragment()
    {
        // Required empty public constructor
    }

    public static PickupHistoryFragment newInstance(String title)
    {
        PickupHistoryFragment fragment = new PickupHistoryFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_ARGS_PARAM, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setPickupMenuListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        hideFABNewPickup();
    }

    @Override
    public String getTitle()
    {
        if (getArguments() != null)
            return getArguments().getString(TITLE_ARGS_PARAM);
        return "";
    }

    @Override
    public Call<QueryResult<Pickup>> getPickupCall(Long currentPage)
    {
        return RetrofitProvider.getAuthorizedResourcesService().getHistoryPickupList(AppConfig.DEFAULT_PAGING_SIZE, currentPage);
    }

    @Override
    protected void onListItemClicked(AdapterView<?> parent, View view, int position, long id)
    {
        final Pickup pickup = getPickupAdapter().getItem(position);
        if (pickup == null)
            return;

        View anchorView = view.findViewById(R.id.tv_price);
        if (anchorView == null)
            return;

        showPickupPopupMenu(pickup, anchorView);
    }

    @Override
    public void onMenuClicked(View v, int position)
    {
        final Pickup pickup = getPickupAdapter().getItem(position);
        if (pickup == null)
            return;

        showPickupPopupMenu(pickup, v);
    }

    private void showPickupPopupMenu(final Pickup pickup, View anchorView)
    {
        PickupHistoryPopupMenu pickupHistoryPopupMenu = new PickupHistoryPopupMenu(mContext, anchorView, pickup)
        {
            @Override
            public void onView()
            {
                if (mPickupHistoryFragmentListener != null)
                    mPickupHistoryFragmentListener.onViewPickupHistory(pickup);
            }

            @Override
            public void onCourierView()
            {
                if (mPickupHistoryFragmentListener != null)
                    mPickupHistoryFragmentListener.onViewCourierHistory(pickup);
            }
        };

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(mContext, (MenuBuilder) pickupHistoryPopupMenu.getMenu(), anchorView);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    public void setListener(PickupHistoryFragmentListener pickupHistoryFragmentListener)
    {
        mPickupHistoryFragmentListener = pickupHistoryFragmentListener;
    }

    public interface PickupHistoryFragmentListener
    {
        void onViewPickupHistory(Pickup pickup);
        void onViewCourierHistory(Pickup pickup);
    }

}
