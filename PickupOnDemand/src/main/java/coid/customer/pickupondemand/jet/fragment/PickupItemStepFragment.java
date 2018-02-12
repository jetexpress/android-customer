package coid.customer.pickupondemand.jet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.model.PickupItem;

public abstract class PickupItemStepFragment extends BaseFragment implements Step
{
    private static final String PICKUP_ITEM_SAVED_INSTANCE = "pickupItemSavedInstance";

    private PickupItemStepListener mPickupItemStepListener;
    protected PickupItem mStepPickupItem;

    @Override
    public void onResume()
    {
        super.onResume();
        setValue(mStepPickupItem);
        Utility.UI.hideKeyboard(mView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PICKUP_ITEM_SAVED_INSTANCE, mStepPickupItem);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
        {
            PickupItem savedInstancePickup = savedInstanceState.getParcelable(PICKUP_ITEM_SAVED_INSTANCE);
            if (savedInstancePickup != null)
            {
                mStepPickupItem.update(savedInstancePickup);
                setValue(mStepPickupItem);
            }
        }
    }

    @Override
    public void onArrowBackPressed()
    {
        updatePickupItem();
        if (mPickupItemStepListener != null)
            mPickupItemStepListener.onStepArrowBackPressed();
    }

    @Override
    public void onError(@NonNull VerificationError error)
    {

    }

    @Override
    public void onSelected()
    {
        if (mPickupItemStepListener != null)
            mPickupItemStepListener.onStepSelected();
    }

    @Override
    public VerificationError verifyStep()
    {
        updatePickupItem();
        return null;
    }

    public void setPickupItemStepListener(PickupItemStepListener pickupItemStepListener)
    {
        mPickupItemStepListener = pickupItemStepListener;
        mStepPickupItem = mPickupItemStepListener.getPickupItem();
    }

    protected PickupItemStepListener getStepListener()
    {
        return mPickupItemStepListener;
    }

    protected abstract void setValue(PickupItem pickupItem);
    protected abstract void updatePickupItem();
    protected abstract Boolean isValid(PickupItem pickupItem);

    public interface PickupItemStepListener
    {
        Boolean isAddFirstItem();
        Boolean isAddNew();
        Boolean isEdit();
        Boolean isView();
        PickupItem getPickupItem();
        void onStepArrowBackPressed();
        void onStepSelected();
        void showStepper();
        void hideStepper();
        void onSave(PickupItem pickupItem);
    }
}
