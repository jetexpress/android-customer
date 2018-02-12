package coid.customer.pickupondemand.jet.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.adapter.AbstractStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.fragment.PickupItemStep1Fragment;
import coid.customer.pickupondemand.jet.fragment.PickupItemStep2Fragment;
import coid.customer.pickupondemand.jet.fragment.PickupItemStep3Fragment;
import coid.customer.pickupondemand.jet.fragment.PickupItemStepFragment;

public class PickupItemStepperAdapter extends AbstractFragmentStepAdapter
{
    private PickupItemStep1Fragment mPickupItemStep1Fragment;
    private PickupItemStep2Fragment mPickupItemStep2Fragment;
    private PickupItemStep3Fragment mPickupItemStep3Fragment;
    private Boolean mIsView;

    public PickupItemStepperAdapter(FragmentManager fm, Context context, Boolean isView)
    {
        super(fm, context);
        mIsView = isView;
        init();
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position)
    {
        switch (position) {
            case 0:
                return mPickupItemStep1Fragment;
            case 1:
                return mPickupItemStep2Fragment;
            case 2:
                return mPickupItemStep3Fragment;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position)
    {
        if (mIsView)
        {
            StepViewModel.Builder builder = new StepViewModel.Builder(context);
            int lastPosition = getCount() - 1;
            if (position >= lastPosition)
                builder.setEndButtonVisible(false);

            return builder.create();
        }

        return super.getViewModel(position);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    public List<PickupItemStepFragment> getPickupItemStepFragmentList()
    {
        List<PickupItemStepFragment> pickupItemStepFragmentList = new ArrayList<>();
        for (int i = 0; i < getCount(); i++)
        {
            Fragment fragment = getItem(i);
            if (fragment instanceof PickupItemStepFragment)
                pickupItemStepFragmentList.add((PickupItemStepFragment) fragment);
        }
        return pickupItemStepFragmentList;
    }

    private void init()
    {
        mPickupItemStep1Fragment = new PickupItemStep1Fragment();
        mPickupItemStep2Fragment = new PickupItemStep2Fragment();
        mPickupItemStep3Fragment = new PickupItemStep3Fragment();
    }

    public void destroy()
    {
        mPickupItemStep1Fragment = null;
        mPickupItemStep2Fragment = null;
        mPickupItemStep3Fragment = null;
    }

    public PickupItemStep1Fragment getPickupItemStep1Fragment()
    {
        return mPickupItemStep1Fragment;
    }
    public PickupItemStep2Fragment getPickupItemStep2Fragment()
    {
        return mPickupItemStep2Fragment;
    }
    public PickupItemStep3Fragment getPickupItemStep3Fragment()
    {
        return mPickupItemStep3Fragment;
    }
}
