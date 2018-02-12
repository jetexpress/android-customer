package com.weekendinc.jet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.weekendinc.jet.fragments.CreditFragment;
import com.weekendinc.jet.fragments.DashboardFragment;
import com.weekendinc.jet.fragments.MyShipmentFragment;
import com.weekendinc.jet.fragments.PriceCheckingFragment;
import com.weekendinc.jet.fragments.SettingFragment;
import com.weekendinc.jet.fragments.TrackingFragment;
import com.weekendinc.jet.utils.SessionManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.activity.TutorialActivity;
import coid.customer.pickupondemand.jet.base.BaseHasNotificationActivity;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.fragment.UpdateRequiredDialogFragment;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.UpdateInfo;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.ModelLoader;
import coid.customer.pickupondemand.jet.notification.JETNotificationOpenedHandler;
import coid.customer.pickupondemand.jet.notification.JETNotificationReceivedHandler;
import coid.customer.pickupondemand.jet.request.UpdateInfoRequest;
import coid.customer.pickupondemand.jet.request.UserProfileRequest;
import retrofit2.Response;


public class MainActivity extends BaseHasNotificationActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        JETNotificationReceivedHandler.INotificationReceivedListener,
        JETNotificationOpenedHandler.INotificationOpenedListener
{
    public static int PICKUP_ACTIVITY_REQUEST_CODE = 1001;
    public static int LOGIN_ACTIVITY_REQUEST_CODE = 1002;
    private TextView profileName, bonusCredit;
    private UpdateInfoRequest mUpdateInfoRequest;
    private ActionBarDrawerToggle mDrawerToggle;
    private UserProfileRequest mUserProfileRequest;

    SessionManager sessionManager;
    String accessToken, tokenType, refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        JETApplication.notificationReceivedHandler.setOnReceivedListener(this);
        JETApplication.notificationOpenedHandler.setOnOpenedListener(this);

        if (getIntent() != null && getIntent().hasExtra(JETNotificationOpenedHandler.NOTIFICATION_OPENED_PARAM))
        {
            String notificationData = getIntent().getStringExtra(JETNotificationOpenedHandler.NOTIFICATION_OPENED_PARAM);
            handleNotificationData(notificationData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

       mUserProfileRequest = new UserProfileRequest();
       mUserProfileRequest.executeAsync();

        if (getSupportLoaderManager().getLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID) != null)
            getSupportLoaderManager().restartLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID, null, userProfileLoader);
        else
            getSupportLoaderManager().initLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID, null, userProfileLoader);

        checkUpdateRequired();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        JETApplication.notificationReceivedHandler.setOnReceivedListener(this);
        JETApplication.notificationOpenedHandler.setOnOpenedListener(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null)
        {
            Menu navigationMenu = navigationView.getMenu();
            if (navigationMenu != null)
            {
                MenuItem pickupHelpMenuItem = navigationMenu.findItem(R.id.action_pickup_help);
                if (pickupHelpMenuItem != null)
                {
                    if(isFirstTimeOpened())
                        pickupHelpMenuItem.setVisible(false);
                    else
                        pickupHelpMenuItem.setVisible(true);
                }
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        if (mUserProfileRequest != null)
        {
            mUserProfileRequest.clear();
            mUserProfileRequest = null;
        }
        if (mUpdateInfoRequest != null)
        {
            mUpdateInfoRequest.clear();
            mUpdateInfoRequest = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConfig.PICKUP_ACTIVITY_REQUEST_CODE)
        {
            String pickupExtraParam = data != null ? data.getStringExtra(AppConfig.MAIN_ACTIVITY_EXTRA_PARAM) : "";
            if (pickupExtraParam.equals(ApiConfig.REFRESH_GRANT_TYPE))
                sessionManager.logoutUser();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.action_shipment:
                getNavigator().showFragment(MyShipmentFragment.newInstance(0));
                break;
            case R.id.action_check_price:
                getNavigator().showFragment(PriceCheckingFragment.newInstance());
                break;
//            case R.id.action_credit:
//                getNavigator().showFragment(CreditFragment.newInstance());
//                break;
            case R.id.action_track:
                getNavigator().showFragment(TrackingFragment.newInstance());
                break;
            case R.id.action_pickup_on_demand:
                Intent intent = new Intent(getApplicationContext(), PickupOnDemandActivity.class);
                startActivityForResult(intent, AppConfig.PICKUP_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.action_address:
                Intent intent_1 = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_ADDRESS));
                startActivity(intent_1);
                break;
            case R.id.action_help:
                Intent intent_2 = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_HELP));
                startActivity(intent_2);
                break;
            case R.id.action_pickup_help:
                Intent tutorialIntent = new Intent(MainActivity.this, TutorialActivity.class);
                startActivity(tutorialIntent);
                break;
            case R.id.action_settings:
                getNavigator().showFragment(SettingFragment.newInstance());
                break;
            case R.id.action_about:
                Intent intent_3 = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_ABOUT));
                startActivity(intent_3);
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

