package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.PickupItemSimulation;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PickupItemPriceSimulationRequest extends BaseNetworkRequest<PickupItemSimulation>
{
    private PickupItemSimulation mPickupItemSimulation;

    public PickupItemPriceSimulationRequest(Context context, PickupItemSimulation pickupItemSimulation)
    {
        super(context);
        mPickupItemSimulation = pickupItemSimulation;
    }

    @Override
    public Call<PickupItemSimulation> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().simulatePrice(mPickupItemSimulation);
    }

    @Override
    protected void onResponseFailedOnNetworkThread(Response<PickupItemSimulation> response)
    {
        super.onResponseFailedOnNetworkThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_estimated_price_request_timed_out, response));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_estimated_price_request_timed_out));
    }
}
