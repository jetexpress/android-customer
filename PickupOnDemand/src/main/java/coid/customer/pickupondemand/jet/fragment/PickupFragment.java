package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.adapter.PickupFragmentPagerAdapter;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.request.PickupGetByCodeRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupFragment extends BaseFragment
        implements PickupCurrentFragment.PickupCurrentFragmentListener,
        PickupHistoryFragment.PickupHistoryFragmentListener,
        PickupDetailFragment.PickupDetailFragmentListener,
        CourierDetailFragment.ICourierDetailFragmentListener
{
    private static final String NOTIFICATION_OPENED_ARGS_PARAM = "notificationOpenedParam";
    private static final String NOTIFICATION_FROM_LIST_ARGS_PARAM = "notificationFromListParam";
    private static final int ADD_REQUEST_CODE = 201;
    private static final int EDIT_REQUEST_CODE = 202;
    private static final int VIEW_PICKUP_REQUEST_CODE = 203;
    private static final int VIEW_COURIER_REQUEST_CODE = 204;
    private static final int VIEW_PICKUP_HISTORY_REQUEST_CODE = 205;
    private static final int VIEW_COURIER_HISTORY_REQUEST_CODE = 206;

    private TabLayout tab_layout;
    private ViewPager view_pager;
    private PickupFragmentPagerAdapter mAdapter;
    private NotificationPayload mNotificationOpenedPayload;
    private NotificationPayload mNotificationPayloadFromList;
    private PickupGetByCodeRequest mPickupGetByCodeRequest;

    public PickupFragment()
    {
        // Required empty public constructor
    }

    public static PickupFragment onNotificationOpenedInstance(String notificationData)
    {
        PickupFragment fragment = new PickupFragment();
        Bundle args = new Bundle();
        args.putString(NOTIFICATION_OPENED_ARGS_PARAM, notificationData);
        fragment.setArguments(args);
        return fragment;
    }

    public static PickupFragment onNotificationFromListInstance(String notificationData)
    {
        PickupFragment fragment = new PickupFragment();
        Bundle args = new Bundle();
        args.putString(NOTIFICATION_FROM_LIST_ARGS_PARAM, notificationData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            if (getArguments().containsKey(NOTIFICATION_OPENED_ARGS_PARAM))
                mNotificationOpenedPayload = NotificationPayload.createFromString(getArguments().getString(NOTIFICATION_OPENED_ARGS_PARAM));
            else if (getArguments().containsKey(NOTIFICATION_FROM_LIST_ARGS_PARAM))
                mNotificationPayloadFromList = NotificationPayload.createFromString(getArguments().getString(NOTIFICATION_FROM_LIST_ARGS_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setTabLayout();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(getString(R.string.pod_pickup));
        handleNotificationOpenedPickupStatus();
    }

    @Override
    public void onDestroy()
    {
        if (mPickupGetByCodeRequest != null)
        {
            mPickupGetByCodeRequest.clear();
            mPickupGetByCodeRequest = null;
        }
        super.onDestroy();
    }

    @Override
    public void onAttachFragment(Fragment childFragment)
    {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof PickupCurrentFragment)
            ((PickupCurrentFragment)childFragment).setListener(this);
        if (childFragment instanceof PickupHistoryFragment)
            ((PickupHistoryFragment) childFragment).setListener(this);
    }

    @Override
    public void onCreatePickup()
    {
        PickupDetailFragment pickupDetailFragment = new PickupDetailFragment();
        pickupDetailFragment.setTargetFragment(this, ADD_REQUEST_CODE);
        getNavigator().showFragment(pickupDetailFragment);
    }

    @Override
    public void onEditPickup(Pickup pickup)
    {
        PickupDetailFragment pickupDetailFragment = PickupDetailFragment.newInstance(pickup);
        pickupDetailFragment.setTargetFragment(this, EDIT_REQUEST_CODE);
        getNavigator().showFragment(pickupDetailFragment);
    }

    @Override
    public void onViewPickup(Pickup pickup)
    {
        PickupDetailFragment pickupDetailFragment = PickupDetailFragment.newInstance(pickup);
        pickupDetailFragment.setTargetFragment(this, VIEW_PICKUP_REQUEST_CODE);
        getNavigator().showFragment(pickupDetailFragment);
    }

    @Override
    public void onViewCourier(Pickup pickup)
    {
        CourierDetailFragment courierDetailFragment = CourierDetailFragment.newInstance(pickup);
        courierDetailFragment.setTargetFragment(this, VIEW_COURIER_REQUEST_CODE);
        getNavigator().showFragment(courierDetailFragment);
    }

    @Override
    public void onViewPickupHistory(Pickup pickup)
    {
        PickupHistoryDetailFragment pickupHistoryDetailFragment = PickupHistoryDetailFragment.newInstance(pickup);
        pickupHistoryDetailFragment.setTargetFragment(this, VIEW_PICKUP_HISTORY_REQUEST_CODE);
        getNavigator().showFragment(pickupHistoryDetailFragment);

    }

    @Override
    public void onViewCourierHistory(Pickup pickup)
    {
        CourierDetailFragment courierDetailFragment = CourierDetailFragment.readOnlyInstance(pickup);
        courierDetailFragment.setTargetFragment(this, VIEW_COURIER_HISTORY_REQUEST_CODE);
        getNavigator().showFragment(courierDetailFragment);
    }

    @Override
    public void onPickupCreated(Pickup pickup)
    {
        PickupCurrentFragment fragment = mAdapter.getPickupCurrentFragment();

        if (pickup == null)
            Log.d("PICKUP_127", "PICKUP NULL");

        if (fragment == null)
            Log.d("PICKUP_127", "FRAGMENT NULL");
        else
        {
            if (fragment.getPickupAdapter() == null)
                Log.d("PICKUP_127", "ADAPTER NULL");
            else
            {
                fragment.getPickupAdapter().insert(pickup, 0);
                fragment.getPickupAdapter().notifyDataSetChanged();
                Log.d("PICKUP_127", "PICKUP ADDED");
            }
        }
    }

    @Override
    public void onPickupItemChanged()
    {
        PickupCurrentFragment fragment = mAdapter.getPickupCurrentFragment();

        if (fragment == null)
            Log.d("PICKUP_127", "FRAGMENT NULL");
        else
        {
            if (fragment.getPickupAdapter() == null)
                Log.d("PICKUP_127", "ADAPTER NULL");
            else
            {
                fragment.getPickupAdapter().notifyDataSetChanged();
                Log.d("PICKUP_127", "PICKUP CHANGED");
            }
        }
    }

    @Override
    public void onCourierDetailPickupCancelled(Pickup pickup)
    {
        onPickupCancelled(pickup);
    }

    private void setView()
    {
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void setTabLayout()
    {
        mAdapter = new PickupFragmentPagerAdapter(getChildFragmentManager());
        view_pager.setAdapter(mAdapter);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void handleNotificationOpenedPickupStatus()
    {
        if (mNotificationOpenedPayload != null)
        {
            switch (mNotificationOpenedPayload.getStatus())
            {
                case Pickup.STATUS_DRAFTED:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.pod_pickup_dialog_draft_title);
                    builder.setMessage(R.string.pod_pickup_dialog_draft_content);
                    builder.setPositiveButton(R.string.pod_ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    mNotificationOpenedPayload = null;
                    break;
                case Pickup.STATUS_ASSIGNED : getNavigator().showFragment(CourierDetailFragment.newInstance(mNotificationOpenedPayload.getCode()));
                    mNotificationOpenedPayload = null;
                    break;
                case Pickup.STATUS_TRIP_STARTED : getNavigator().showFragment(ObserveCourierFragment.newInstance(mNotificationOpenedPayload.getCode()));
                    mNotificationOpenedPayload = null;
                    break;
                case Pickup.STATUS_ARRIVED : getNavigator().showFragment(CourierDetailFragment.newInstance(mNotificationOpenedPayload.getCode()));
                    mNotificationOpenedPayload = null;
                    break;
                case Pickup.STATUS_COMPLETED : requestPickup();
                    break;
                default:
                    Log.d("JET_127","Default");
                    mNotificationOpenedPayload = null;
                    break;
            }
        }
        else if (mNotificationPayloadFromList != null)
        {
            Pickup pickup = new Pickup();
            pickup.setCode(mNotificationPayloadFromList.getCode());
            pickup.setStatus(mNotificationPayloadFromList.getStatus());

            if (pickup.isActive())
                getNavigator().showFragment(PickupDetailFragment.newInstance(pickup));
            else
                getNavigator().showFragment(PickupHistoryDetailFragment.newInstance(pickup));

            mNotificationPayloadFromList = null;
        }
    }

    private void requestPickup()
    {
        if (mNotificationOpenedPayload == null)
            return;

        mPickupGetByCodeRequest = new PickupGetByCodeRequest(mContext, mNotificationOpenedPayload.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                super.onSuccessOnUIThread(response);
                mNotificationOpenedPayload = null;
                RatePickupDialogFragment dialog = RatePickupDialogFragment.newInstance(response.body());
                dialog.show(getFragmentManager(), RatePickupDialogFragment.class.getSimpleName());
            }
        };
        mPickupGetByCodeRequest.executeAsync();
    }

    private void onPickupCancelled(Pickup pickup)
    {
        PickupCurrentFragment currentFragment = mAdapter.getPickupCurrentFragment();
        PickupHistoryFragment historyFragment = mAdapter.getPickupHistoryFragment();

        if (pickup == null)
            Log.d("PICKUP_127", "PICKUP NULL");

        if (currentFragment == null)
            Log.d("PICKUP_127", "CURRENT FRAGMENT NULL");
        else
        {
            if (currentFragment.getPickupAdapter() == null)
                Log.d("PICKUP_127", "CURRENT ADAPTER NULL");
            else
            {
                currentFragment.refreshList();
//                currentFragment.getPickupAdapter().add(pickup);
//                currentFragment.getPickupAdapter().notifyDataSetChanged();
                Log.d("PICKUP_127", "CURRENT PICKUP CANCELLED");
            }
        }

        if (historyFragment == null)
            Log.d("PICKUP_127", "HISTORY FRAGMENT NULL");
        else
        {
            if (historyFragment.getPickupAdapter() == null)
                Log.d("PICKUP_127", "HISTORY ADAPTER NULL");
            else
            {
                historyFragment.refreshList();
//                historyFragment.getPickupAdapter().remove(pickup);
//                historyFragment.getPickupAdapter().notifyDataSetChanged();
                Log.d("PICKUP_127", "HISTORY PICKUP CANCELLED");
            }
        }
    }
}
