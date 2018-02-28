package com.weekendinc.jet.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weekendinc.jet.ProfileSettingActivity;
import com.weekendinc.jet.R;
import com.weekendinc.jet.TheConstant;
import com.weekendinc.jet.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.ModelLoader;

public class SettingFragment extends BaseFragment {

    private SessionManager mSessionManager;

    private TextView tv_name, tv_profile_setting, tv_outlet, tv_help, tv_about, tv_sign_out;
    private ProgressBar progress_bar_name;

    public SettingFragment() {

    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(R.string.title_section_setting);
        if (getLoaderManager().getLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID) != null)
            getLoaderManager().restartLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID, null, userProfileLoader);
        else
            getLoaderManager().initLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID, null, userProfileLoader);
    }

    private void setView()
    {
        progress_bar_name = (ProgressBar) findViewById(R.id.progress_bar_name);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_profile_setting = (TextView) findViewById(R.id.tv_profile_setting);
        tv_outlet = (TextView) findViewById(R.id.tv_outlet);
        tv_help = (TextView) findViewById(R.id.tv_help);
        tv_about = (TextView) findViewById(R.id.tv_about);
        tv_sign_out = (TextView) findViewById(R.id.tv_sign_out);
    }

    private void setEvent()
    {
        tv_profile_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });
        tv_outlet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_ADDRESS));
                startActivity(intent);
            }
        });
        tv_help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_HELP));
                startActivity(intent);
            }
        });
        tv_about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TheConstant.URL_ABOUT));
                startActivity(intent);
            }
        });
        tv_sign_out.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getBaseActivity().getNavigator().popResumeToDefaultFragment();
                mSessionManager.logoutUser();
            }
        });
    }

    LoaderManager.LoaderCallbacks<List<UserProfile>> userProfileLoader = new LoaderManager.LoaderCallbacks<List<UserProfile>>()
    {
        @Override
        public Loader<List<UserProfile>> onCreateLoader(int id, Bundle args)
        {
            return new ModelLoader<>(getContext(), UserProfile.class, true);
        }
        @Override
        public void onLoadFinished(Loader<List<UserProfile>> loader, List<UserProfile> data)
        {
            if (data.size() > 0)
            {
                tv_name.setText(data.get(0).getFullName());
            }
            progress_bar_name.setVisibility(View.GONE);
        }
        @Override
        public void onLoaderReset(Loader<List<UserProfile>> loader){}
    };
}
