package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PickupItemCreateRequest extends BaseNetworkRequest<PickupItem>
{
    private String mPickupCode;
    private PickupItem mPickupItem;

    public PickupItemCreateRequest(Context context, String pickupCode, PickupItem pickupItem)
    {
        super(context);
        mPickupCode = pickupCode;
        mPickupItem = pickupItem;
    }

    @Override
    public Call<PickupItem> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().createPickupItemDraft(mPickupCode, mPickupItem);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<PickupItem> response)
    {
        super.onResponseFailedOnUIThread(response);
        if (Utility.Message.responseFailedMessage.contains(ApiConfig.QR_CODE_ALREADY_USED))
            showToast(getString(R.string.pod_pickup_item_duplicate_qr_code_message));
        else if (Utility.Message.responseFailedMessage.contains(ApiConfig.CARGO_MINIMUM_WEIGHT))
            showToast(getString(R.string.pod_pickup_item_cargo_minimum_weight_message));
        else
            showToast(Utility.Message.getResponseFailedMessage(R.string.pod_create_pickup_item_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_create_pickup_item_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
