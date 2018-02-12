package coid.customer.pickupondemand.jet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import coid.customer.pickupondemand.jet.base.BaseActivity;

public class Navigator
{
    private Context mContext;
    private BaseActivity mActivity;
    private Integer mFragmentContainerId;

    public Navigator (Context context, Integer fragmentContainerId)
    {
        mContext = context;
        if (mContext instanceof BaseActivity)
            mActivity = (BaseActivity) mContext;
        mFragmentContainerId = fragmentContainerId;
    }

    public void back()
    {
        try
        {
            if (getFragmentManager().getBackStackEntryCount() > 1)
                getFragmentManager().popBackStack();
            else
                mActivity.finish();
        }
        catch (Exception ex)
        {
            popResumeToDefaultFragment();
        }
    }

    public void showFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(mFragmentContainerId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment, FragmentManager fragmentManager, int containerId)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showDialogFragment(DialogFragment fragment, String tag)
    {
        fragment.show(getFragmentManager(), tag);
    }

    public void showDefaultFragment()
    {
        Fragment fragment = getFragmentManager().findFragmentById(mFragmentContainerId);
        if (fragment == null) showFragment(mActivity.getDefaultFragment());
    }

    public void popResumeToDefaultFragment()
    {
        try
        {
            FragmentManager fm = getFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                fm.popBackStack();
            }
        }
        catch (Exception ex)
        {
            showFragment(mActivity.getDefaultFragment());
        }
    }

    public void popAllFragments()
    {
        try
        {
            FragmentManager fm = getFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }
        }
        catch (Exception ex)
        {
            showFragment(mActivity.getDefaultFragment());
        }
    }

    public void navigateToActivity(Class activityClass)
    {
        final Intent intent = new Intent(mContext, activityClass);
        mContext.startActivity(intent);
    }

    private FragmentManager getFragmentManager() {

        if (mContext instanceof FragmentActivity) {
            return ((FragmentActivity) mContext).getSupportFragmentManager();
        }
        throw new IllegalStateException(
                "The context being used is not an instance of FragmentActivity.");
    }

}
