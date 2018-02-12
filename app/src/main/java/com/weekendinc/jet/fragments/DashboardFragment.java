package com.weekendinc.jet.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.weekendinc.jet.R;
import com.weekendinc.jet.TheConstant;
import com.weekendinc.jet.utils.SessionManager;

import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.activity.TutorialActivity;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.config.AppConfig;

public class DashboardFragment extends BaseFragment implements View.OnClickListener
{
    private LinearLayout ll_pickup;
    private RelativeLayout rl_price_check, rl_tracking, rl_history, rl_outlet_list,
            rl_credit, rl_help, rl_setting, rl_about;
    SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(R.string.title_section_home);
        setDisplayBackEnabled(false);
        sessionManager = new SessionManager(mContext);


        if (sessionManager.isLoggedIn())
        {
            if(isFirstTimeOpened())
            {
                Intent tutorialIntent = new Intent(getContext(), TutorialActivity.class);
                mContext.startActivity(tutorialIntent);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == ll_pickup)
        {
            Intent intent = new Intent(getContext(), PickupOnDemandActivity.class);
            startActivityForResult(intent, AppConfig.PICKUP_ACTIVITY_REQUEST_CODE);
        }
        else if (v == rl_price_check)
            getNavigator().showFragment(PriceCheckingFragment.newInstance());
        else if (v == rl_tracking)
            getNavigator().showFragment(TrackingFragment.newInstance());
        else if (v == rl_history)
            getNavigator().showFragment(MyShipmentFragment.newInstance(1));
        else if (v == rl_outlet_list)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_ADDRESS));
            startActivity(intent);
        }
        else if (v == rl_credit)
            getNavigator().showFragment(CreditFragment.newInstance());
        else if (v == rl_help)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_HELP));
            startActivity(intent);
        }
        else if (v == rl_setting)
            getNavigator().showFragment(SettingFragment.newInstance());
        else if (v == rl_about)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_ABOUT));
            startActivity(intent);
        }
    }

    private void setView()
    {
        ll_pickup = (LinearLayout) findViewById(R.id.ll_pickup);
        rl_price_check = (RelativeLayout) findViewById(R.id.rl_price_check);
        rl_tracking = (RelativeLayout) findViewById(R.id.rl_tracking);
        rl_history = (RelativeLayout) findViewById(R.id.rl_history);
        rl_outlet_list = (RelativeLayout) findViewById(R.id.rl_outlet_list);
        rl_credit = (RelativeLayout) findViewById(R.id.rl_credit);
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
    }

    private void setEvent()
    {
        ll_pickup.setOnClickListener(this);
        rl_price_check.setOnClickListener(this);
        rl_tracking.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        rl_outlet_list.setOnClickListener(this);
        rl_credit.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }

    private Boolean isFirstTimeOpened()
    {
        SharedPreferences pref = mContext.getSharedPreferences(AppConfig.JET_SHARED_PREFERENCES, mContext.MODE_PRIVATE);
        return pref.getBoolean(AppConfig.FIRST_TIME_OPENED_PARAM_KEY, true);
    }
}
