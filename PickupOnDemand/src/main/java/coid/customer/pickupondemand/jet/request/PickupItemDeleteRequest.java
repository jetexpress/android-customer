package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PickupItemDeleteRequest extends BaseNetworkRequest<PickupItem>
{
    private String mPickupCode;
    private String mPickupItemCode;

    public PickupItemDeleteRequest(Context context, String pickupCode, String pickupItemCode)
    {
        super(context);
        mPickupCode = pickupCode;
        mPickupItemCode = pickupItemCode;
    }

    @Override
    public Call<PickupItem> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().deletePickupItem(mPickupCode, mPickupItemCode);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<PickupItem> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_delete_pickup_item_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_delete_pickup_item_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
