package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.model.PickupItemSimulation;
import coid.customer.pickupondemand.jet.request.PickupItemPriceSimulationRequest;
import retrofit2.Response;

public class EstimatedPriceView extends LinearLayout
{
    private ProgressBar progress_bar_estimated_price;
    private ImageView img_refresh_estimated_price;
    private TextView tv_estimated_price;
    private PickupItemPriceSimulationRequest mPickupItemPriceSimulationRequest;

    public EstimatedPriceView(Context context)
    {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_layout_pickup_estimated_price, this, true);
        initialize();
    }

    private void initialize()
    {
        setView();
        setEvent();
    }

    private void setView()
    {
        tv_estimated_price = (TextView) this.findViewById(R.id.tv_estimated_price);
        img_refresh_estimated_price = (ImageView) this.findViewById(R.id.img_refresh_estimated_price);
        progress_bar_estimated_price = (ProgressBar) this.findViewById(R.id.progress_bar_estimated_price);
    }

    private void setEvent()
    {
        img_refresh_estimated_price.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onRefreshClicked();
            }
        });
    }

    private PickupItemPriceSimulationRequest getPickupItemPriceSimulationRequest(PickupItemSimulation pickupItemSimulation)
    {
        if (pickupItemSimulation == null)
            return null;

        return new PickupItemPriceSimulationRequest(getContext(), pickupItemSimulation)
        {
            @Override
            protected void showLoadingDialog()
            {
                showRefreshProgressBar();
            }

            @Override
            protected void hideLoadingDialog()
            {
                showRefreshButton();
            }

            @Override
            protected void onSuccessOnUIThread(Response<PickupItemSimulation> response)
            {
                super.onSuccessOnUIThread(response);
                setEstimatedPrice(response.body().getTotalFee());
            }
        };
    }

    public void requestEstimatedPrice(PickupItemSimulation pickupItemSimulation)
    {
        mPickupItemPriceSimulationRequest = getPickupItemPriceSimulationRequest(pickupItemSimulation);

        if (mPickupItemPriceSimulationRequest != null)
            mPickupItemPriceSimulationRequest.executeAsync();
    }

    public void cancelRequest()
    {
        if (mPickupItemPriceSimulationRequest != null)
            mPickupItemPriceSimulationRequest.cancel();
    }

    public void clear()
    {
        if (mPickupItemPriceSimulationRequest != null)
        {
            mPickupItemPriceSimulationRequest.clear();
            mPickupItemPriceSimulationRequest = null;
        }
    }

    public void setEstimatedPrice(Double estimatedPrice)
    {
        String formattedEstimatedPrice = "-";
        if (estimatedPrice > 0)
            formattedEstimatedPrice = getContext().getString(R.string.pod_currency) + " " + Utility.NumberFormat.doubleToString(estimatedPrice, 0);
        tv_estimated_price.setText(formattedEstimatedPrice);
    }

    public void setEstimatedPrice(String estimatedPrice)
    {
        tv_estimated_price.setText(estimatedPrice);
    }

    public void showRefreshButton()
    {
        img_refresh_estimated_price.setVisibility(VISIBLE);
        progress_bar_estimated_price.setVisibility(GONE);
    }

    public void showRefreshProgressBar()
    {
        img_refresh_estimated_price.setVisibility(GONE);
        progress_bar_estimated_price.setVisibility(VISIBLE);
    }

    public void showPriceOnly()
    {
        img_refresh_estimated_price.setVisibility(GONE);
        progress_bar_estimated_price.setVisibility(GONE);
    }

    public void onRefreshClicked(){}
}
