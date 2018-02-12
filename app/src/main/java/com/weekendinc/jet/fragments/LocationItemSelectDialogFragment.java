package com.weekendinc.jet.fragments;

import android.os.Bundle;

import com.weekendinc.jet.R;
import com.weekendinc.jet.model.PricingModel;
import com.weekendinc.jet.model.pojo.Location;
import com.weekendinc.jet.utils.SubscriptionUtils;

import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseItemSelectDialogFragment;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class LocationItemSelectDialogFragment extends BaseItemSelectDialogFragment<Location>
{
    private String mKeyword;
    private PricingModel mPricingModel;
    private CompositeSubscription mSubscription;

    public LocationItemSelectDialogFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPricingModel = new PricingModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        showFilter();
    }

    @Override
    protected String getTitle()
    {
        return Utility.Message.get(R.string.price_check_choose_location);
    }

    @Override
    protected void onRunFilter(String keyword)
    {
        mKeyword = keyword;
        requestLocationList();
    }

    @Override
    protected void onRetry()
    {
        requestLocationList();
    }

    private void requestLocationList()
    {
        mSubscription.clear();
        if (mKeyword.isEmpty())
        {
            clearData();
            return;
        }

        showFilterProgressBar();
        mSubscription.add(
                SubscriptionUtils.subscribing(
                        mPricingModel.getLocations(mKeyword),
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                showRetry(R.string.request_timed_out);
                                hideFilterProgressBar();
                            }
                        },
                        new Action1<List<Location>>() {
                            @Override
                            public void call(List<Location> locationList) {
                                updateData(locationList);
                                showContent(locationList.size() > 0);
                                hideFilterProgressBar();
                            }
                        }
                )
        );
    }
}
