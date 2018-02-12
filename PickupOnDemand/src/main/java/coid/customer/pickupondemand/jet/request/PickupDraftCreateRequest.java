package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PickupDraftCreateRequest extends BaseNetworkRequest<Pickup>
{
    private Double mLat, mLng;

    public PickupDraftCreateRequest(Context context, Double lat, Double lng)
    {
        super(context);
        mLat = lat != null ? lat : 0D;
        mLng = lng != null ? lng : 0D;
    }

    @Override
    public Call<Pickup> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().createPickupDraft(mLat, mLng);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Pickup> response)
    {
        super.onResponseFailedOnUIThread(response);
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
