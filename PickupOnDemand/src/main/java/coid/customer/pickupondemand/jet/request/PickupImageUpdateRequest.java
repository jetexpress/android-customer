package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PickupImageUpdateRequest extends BaseNetworkRequest<Pickup>
{
    private String mPickupCode;
    private String mImageData;

    public PickupImageUpdateRequest(Context context, String pickupCode, String imageData)
    {
        super(context);
        mPickupCode = pickupCode;
        mImageData = imageData;
    }

    @Override
    public Call<Pickup> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().updatePickupImage(mPickupCode, mImageData);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Pickup> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_update_pickup_image_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_update_pickup_image_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
