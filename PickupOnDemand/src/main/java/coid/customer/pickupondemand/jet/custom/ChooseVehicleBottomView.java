package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import coid.customer.pickupondemand.jet.R;

public abstract class ChooseVehicleBottomView extends LinearLayout
{
    private LinearLayout ll_vehicle_container;
    private RelativeLayout rl_bike_container, rl_car_container;
    private ImageView img_bike, img_car;
    private EstimatedPriceView mEstimatedPriceBottomView;

    public ChooseVehicleBottomView(Context context)
    {
        super(context);
        LayoutInflater  inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_layout_choose_vehicle_bottom_view, this, true);
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

        ll_vehicle_container = (LinearLayout) findViewById(R.id.ll_vehicle_container);
        ll_vehicle_container.addView(mEstimatedPriceBottomView, 0);

        rl_bike_container = (RelativeLayout) findViewById(R.id.rl_bike_container);
        rl_car_container = (RelativeLayout) findViewById(R.id.rl_car_container);
        img_bike = (ImageView) findViewById(R.id.img_bike);
        img_car = (ImageView) findViewById(R.id.img_car);
    }

    private void setEvent()
    {
        View.OnClickListener enableBikeOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setBikeEnabled();
                onBikeEnabled(view);
            }
        };

        View.OnClickListener enableCarOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setCarEnabled();
                onCarEnabled(view);
            }
        };

        rl_bike_container.setOnClickListener(enableBikeOnClickListener);
        rl_car_container.setOnClickListener(enableCarOnClickListener);
        img_bike.setOnClickListener(enableBikeOnClickListener);
        img_car.setOnClickListener(enableCarOnClickListener);
    }

    public void setEstimatedPrice(Double price)
    {
        mEstimatedPriceBottomView.setEstimatedPrice(price);
    }

    public void setEstimatedPrice(String price)
    {
        mEstimatedPriceBottomView.setEstimatedPrice(price);
    }

    private void setBikeEnabled()
    {
        rl_bike_container.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_bg_circle_primary_fill));
        img_bike.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_motorcycle_white));
        rl_car_container.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_bg_circle_white_fill));
        img_car.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_truck_grey));
    }

    private void setCarEnabled()
    {
        rl_bike_container.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_bg_circle_white_fill));
        img_bike.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_motorcycle_grey));
        rl_car_container.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_bg_circle_primary_fill));
        img_car.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_truck_white));
    }

    public abstract void onBikeEnabled(View view);
    public abstract void onCarEnabled(View view);
}
