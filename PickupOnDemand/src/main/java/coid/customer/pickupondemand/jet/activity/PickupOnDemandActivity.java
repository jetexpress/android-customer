package coid.customer.pickupondemand.jet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseActivity;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.fragment.CourierOrderLoadingDialogFragment;
import coid.customer.pickupondemand.jet.fragment.PickupFragment;
import coid.customer.pickupondemand.jet.fragment.PickupLocationConfirmationFragment;
import coid.customer.pickupondemand.jet.fragment.PickupNotificationFragment;
import coid.customer.pickupondemand.jet.fragment.RatePickupDialogFragment;
import coid.customer.pickupondemand.jet.model.Config;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.QueryResult;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.notification.JETNotificationOpenedHandler;
import coid.customer.pickupondemand.jet.notification.JETNotificationReceivedHandler;
import coid.customer.pickupondemand.jet.request.BroadcastPickupRequest;
import coid.customer.pickupondemand.jet.request.ConfigRequest;
import coid.customer.pickupondemand.jet.request.PickupGetByCodeRequest;
import coid.customer.pickupondemand.jet.request.UnratedPickupListRequest;
import retrofit2.Response;

public class PickupOnDemandActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        JETNotificationReceivedHandler.INotificationReceivedListener,
        JETNotificationOpenedHandler.INotificationOpenedListener,
        BaseActivity.ILocationPermissionListener,
        PickupLocationConfirmationFragment.ICourierOrderListener
{
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ActionBarDrawerToggle mDrawerToggle;
    private View mNavigationViewHeader;

    private CourierOrderLoadingDialogFragment mCourierOrderLoadingDialogFragment;
    private ConfigRequest mConfigRequest;
    private PickupGetByCodeRequest mPickupGetByCodeRequest;
    private BroadcastPickupRequest mBroadcastPickupRequest;
    private UnratedPickupListRequest mUnratedPickupListRequest;
    private Config mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(coid.customer.pickupondemand.jet.R.layout.activity_pod_main);
        setView();
        setValue();
        getNavigator().showDefaultFragment();
        Utility.OneSignal.sendTags();
        JETApplication.notificationReceivedHandler.setOnReceivedListener(this);
        JETApplication.notificationOpenedHandler.setOnOpenedListener(this);

        if (isFirstTimeOpened())
        {
            Intent tutorialIntent = new Intent(this, TutorialActivity.class);
            startActivity(tutorialIntent);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setLocationPermissionListener(this);
        checkLocationPermission();
        requestUnratedPickup();
        requestConfig();
    }

    @Override
    protected void onDestroy()
    {
        JETApplication.notificationReceivedHandler.clearListener();
        JETApplication.notificationOpenedHandler.clearListener();
        if (mPickupGetByCodeRequest != null)
        {
            mPickupGetByCodeRequest.clear();
            mPickupGetByCodeRequest = null;
        }
        if (mConfigRequest != null)
        {
            mConfigRequest.clear();
            mConfigRequest = null;
        }
        if (mBroadcastPickupRequest != null)
        {
            mBroadcastPickupRequest.clear();
            mBroadcastPickupRequest = null;
        }
        if (mUnratedPickupListRequest != null)
        {
            mUnratedPickupListRequest.clear();
            mUnratedPickupListRequest = null;
        }

        if (mCourierOrderLoadingDialogFragment != null)
        {
            if (mCourierOrderLoadingDialogFragment.getDialog() != null && mCourierOrderLoadingDialogFragment.getDialog().isShowing())
                mCourierOrderLoadingDialogFragment.dismiss();

            mCourierOrderLoadingDialogFragment = null;
        }

        clearLocationPermissionListener();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(coid.customer.pickupondemand.jet.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Fragment fragment = getSupportFragmentManager().findFragmentById(getFragmentContainerId());
            if (fragment instanceof BaseFragment)
            {
                BaseFragment baseFragment = (BaseFragment) fragment;
                baseFragment.onBackPressed();
            }
            else
                getNavigator().back();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == coid.customer.pickupondemand.jet.R.id.nav_pickup_on_demand)
        {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(coid.customer.pickupondemand.jet.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public ActionBarDrawerToggle getDrawerToggle()
    {
        return mDrawerToggle;
    }

    @Override
    public Integer getFragmentContainerId()
    {
        return coid.customer.pickupondemand.jet.R.id.main_fragment_container;
    }

    @Override
    public Fragment getDefaultFragment()
    {
        if (getIntent() != null)
        {
            if (getIntent().hasExtra(JETNotificationOpenedHandler.NOTIFICATION_OPENED_PARAM))
            {
                String notificationData = getIntent().getStringExtra(JETNotificationOpenedHandler.NOTIFICATION_OPENED_PARAM);
                if (notificationData != null && !notificationData.isEmpty())
                    return PickupFragment.onNotificationOpenedInstance(notificationData);
            }
            else if (getIntent().hasExtra(PickupNotificationFragment.NOTIFICATION_FROM_LIST_PARAM))
            {
                String notificationData = getIntent().getStringExtra(PickupNotificationFragment.NOTIFICATION_FROM_LIST_PARAM);
                if (notificationData != null && !notificationData.isEmpty())
                    return PickupFragment.onNotificationFromListInstance(notificationData);
            }
        }

        return new PickupFragment();
    }

    @Override
    public void onNotificationOpenedAppInFocus(String notificationData)
    {
        getNavigator().popAllFragments();
        getNavigator().showFragment(PickupFragment.onNotificationOpenedInstance(notificationData));
    }

    @Override
    public void onNotificationReceived(NotificationPayload notificationPayload, Boolean isAppInFocus)
    {
        if (mBroadcastPickupRequest != null)
            mBroadcastPickupRequest.clearDelayedRequest();

        if (mCourierOrderLoadingDialogFragment != null)
        {
            if (mCourierOrderLoadingDialogFragment.getDialog() != null && mCourierOrderLoadingDialogFragment.getDialog().isShowing())
                mCourierOrderLoadingDialogFragment.dismiss();
            mCourierOrderLoadingDialogFragment = null;

            if (isAppInFocus && notificationPayload.getStatus().equals(Pickup.STATUS_ASSIGNED))
                getNavigator().popResumeToDefaultFragment();
        }

        if (isAppInFocus)
        {
            Log.d("JET_127", "APP IN FOCUS");
            switch (notificationPayload.getStatus())
            {
                case Pickup.STATUS_DRAFTED :
                    Log.d("JET_127","Drafted");
                    showDialogDraft();
                    break;
                case Pickup.STATUS_COMPLETED : requestPickupForRating(notificationPayload);
                    break;
                default:
                    Log.d("JET_127","Default");
                    break;
            }
        }
    }

    @Override
    public void onCreatePickupSuccess(final Pickup pickup)
    {
        long delay;
        if (mConfig != null)
            delay = (mConfig.getWaitingTime() + 10) * 1000;
        else
            delay = AppConfig.COURIER_ORDER_ATTEMPT_DELAY_IN_MILLIS;

        mBroadcastPickupRequest = new BroadcastPickupRequest(mContext, pickup, delay)
        {
            @Override
            protected void onStartOnUIThread()
            {
                if (mCourierOrderLoadingDialogFragment == null)
                    mCourierOrderLoadingDialogFragment = new CourierOrderLoadingDialogFragment();

                if (mCourierOrderLoadingDialogFragment.getDialog() == null || !mCourierOrderLoadingDialogFragment.getDialog().isShowing())
                    mCourierOrderLoadingDialogFragment.show(getSupportFragmentManager(), getClass().getSimpleName());
            }

            @Override
            protected void hideLoadingDialog()
            {
                if (mCourierOrderLoadingDialogFragment != null)
                    mCourierOrderLoadingDialogFragment.dismiss();

                mCourierOrderLoadingDialogFragment = null;
            }
        };

        mBroadcastPickupRequest.executeAsync();
    }

    @Override
    public void onLocationPermissionGranted()
    {

    }

    @Override
    public void onLocationPermissionDenied()
    {
        finish();
    }

    private void showDialogDraft()
    {
        Log.d("JET_127","Masuk ShowNotif");
        // setup the alert builder
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(PickupOnDemandActivity.this);

                builder.setTitle(R.string.pod_pickup_dialog_draft_title);
                builder.setMessage(R.string.pod_pickup_dialog_draft_content);

                // add a button
                builder.setPositiveButton(R.string.pod_ok, null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setView()
    {
        Toolbar toolbar = findViewById(coid.customer.pickupondemand.jet.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(coid.customer.pickupondemand.jet.R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, coid.customer.pickupondemand.jet.R.string.pod_navigation_drawer_open, coid.customer.pickupondemand.jet.R.string.pod_navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = findViewById(coid.customer.pickupondemand.jet.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        mNavigationViewHeader = navigationView.getHeaderView(0);
    }

    private void setValue()
    {
        UserProfile userProfile = DBQuery.getSingle(UserProfile.class);
        if (userProfile == null)
            return;

        TextView tv_username = mNavigationViewHeader.findViewById(coid.customer.pickupondemand.jet.R.id.tv_username);
        TextView tv_email = mNavigationViewHeader.findViewById(coid.customer.pickupondemand.jet.R.id.tv_email);
        tv_username.setText(userProfile.getUsername());
        tv_email.setText(userProfile.getEmail());
    }

    private void requestUnratedPickup()
    {
        mUnratedPickupListRequest = new UnratedPickupListRequest(this)
        {
            @Override
            protected void onStartOnUIThread(){}

            @Override
            protected void onSuccessOnUIThread(Response<QueryResult<Pickup>> response)
            {
                if (response.body().getResult().size() > 0)
                {
                    RatePickupDialogFragment dialog = RatePickupDialogFragment.newInstance(response.body().getResult().get(0));
                    dialog.show(getSupportFragmentManager(), RatePickupDialogFragment.class.getSimpleName());
                }
            }
        };
        mUnratedPickupListRequest.executeAsync();
    }

    private void requestConfig()
    {
        if (mConfig != null)
            return;

        mConfigRequest = new ConfigRequest(mContext)
        {
            @Override
            protected void showLoadingDialog(){}
            @Override
            protected void hideLoadingDialog(){}

            @Override
            protected void onSuccessOnUIThread(Response<Config> response)
            {
                mConfig = response.body();
            }
        };
        mConfigRequest.executeAsync();
    }

    private void requestPickupForRating(NotificationPayload notificationPayload)
    {
        if (notificationPayload == null)
            return;

        mPickupGetByCodeRequest = new PickupGetByCodeRequest(mContext, notificationPayload.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                super.onSuccessOnUIThread(response);
                RatePickupDialogFragment dialog = RatePickupDialogFragment.newInstance(response.body());
                dialog.show(getSupportFragmentManager(), RatePickupDialogFragment.class.getSimpleName());
            }
        };
        mPickupGetByCodeRequest.executeAsync();
    }

    private Boolean isFirstTimeOpened()
    {
        SharedPreferences pref = getSharedPreferences(AppConfig.JET_SHARED_PREFERENCES, MODE_PRIVATE);
        return pref.getBoolean(AppConfig.FIRST_TIME_OPENED_PARAM_KEY, true);
    }
}