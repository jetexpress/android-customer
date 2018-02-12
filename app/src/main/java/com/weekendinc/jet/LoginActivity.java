package com.weekendinc.jet;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;

import com.weekendinc.jet.fragments.LoginFragment;

import coid.customer.pickupondemand.jet.base.BaseFragment;

public class LoginActivity extends coid.customer.pickupondemand.jet.base.BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getNavigator().showDefaultFragment();
    }

    @Override
    public void onBackPressed()
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

    @Override
    public ActionBarDrawerToggle getDrawerToggle()
    {
        return null;
    }

    @Override
    public Integer getFragmentContainerId()
    {
        return R.id.login_fragment_container;
    }

    @Override
    public Fragment getDefaultFragment()
    {
        return new LoginFragment();
    }
}
