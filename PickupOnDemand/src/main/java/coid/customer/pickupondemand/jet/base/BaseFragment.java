package coid.customer.pickupondemand.jet.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.Toast;

import coid.customer.pickupondemand.jet.Navigator;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;

public class BaseFragment extends Fragment
{
    public Context mContext;
    public View mView;
    public Fragment mFragment;

    private ProgressDialog mProgressDialog;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mFragment = this;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mDrawerToggle = getBaseActivity().getDrawerToggle();
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayBackEnabled(true);
    }

    @Override
    public void onDestroy() {
        hideLoadingDialog();
        Utility.UI.hideKeyboard(mView);
        super.onDestroy();
    }

    protected View findViewById(Integer id)
    {
        return mView.findViewById(id);
    }

    protected BaseActivity getBaseActivity()
    {
        return (BaseActivity) getActivity();
    }

    protected Navigator    getNavigator()
    {
        return getBaseActivity().getNavigator();
    }

    protected void setTitle(String title)
    {
        getActivity().setTitle(title);
    }

    protected void setTitle(@StringRes int stringResourceId)
    {
        getActivity().setTitle(stringResourceId);
    }

    protected void setDisplayBackEnabled(boolean isDisplayBackEnabled)
    {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null && mDrawerToggle != null) {
            mDrawerToggle.setDrawerIndicatorEnabled(!isDisplayBackEnabled);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayBackEnabled);
            mDrawerToggle.syncState();
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mDrawerToggle.isDrawerIndicatorEnabled()) {
                        onArrowBackPressed();
                    }
                }
            });
        }
    }

    public void onArrowBackPressed()
    {
        getNavigator().back();
        Utility.UI.hideKeyboard(mView);
    }

    public void onBackPressed()
    {
        getNavigator().back();
        Utility.UI.hideKeyboard(mView);
    }

    protected void hideDrawerToggle()
    {
        if (mDrawerToggle != null)
            mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    public void showLoadingDialog()
    {
        mProgressDialog = new ProgressDialog(mContext, R.style.ProgressDialog);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void showLoadingDialog(String message)
    {
        if (message == null) message = getString(R.string.pod_loading);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideLoadingDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showToast(String toastMessage)
    {
        Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
    }


}
