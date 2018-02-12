package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.support.annotation.Nullable;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class RateCourierRequest extends BaseNetworkRequest<Void>
{
    private Float mRating;
    private String mPickupCode;

    public RateCourierRequest(@Nullable Context context, Float rating, String pickupCode)
    {
        super(context);
        mRating = rating;
        mPickupCode = pickupCode;
    }

    @Override
    public Call<Void> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().rateCourier(mPickupCode, mRating);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Void> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_rate_courier_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_rate_courier_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
