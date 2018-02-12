package com.weekendinc.jet.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weekendinc.jet.R;
import com.weekendinc.jet.adapter.ProductPriceItemAdapter;
import com.weekendinc.jet.model.PricingModel;
import com.weekendinc.jet.model.pojo.Price;
import com.weekendinc.jet.model.pojo.PricingOption;
import com.weekendinc.jet.utils.SubscriptionUtils;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.base.BaseItemSelectDialogFragment;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class PriceCheckingFragment extends BaseFragment
{
    private static final int ORIGIN_REQUEST_CODE = 1271;
    private static final int DESTINATION_REQUEST_CODE = 1272;

    final PricingModel pricingModel = PricingModel.getInstance();
    final private CompositeSubscription subscription = new CompositeSubscription();

    private Button btn_price_check;
    private EditText et_origin, et_destination;
    private ImageView img_weight_minus, img_weight_plus;
    private TextView tv_weight, tv_price_error_info, tv_origin_error_info, tv_destination_error_info;
    private ListView list_view_price_result;
    private ProgressBar progress_bar_price_result;

    private int mCurrentWeight;
    private ProductPriceItemAdapter mAdapter;

    public PriceCheckingFragment() {

    }

    public static PriceCheckingFragment newInstance() {
        return new PriceCheckingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentWeight = 1;
        mAdapter = new ProductPriceItemAdapter(getContext(), new ArrayList<PricingOption>());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_price_checking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
        setUIWeight();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(R.string.title_section_check_price);
    }

    @Override
    public void onDestroy()
    {
        subscription.unsubscribe();
        subscription.clear();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            CharSequence code = data.getStringExtra(BaseItemSelectDialogFragment.SELECTED_ITEM_CODE_ARGS_PARAM);
            CharSequence description = data.getStringExtra(BaseItemSelectDialogFragment.SELECTED_ITEM_DESCRIPTION_ARGS_PARAM);
            switch (requestCode)
            {
                case ORIGIN_REQUEST_CODE :
                    tv_origin_error_info.setVisibility(View.GONE);
                    et_origin.setText(description);
                    break;
                case DESTINATION_REQUEST_CODE:
                    tv_destination_error_info.setVisibility(View.GONE);
                    et_destination.setText(description);
                    break;
                default: break;
            }
        }
    }

    private void setView()
    {
        btn_price_check = (Button) findViewById(R.id.btn_price_check);
        et_origin = (EditText) findViewById(R.id.et_origin);
        et_destination = (EditText) findViewById(R.id.et_destination);
        img_weight_minus = (ImageView) findViewById(R.id.img_weight_minus);
        img_weight_plus = (ImageView) findViewById(R.id.img_weight_plus);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_price_error_info = (TextView) findViewById(R.id.tv_price_error_info);
        tv_origin_error_info = (TextView) findViewById(R.id.tv_origin_error_info);
        tv_destination_error_info = (TextView) findViewById(R.id.tv_destination_error_info);
        progress_bar_price_result = (ProgressBar) findViewById(R.id.progress_bar_price_result);
        list_view_price_result = (ListView) findViewById(R.id.list_view_price_result);

        list_view_price_result.setAdapter(mAdapter);
    }

    private void setEvent()
    {
        et_origin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (getFragmentManager() == null)
                    return;

                LocationItemSelectDialogFragment originDialog = new LocationItemSelectDialogFragment();
                originDialog.setTargetFragment(PriceCheckingFragment.this, ORIGIN_REQUEST_CODE);
                originDialog.show(getFragmentManager(), "origin");
            }
        });
        et_destination.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (getFragmentManager() == null)
                    return;

                LocationItemSelectDialogFragment originDialog = new LocationItemSelectDialogFragment();
                originDialog.setTargetFragment(PriceCheckingFragment.this, DESTINATION_REQUEST_CODE);
                originDialog.show(getFragmentManager(), "destination");
            }
        });
        btn_price_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isValid())
                    submitPrice();
            }
        });
        img_weight_minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mCurrentWeight > 1)
                    mCurrentWeight--;

                setUIWeight();
            }
        });
        img_weight_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentWeight++;
                setUIWeight();
            }
        });
    }

    private void setUIWeight()
    {
        tv_weight.setText(String.valueOf(mCurrentWeight));
    }

    private void showResult()
    {
        list_view_price_result.setVisibility(View.VISIBLE);
        tv_price_error_info.setVisibility(View.GONE);
        progress_bar_price_result.setVisibility(View.GONE);
    }

    private void showErrorInfo(int stringResourceId)
    {
        String errorInfo = Utility.Message.get(stringResourceId);
        tv_price_error_info.setText(errorInfo);

        list_view_price_result.setVisibility(View.GONE);
        tv_price_error_info.setVisibility(View.VISIBLE);
        progress_bar_price_result.setVisibility(View.GONE);
    }

    private void showProgressBar()
    {
        list_view_price_result.setVisibility(View.GONE);
        tv_price_error_info.setVisibility(View.GONE);
        progress_bar_price_result.setVisibility(View.VISIBLE);
    }

    private boolean isValid()
    {
        int errorCount = 0;
        String origin = et_origin.getText().toString().trim();
        String destination = et_destination.getText().toString().trim();

        if (origin.isEmpty())
        {
            errorCount++;
            tv_origin_error_info.setVisibility(View.VISIBLE);
            et_origin.requestFocus();
        }

        if (destination.isEmpty())
        {
            errorCount++;
            tv_destination_error_info.setVisibility(View.VISIBLE);
            et_destination.requestFocus();
        }

        return errorCount <=0 ;
    }

    private void submitPrice()
    {
        showProgressBar();

        String origin = et_origin.getText().toString().trim();
        String destination = et_destination.getText().toString().trim();
        String weight = tv_weight.getText().toString().trim();

        if (!origin.isEmpty() && !destination.isEmpty() && !weight.isEmpty())
        {
            subscription.add(
                    SubscriptionUtils.subscribing(
                            pricingModel.getPrice(origin.trim(), destination.trim(), Integer.parseInt(weight)),
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    showErrorInfo(R.string.request_timed_out);
                                }
                            },
                            new Action1<List<Price>>() {
                                @Override
                                public void call(List<Price> prices) {
                                    if (prices.size() <= 0)
                                        showErrorInfo(R.string.price_check_result_not_found);
                                    else
                                    {
                                        List<PricingOption> pricingOption = getFilteredPricingOptionList(prices.get(0).getPricingOptions());
                                        mAdapter.updateData(pricingOption);
                                        showResult();
                                    }
                                }
                            }
                    )
            );
        }
    }

    private List<PricingOption> getFilteredPricingOptionList(List<PricingOption> pricingOptionList)
    {
        List<PricingOption> filteredPricingOptionList = new ArrayList<>();

        for (PricingOption pricingOption : pricingOptionList)
        {
            if (pricingOption.getProductCode().equalsIgnoreCase("sds"))
                continue;
            filteredPricingOptionList.add(pricingOption);
        }

        return filteredPricingOptionList;
    }
}
