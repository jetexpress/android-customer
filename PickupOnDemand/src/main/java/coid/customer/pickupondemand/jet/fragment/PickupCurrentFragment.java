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
import coid.customer.pickupondemand.jet.custom.DeleteConfirmationDialog;
import coid.customer.pickupondemand.jet.custom.PickupPopupMenu;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.QueryResult;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import coid.customer.pickupondemand.jet.request.PickupDraftDeleteRequest;
import coid.customer.pickupondemand.jet.request.PickupRequestedCancelRequest;
import retrofit2.Call;
import retrofit2.Response;

public class PickupCurrentFragment extends PickupOnDemandListFragment implements IBaseOverflowMenuListener
{
    private PickupDraftDeleteRequest mPickupDraftDeleteRequest;
    private PickupRequestedCancelRequest mPickupRequestedCancelRequest;
    private PickupCurrentFragmentListener mPickupCurrentFragmentListener;

    public PickupCurrentFragment()
    {
        // Required empty public constructor
    }

    public static PickupCurrentFragment newInstance(String title)
    {
        PickupCurrentFragment fragment = new PickupCurrentFragment();
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
        showFABNewPickup();
    }

    @Override
    public void onDestroy()
    {
        if (mPickupDraftDeleteRequest != null)
        {
            mPickupDraftDeleteRequest.clear();
            mPickupDraftDeleteRequest = null;
        }
        if (mPickupRequestedCancelRequest != null)
        {
            mPickupRequestedCancelRequest.clear();
            mPickupRequestedCancelRequest = null;
        }
        super.onDestroy();
    }

    @Override
    public String getTitle()
    {
        if (getArguments() != null)
            return getArguments().getString(TITLE_ARGS_PARAM);
        else
            return "";
    }

    @Override
    public Call<QueryResult<Pickup>> getPickupCall(Long currentPage)
    {
        return RetrofitProvider.getAuthorizedResourcesService().getActivePickupList(AppConfig.DEFAULT_PAGING_SIZE, currentPage);
    }

    @Override
    protected void onFABNewPickupClicked()
    {
        if (mPickupCurrentFragmentListener != null)
            mPickupCurrentFragmentListener.onCreatePickup();
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
        PickupPopupMenu pickupPopupMenu = new PickupPopupMenu(mContext, anchorView, pickup)
        {
            @Override
            public void onEdit()
            {
                if (mPickupCurrentFragmentListener != null)
                    mPickupCurrentFragmentListener.onEditPickup(pickup);
            }

            @Override
            public void onView()
            {
                if (mPickupCurrentFragmentListener != null)
                    mPickupCurrentFragmentListener.onViewPickup(pickup);
            }

            @Override
            public void onCourierView()
            {
                if (mPickupCurrentFragmentListener != null)
                    mPickupCurrentFragmentListener.onViewCourier(pickup);
            }

            @Override
            public void onObserve()
            {
                getNavigator().showFragment(ObserveCourierFragment.newInstance(pickup));
            }

            @Override
            public void onDelete()
            {
                String title = getString(R.string.pod_delete_pickup);
                String message = getString(R.string.pod_delete_pickup_confirmation);
                DeleteConfirmationDialog deleteConfirmationDialog = new DeleteConfirmationDialog(mContext, title, message)
                {
                    @Override
                    public void onOKClicked()
                    {
                        requestDeletePickup(pickup);
                    }
                };
                deleteConfirmationDialog.show();
            }

            @Override
            public void onCancel()
            {
                String title = getString(R.string.pod_cancel_pickup);
                String message = getString(R.string.pod_cancel_pickup_confirmation);
                DeleteConfirmationDialog cancelConfirmationDialog = new DeleteConfirmationDialog(mContext, title, message)
                {
                    @Override
                    public void onOKClicked()
                    {
                        requestCancelPickup(pickup);
                    }
                };
                cancelConfirmationDialog.show();
            }
        };

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(mContext, (MenuBuilder) pickupPopupMenu.getMenu(), anchorView);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    private void requestDeletePickup(final Pickup pickup)
    {
        mPickupDraftDeleteRequest = new PickupDraftDeleteRequest(mContext, pickup.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                super.onSuccessOnUIThread(response);
                getPickupAdapter().remove(pickup);
                getPickupAdapter().notifyDataSetChanged();
            }
        };
        mPickupDraftDeleteRequest.executeAsync();
    }

    private void requestCancelPickup(final Pickup pickup)
    {
        mPickupRequestedCancelRequest = new PickupRequestedCancelRequest(mContext, pickup.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                super.onSuccessOnUIThread(response);
                getPickupAdapter().remove(pickup);
                getPickupAdapter().notifyDataSetChanged();
            }
        };

        mPickupRequestedCancelRequest.executeAsync();
    }

    public void setListener(PickupCurrentFragmentListener pickupCurrentFragmentListener)
    {
        mPickupCurrentFragmentListener = pickupCurrentFragmentListener;
    }

    public interface PickupCurrentFragmentListener
    {
        void onCreatePickup();
        void onEditPickup(Pickup pickup);
        void onViewPickup(Pickup pickup);
        void onViewCourier(Pickup pickup);
    }
}
