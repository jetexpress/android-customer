package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.support.annotation.Nullable;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PickupItemUpdateRequest extends BaseNetworkRequest<PickupItem>
{
    private String mPickupCode;
    private PickupItem mPickupItem;

    public PickupItemUpdateRequest(Context context, String pickupCode, PickupItem pickupItem)
    {
        super(context);
        mPickupCode = pickupCode;
        mPickupItem = pickupItem;
    }

    @Override
    public Call<PickupItem> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().updatePickupItem(mPickupCode, mPickupItem.getCode(), mPickupItem);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<PickupItem> response)
    {
        super.onResponseFailedOnUIThread(response);
        if (Utility.Message.responseFailedMessage.contains(ApiConfig.CARGO_MINIMUM_WEIGHT))
            showToast(getString(R.string.pod_pickup_item_cargo_minimum_weight_message));
        else
            showToast(Utility.Message.getResponseFailedMessage(R.string.pod_update_pickup_item_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_update_pickup_item_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(Utility.Message.get(R.string.pod_request_timed_out));
    }
}
