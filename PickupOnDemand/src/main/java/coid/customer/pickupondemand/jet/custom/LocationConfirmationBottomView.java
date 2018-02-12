package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.R;

public abstract class LocationConfirmationBottomView extends LinearLayout
{
    private LinearLayout ll_location_confirmation_container;
    private RelativeLayout rl_courier_order;
    private EstimatedPriceView mEstimatedPriceBottomView;

    public LocationConfirmationBottomView(Context context)
    {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_layout_location_confirmation_bottom_view, this, true);
        initialize();
    }

    private void initialize()
    {
        setView();
        setEvent();
    }

    private void setView()
    {
        mEstimatedPriceBottomView = new EstimatedPriceView(getContext());
        mEstimatedPriceBottomView.showPriceOnly();

        ll_location_confirmation_container = (LinearLayout) findViewById(R.id.ll_location_confirmation_container);
        ll_location_confirmation_container.addView(mEstimatedPriceBottomView, 0);

        rl_courier_order = (RelativeLayout) findViewById(R.id.rl_courier_order);
    }

    private void setEvent()
    {
        rl_courier_order.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onCourierOrderClicked(view);
            }
        });
    }

    public void setEstimatedPrice(Double price)
    {
        mEstimatedPriceBottomView.setEstimatedPrice(price);
    }

    public void setEstimatedPrice(String price)
    {
        mEstimatedPriceBottomView.setEstimatedPrice(price);
    }

    public abstract void onCourierOrderClicked(View view);
}
