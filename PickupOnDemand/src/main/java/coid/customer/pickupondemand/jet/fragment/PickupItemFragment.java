package coid.customer.pickupondemand.jet.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.stepstone.stepper.StepperLayout;

import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.adapter.PickupItemStepperAdapter;
import coid.customer.pickupondemand.jet.base.BaseActivity;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.custom.ConfirmationDialog;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.model.PackagingItem;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.model.PickupItemSimulation;
import coid.customer.pickupondemand.jet.model.Product;
import coid.customer.pickupondemand.jet.model.QueryData;
import coid.customer.pickupondemand.jet.request.PackagingItemListRequest;
import coid.customer.pickupondemand.jet.request.PickupDraftCreateRequest;
import coid.customer.pickupondemand.jet.request.PickupItemCreateRequest;
import coid.customer.pickupondemand.jet.request.PickupItemUpdateRequest;
import coid.customer.pickupondemand.jet.request.ProductListRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupItemFragment extends BaseFragment implements PickupItemStepFragment.PickupItemStepListener
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";
    private static final String PICKUP_ITEM_ARGS_PARAM = "PickupItemParam";

    private GoogleApiClient mGoogleApiClient;
    private Pickup mPickup;
    private PickupItem mPickupItem;
    private PickupItem mTempPickupItem;
    private PickupItemStepperAdapter mPickupItemStepperAdapter;
    private PickupItemFragmentListener mPickupItemFragmentListener;
    private PickupItemFragmentDataListener mPickupItemFragmentDataListener;
    private ProductListRequest mProductListRequest;
    private PackagingItemListRequest mPackagingItemListRequest;
    private PickupDraftCreateRequest mPickupDraftCreateRequest;
    private PickupItemCreateRequest mFirstPickupItemCreateRequest;
    private PickupItemCreateRequest mPickupItemCreateRequest;
    private PickupItemUpdateRequest mPickupItemUpdateRequest;

    private StepperLayout stepper_layout_pickup_item;
    private EstimatedPriceView mEstimatedPriceBottomView;

    public PickupItemFragment()
    {
        // Required empty public constructor
    }

    public static PickupItemFragment newAddInstance(Pickup pickup)
    {
        PickupItemFragment fragment = new PickupItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        fragment.setArguments(args);
        return fragment;
    }

    public static PickupItemFragment newEditInstance(Pickup pickup, PickupItem pickupItem)
    {
        PickupItemFragment fragment = new PickupItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        args.putParcelable(PICKUP_ITEM_ARGS_PARAM, pickupItem);
        fragment.setArguments(args);
        return fragment;
    }

    public static PickupItemFragment newViewInstance(PickupItem pickupItem)
    {
        PickupItemFragment fragment = new PickupItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ITEM_ARGS_PARAM, pickupItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
            mPickupItem = getArguments().getParcelable(PICKUP_ITEM_ARGS_PARAM);
        }

        if (getTargetFragment() instanceof PickupItemFragmentListener)
            mPickupItemFragmentListener = (PickupItemFragmentListener) getTargetFragment();

        mProductListRequest = getProductListRequest();
        mPackagingItemListRequest = getPackagingItemListRequest();

        mTempPickupItem = new PickupItem();
        if (mPickupItem != null)
            mTempPickupItem.update(mPickupItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        stepper_layout_pickup_item = (StepperLayout) findViewById(R.id.stepper_layout_pickup_item);
        if (mEstimatedPriceBottomView == null)
        {
//            mEstimatedPriceBottomView = LayoutInflater.from(mContext).inflate(R.layout.custom_layout_pickup_estimated_price, stepper_layout_pickup_item, false);
            mEstimatedPriceBottomView = new EstimatedPriceView(mContext)
            {
                @Override
                public void onRefreshClicked()
                {
                    PickupItemSimulation pickupItemSimulation = getPickupItemSimulation();
                    if (pickupItemSimulation != null)
                        requestEstimatedPrice(pickupItemSimulation);
                    else
                    {
                        String title = getString(R.string.pod_pickup_item_how_to_use_estimated_price_title);
                        String message = getString(R.string.pod_pickup_item_how_to_use_estimated_price_message);
                        final ConfirmationDialog confirmationDialog = new ConfirmationDialog(getContext(), title, message)
                        {
                            @Override
                            public void onOKClicked()
                            {
                                dismiss();

                                if (mPickupItemStepperAdapter.getPickupItemStep1Fragment() == null)
                                    return;

                                if (stepper_layout_pickup_item.getCurrentStepPosition() > 0)
                                    stepper_layout_pickup_item.setCurrentStepPosition(0);

                                mPickupItemStepperAdapter.getPickupItemStep1Fragment().showOptionalContent();
                                mPickupItemStepperAdapter.getPickupItemStep1Fragment().chooseAddressMapLocation();
                            }
                        };
                        confirmationDialog.setCancelable(false);
                        confirmationDialog.setOnShowListener(new DialogInterface.OnShowListener()
                        {
                            @Override
                            public void onShow(DialogInterface dialog)
                            {
                                Button buttonPositive = confirmationDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                buttonPositive.setTextColor(Color.BLACK);
                                Button buttonNegative = confirmationDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                                buttonNegative.setEnabled(false);
                            }
                        });
                        confirmationDialog.show();
                    }
                }
            };
            stepper_layout_pickup_item.addView(mEstimatedPriceBottomView, 2);
        }
        if (isView())
            mEstimatedPriceBottomView.showPriceOnly();

        mPickupItemStepperAdapter = new PickupItemStepperAdapter(getChildFragmentManager(), mContext, isView());
        stepper_layout_pickup_item.setAdapter(mPickupItemStepperAdapter);
        if (mPickupItemStepperAdapter.getPickupItemStep2Fragment() != null)
            mPickupItemFragmentDataListener = mPickupItemStepperAdapter.getPickupItemStep2Fragment();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!isView())
        {
            mProductListRequest.executeAsync();
            mPackagingItemListRequest.executeAsync();
        }
        mEstimatedPriceBottomView.setEstimatedPrice(mPickupItem != null ? mPickupItem.getTotalFee() : 0D);
    }

    @Override
    public void onStop()
    {
        mProductListRequest.cancel();
        mPackagingItemListRequest.cancel();
        mEstimatedPriceBottomView.cancelRequest();
        if (mPickupDraftCreateRequest != null)
            mPickupDraftCreateRequest.cancel();

        if (mFirstPickupItemCreateRequest != null)
            mFirstPickupItemCreateRequest.cancel();

        if (mPickupItemCreateRequest != null)
            mPickupItemCreateRequest.cancel();

        if (mPickupItemUpdateRequest != null)
            mPickupItemUpdateRequest.cancel();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        mProductListRequest.clear();
        mProductListRequest = null;
        mPackagingItemListRequest.clear();
        mPackagingItemListRequest = null;

        mEstimatedPriceBottomView.clear();
        mEstimatedPriceBottomView = null;

        if (mPickupDraftCreateRequest != null)
        {
            mPickupDraftCreateRequest.clear();
            mPickupDraftCreateRequest = null;
        }

        if (mFirstPickupItemCreateRequest != null)
        {
            mFirstPickupItemCreateRequest.clear();
            mFirstPickupItemCreateRequest = null;
        }

        if (mPickupItemCreateRequest != null)
        {
            mPickupItemCreateRequest.clear();
            mPickupItemCreateRequest = null;
        }

        if (mPickupItemUpdateRequest != null)
        {
            mPickupItemUpdateRequest.clear();
            mPickupItemUpdateRequest = null;
        }

        mPickupItemStepperAdapter.destroy();
        mPickupItemStepperAdapter = null;
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof BaseActivity)
            mGoogleApiClient = ((BaseActivity) context).getGoogleApiClient();
    }

    @Override
    public void onBackPressed()
    {
        stepBack();
    }

    @Override
    public void onArrowBackPressed()
    {
        stepBack();
    }

    @Override
    public void onAttachFragment(Fragment childFragment)
    {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof PickupItemStepFragment)
            ((PickupItemStepFragment) childFragment).setPickupItemStepListener(this);
    }

    @Override
    public Boolean isAddFirstItem()
    {
        return mPickup == null && mPickupItem == null;
    }

    @Override
    public Boolean isAddNew()
    {
        return mPickup != null && mPickupItem == null;
    }

    @Override
    public Boolean isEdit()
    {
        return mPickup != null && mPickupItem != null;
    }

    @Override
    public Boolean isView()
    {
        return mPickup == null && mPickupItem != null;
    }

    @Override
    public PickupItem getPickupItem()
    {
        return mTempPickupItem;
    }

    @Override
    public void onStepArrowBackPressed()
    {
        stepBack();
    }

    @Override
    public void onStepSelected()
    {
        PickupItemSimulation pickupItemSimulation = getPickupItemSimulation();
        if (pickupItemSimulation != null)
            mEstimatedPriceBottomView.requestEstimatedPrice(pickupItemSimulation);
    }

    @Override
    public void showStepper()
    {
        stepper_layout_pickup_item.setShowBottomNavigation(true);
        mEstimatedPriceBottomView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStepper()
    {
        stepper_layout_pickup_item.setShowBottomNavigation(false);
        mEstimatedPriceBottomView.setVisibility(View.GONE);
    }

    @Override
    public void onSave(PickupItem pickupItem)
    {
        int errorPosition = -1;

        List<PickupItemStepFragment> pickupItemStepFragmentList = mPickupItemStepperAdapter.getPickupItemStepFragmentList();
        for (int i = mPickupItemStepperAdapter.getCount() - 1; i >= 0; i--)
        {
            PickupItemStepFragment fragment = pickupItemStepFragmentList.get(i);
            if (!fragment.isValid(mTempPickupItem))
                errorPosition = i;
        }

        if (errorPosition >= 0)
        {
            stepper_layout_pickup_item.setCurrentStepPosition(errorPosition);
            pickupItemStepFragmentList.get(errorPosition).isValid(mTempPickupItem);
            return;
        }

        if (isAddFirstItem())
        {
            Log.d("PICKUP_ITEM_127", "ADD FIRST ITEM");
            mPickupDraftCreateRequest = getPickupDraftCreateRequest();
            mPickupDraftCreateRequest.executeAsync();
        }
        else if (isAddNew())
        {
            Log.d("PICKUP_ITEM_127", "ADD NEW ITEM");
            mPickupItemCreateRequest = getPickupItemCreateRequest();
            mPickupItemCreateRequest.executeAsync();
        }
        else if (isEdit())
        {
            Log.d("PICKUP_ITEM_127", "EDIT ITEM");
            mPickupItemUpdateRequest = getPickupItemUpdateRequest();
            mPickupItemUpdateRequest.executeAsync();
        }
    }

    public void stepBack()
    {
        Fragment childFragment = getChildFragmentManager().findFragmentById(R.id.fl_step_container);
        if (childFragment != null)
        {
            getChildFragmentManager().popBackStack();
            showStepper();
            return;
        }

        int currentPosition = stepper_layout_pickup_item.getCurrentStepPosition();
        if (currentPosition > 0)
        {
            if (mPickupItemStepperAdapter.getItem(currentPosition) instanceof PickupItemStepFragment)
                ((PickupItemStepFragment) mPickupItemStepperAdapter.getItem(currentPosition)).updatePickupItem();
            stepper_layout_pickup_item.onBackClicked();
        }
        else
            getNavigator().back();
    }

    private ProductListRequest getProductListRequest()
    {
        return new ProductListRequest(mContext)
        {
            @Override
            protected void onStartOnUIThread()
            {
                if (mPickupItemFragmentDataListener != null)
                    mPickupItemFragmentDataListener.onGetProductListStart();
            }

            @Override
            protected void onSuccessOnUIThread(Response<QueryData<Product>> response)
            {
                super.onSuccessOnUIThread(response);
                if (mPickupItemFragmentDataListener != null)
                    mPickupItemFragmentDataListener.onGetProductListSuccess(response.body().getData());
            }

            @Override
            protected void hideLoadingDialog()
            {
                if (mPickupItemFragmentDataListener != null)
                    mPickupItemFragmentDataListener.hideProductListProgress();
            }
        };
    }

    private PackagingItemListRequest getPackagingItemListRequest()
    {
        return new PackagingItemListRequest(mContext)
        {
            @Override
            protected void onStartOnUIThread()
            {
                if (mPickupItemFragmentDataListener != null)
                    mPickupItemFragmentDataListener.onGetPackagingItemListStart();
            }

            @Override
            protected void onSuccessOnUIThread(Response<QueryData<PackagingItem>> response)
            {
                super.onSuccessOnUIThread(response);
                if (mPickupItemFragmentDataListener != null)
                    mPickupItemFragmentDataListener.onGetPackagingItemListSuccess(response.body().getData());
            }

            @Override
            protected void hideLoadingDialog()
            {
                if (mPickupItemFragmentDataListener != null)
                    mPickupItemFragmentDataListener.hidePackagingItemListProgress();
            }
        };
    }

    private PickupItemSimulation getPickupItemSimulation()
    {
        List<PickupItemStepFragment> pickupItemStepFragmentList = mPickupItemStepperAdapter.getPickupItemStepFragmentList();
        for (PickupItemStepFragment fragment: pickupItemStepFragmentList)
        {
            fragment.updatePickupItem();
        }

        if (mTempPickupItem.getConsigneeLatitude() == null
                || mTempPickupItem.getConsigneeLongitude() == null
                || (mTempPickupItem.getConsigneeLatitude() == 0 && mTempPickupItem.getConsigneeLongitude() == 0))
            return null;

        if (isView())
            return null;

        PickupItemSimulation pickupItemSimulation = new PickupItemSimulation(mTempPickupItem);
        if (!isAddFirstItem())
            pickupItemSimulation.setBranchCode(mPickup.getBranchCode());
        else
        {
            Location currentLocation = Utility.Location.getCurrentLocationByFused(mGoogleApiClient);
            if (currentLocation != null)
            {
                pickupItemSimulation.setLatitude(currentLocation.getLatitude());
                pickupItemSimulation.setLongitude(currentLocation.getLongitude());
            }
        }
        return pickupItemSimulation;
    }

    private PickupDraftCreateRequest getPickupDraftCreateRequest()
    {
        Double lat;
        Double lng;

        Location location = Utility.Location.getCurrentLocationByFused(mGoogleApiClient);
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
        else
        {
            lat = null;
            lng = null;
        }

        return new PickupDraftCreateRequest(mContext, lat, lng)
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                if (response.body() == null)
                {
                    Log.d("PICKUP_ITEM_127", "CREATE PICKUP DRAFT SUCCESS = NULL");
                    return;
                }

                mPickup = response.body();
                if (mPickupItemFragmentListener != null)
                    mPickupItemFragmentListener.onPickupCreated(response.body());

                mFirstPickupItemCreateRequest = getFirstPickupItemCreateRequest(response.body());
                mFirstPickupItemCreateRequest.executeAsync();
            }
        };
    }

    private PickupItemCreateRequest getFirstPickupItemCreateRequest(Pickup pickup)
    {
        return new PickupItemCreateRequest(mContext, pickup.getCode(), mTempPickupItem)
        {
            @Override
            protected void onStartOnUIThread(){}
            @Override
            protected void onSuccessOnUIThread(Response<PickupItem> response)
            {
                super.onSuccessOnUIThread(response);

                if (mPickupItemFragmentListener != null)
                    mPickupItemFragmentListener.onPickupItemCreated(response.body());
                getNavigator().back();
            }
        };
    }

    private PickupItemCreateRequest getPickupItemCreateRequest()
    {
        return new PickupItemCreateRequest(mContext, mPickup.getCode(), mTempPickupItem)
        {
            @Override
            protected void onSuccessOnUIThread(Response<PickupItem> response)
            {
                super.onSuccessOnUIThread(response);

                if (mPickupItemFragmentListener != null)
                    mPickupItemFragmentListener.onPickupItemCreated(response.body());
                getNavigator().back();
            }
        };
    }

    private PickupItemUpdateRequest getPickupItemUpdateRequest()
    {
        return new PickupItemUpdateRequest(mContext, mPickup.getCode(), mTempPickupItem)
        {
            @Override
            protected void onSuccessOnUIThread(Response<PickupItem> response)
            {
                super.onSuccessOnUIThread(response);

                mPickupItem.update(response.body());
                if (mPickupItemFragmentListener != null)
                    mPickupItemFragmentListener.onPickupItemUpdated(mPickupItem);
                getNavigator().back();
            }
        };
    }

    public interface PickupItemFragmentListener
    {
        void onPickupCreated(Pickup pickup);
        void onPickupItemCreated(PickupItem pickupItem);
        void onPickupItemUpdated(PickupItem pickupItem);
    }

    public interface PickupItemFragmentDataListener
    {
        void onGetProductListStart();
        void onGetProductListSuccess(List<Product> productList);
        void hideProductListProgress();
        void onGetPackagingItemListStart();
        void onGetPackagingItemListSuccess(List<PackagingItem> packagingItemList);
        void hidePackagingItemListProgress();
    }
}
