package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.adapter.PaymentMethodAdapter;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.model.PaymentMethod;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.QueryData;
import coid.customer.pickupondemand.jet.request.PaymentMethodListRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodFragment extends BaseHasBasicLayoutFragment
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";

    private LinearLayout ll_bottom_container;
    private RelativeLayout rl_content_container, rl_add_pickup_image;
    private ListView list_view_payment_method;
    private EstimatedPriceView mEstimatedPriceBottomView;

    private List<PaymentMethod> mPaymentMethodList;
    private PaymentMethodAdapter mPaymentMethodAdapter;
    private PaymentMethodListRequest mPaymentMethodListRequest;
    private Pickup mPickup;

    public PaymentMethodFragment()
    {
        // Required empty public constructor
    }

    public static PaymentMethodFragment newInstance(Pickup pickup)
    {
        PaymentMethodFragment fragment = new PaymentMethodFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPaymentMethodAdapter = new PaymentMethodAdapter(mContext, new ArrayList<PaymentMethod>());
        if (getArguments() != null)
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
        mPaymentMethodListRequest = getPaymentMethodListRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_method, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(mPickup.getCode());
        if (mPaymentMethodListRequest != null && !mPaymentMethodListRequest.isSuccess())
            mPaymentMethodListRequest.executeAsync();
    }

    @Override
    protected View getBaseContentLayout()
    {
        return rl_content_container;
    }

    @Override
    protected void onRetry()
    {
        if (mPaymentMethodListRequest != null)
            mPaymentMethodListRequest.executeAsync();
    }

    private void setView()
    {
        rl_content_container = (RelativeLayout) findViewById(R.id.rl_content_container);
        mEstimatedPriceBottomView = new EstimatedPriceView(mContext);
        mEstimatedPriceBottomView.showPriceOnly();
        mEstimatedPriceBottomView.setEstimatedPrice(mPickup.getTotalFee());

        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        ll_bottom_container.addView(mEstimatedPriceBottomView, 0);

        rl_add_pickup_image = (RelativeLayout) findViewById(R.id.rl_add_pickup_image);
        list_view_payment_method = (ListView) findViewById(R.id.list_view_payment_method);
        list_view_payment_method.setAdapter(mPaymentMethodAdapter);
    }

    private void setEvent()
    {
        rl_add_pickup_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mPaymentMethodAdapter.isChecked())
                {
                    mPickup.setPaymentMethodCode(mPaymentMethodAdapter.getCheckedPaymentMethod().getCode());
                    getNavigator().showFragment(PickupImageFragment.newInstance(mPickup));
                }
                else
                    showToast(getString(R.string.pod_payment_method_not_available));
            }
        });
    }

    private PaymentMethodListRequest getPaymentMethodListRequest()
    {
        return new PaymentMethodListRequest(mContext)
        {
            @Override
            protected void showLoadingDialog()
            {
                showProgressBar();
            }

            @Override
            protected void hideLoadingDialog()
            {
                showRetry(R.string.pod_request_timed_out);
            }

            @Override
            protected void onSuccessOnUIThread(Response<QueryData<PaymentMethod>> response)
            {
                mPaymentMethodAdapter.updateData(response.body().getData());
                showContent(response.body().getData().size() > 0);
            }
        };
    }
}