//        NavigationView nav = findViewById(R.id.nav_view);
//        MenuItem homeMenuItem = nav.getMenu().findItem(R.id.action_home);
//        if (homeMenuItem != null)
//            homeMenuItem.setChecked(true);

        return false;
    }

    @Override
    public ActionBarDrawerToggle getDrawerToggle()
    {
        return mDrawerToggle;
    }

    @Override
    public Integer getFragmentContainerId()
    {
        return R.id.container;
    }

    @Override
    public Fragment getDefaultFragment()
    {
        return new DashboardFragment();
    }

    @Override
    public void onNotificationOpenedAppInFocus(String notificationData)
    {
        handleNotificationData(notificationData);
    }

    @Override
    public void onNotificationReceived(NotificationPayload notificationPayload, Boolean isAppInFocus)
    {
        if (isAppInFocus)
        {
            Log.d("JET_127", "APP IN FOCUS");
            switch (notificationPayload.getStatus())
            {
                case Pickup.STATUS_DRAFTED :
                    Log.d("JET_127","Drafted");
                    showDialogDraft();
                    break;
                default:
                    Log.d("JET_127","Default");
                    break;
            }
        }
    }

    private void setView()
    {
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn())
            sessionManager.checkLogin();

        HashMap<String, String> loginDetails = sessionManager.getLoginDetails();
        accessToken = loginDetails.get(SessionManager.ACCESS_TOKEN);
        tokenType = loginDetails.get(SessionManager.TOKEN_TYPE);
        refreshToken = loginDetails.get(SessionManager.REFRESH_TOKEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            profileName = navigationView.getHeaderView(0).findViewById(R.id.tv_drawer_profile_name);
//        profileName.setOnClickListener(HiddenPODOnClickListener);
            bonusCredit = navigationView.getHeaderView(0).findViewById(R.id.tv_drawer_bonus_credit);
        }

        getNavigator().showDefaultFragment();
    }

    private void handleNotificationData(String notificationData)
    {
        if (notificationData != null && !notificationData.isEmpty())
        {
            Intent intent = new Intent(getApplicationContext(), PickupOnDemandActivity.class);
            intent.putExtra(JETNotificationOpenedHandler.NOTIFICATION_OPENED_PARAM, notificationData);
            startActivityForResult(intent, AppConfig.PICKUP_ACTIVITY_REQUEST_CODE);
        }
    }

    private void showDialogDraft()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(coid.customer.pickupondemand.jet.R.string.pod_pickup_dialog_draft_title);
                builder.setMessage(coid.customer.pickupondemand.jet.R.string.pod_pickup_dialog_draft_content);

                // add a button
                builder.setPositiveButton(coid.customer.pickupondemand.jet.R.string.pod_ok, null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void updateDrawerProfile(UserProfile userProfile)
    {
        profileName.setText(userProfile.getFullName());

        try
        {
            bonusCredit.setText(formatIndonesianCurrency(userProfile.getWallet().getBonusCredit()));
            bonusCredit.setVisibility(View.GONE);
        }
        catch(Exception ex)
        {
            bonusCredit.setVisibility(View.GONE);
        }
    }

    public static String formatIndonesianCurrency(Object number){
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en","EN"));
        DecimalFormat decimalFormat = (DecimalFormat)nf;
        decimalFormat.applyPattern("###,###");
        return decimalFormat.format(number);
    }

    private Boolean isFirstTimeOpened()
    {
        SharedPreferences pref = getSharedPreferences(AppConfig.JET_SHARED_PREFERENCES, MODE_PRIVATE);
        return pref.getBoolean(AppConfig.FIRST_TIME_OPENED_PARAM_KEY, true);
    }


    private void checkUpdateRequired()
    {
        mUpdateInfoRequest = new UpdateInfoRequest(this, BuildConfig.VERSION_CODE)
        {
            @Override
            protected void onSuccessOnUIThread(Response<UpdateInfo> response)
            {
                if (response.body().getMessage() != null && !response.body().getMessage().isEmpty())
                {
                    UpdateRequiredDialogFragment dialog = UpdateRequiredDialogFragment.newInstance(response.body().isForceUpdate(), response.body().getMessage());
                    dialog.show(getSupportFragmentManager(), UpdateRequiredDialogFragment.class.getSimpleName());
                }
            }
        };
        mUpdateInfoRequest.executeAsync();
    }

    LoaderManager.LoaderCallbacks<List<UserProfile>> userProfileLoader = new LoaderManager.LoaderCallbacks<List<UserProfile>>()
    {
        @Override
        public Loader<List<UserProfile>> onCreateLoader(int id, Bundle args)
        {
            return new ModelLoader<>(getBaseContext(), UserProfile.class, true);
        }
        @Override
        public void onLoadFinished(Loader<List<UserProfile>> loader, List<UserProfile> data)
        {
            if (data.size() > 0)
                updateDrawerProfile(data.get(0));
        }
        @Override
        public void onLoaderReset(Loader<List<UserProfile>> loader){}
    };
}
