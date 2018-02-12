package coid.customer.pickupondemand.jet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.fragment.PickupCurrentFragment;
import coid.customer.pickupondemand.jet.fragment.PickupHistoryFragment;

public class PickupFragmentPagerAdapter extends FragmentPagerAdapter
{
    private WeakReference<PickupCurrentFragment> mPickupCurrentFragment;
    private WeakReference<PickupHistoryFragment> mPickupHistoryFragment;

    public PickupFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                mPickupCurrentFragment = new WeakReference<>((PickupCurrentFragment) createdFragment);
                break;
            case 1:
                mPickupHistoryFragment = new WeakReference<>((PickupHistoryFragment) createdFragment);
                break;
        }
        return createdFragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0 : return Utility.Message.get(R.string.pod_pickup_current);
            case 1 : return Utility.Message.get(R.string.pod_pickup_history);
            default: break;
        }

        return null;
    }

    public PickupCurrentFragment getPickupCurrentFragment()
    {
        if (mPickupCurrentFragment != null)
            return mPickupCurrentFragment.get();
        return PickupCurrentFragment.newInstance(Utility.Message.get(R.string.pod_pickup_current));
    }

    public PickupHistoryFragment getPickupHistoryFragment()
    {
        if (mPickupHistoryFragment != null)
            return mPickupHistoryFragment.get();
        return PickupHistoryFragment.newInstance(Utility.Message.get(R.string.pod_pickup_history));
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return PickupCurrentFragment.newInstance(getPageTitle(position).toString());
            case 1 : return PickupHistoryFragment.newInstance(getPageTitle(position).toString());
            default: break;
        }

        return null;
    }
}
