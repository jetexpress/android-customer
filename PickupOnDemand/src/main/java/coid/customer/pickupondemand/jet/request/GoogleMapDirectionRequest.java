package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.map.Direction;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class GoogleMapDirectionRequest extends BaseNetworkRequest<Direction>
{
    private String mOrigin;
    private String mDestination;

    public GoogleMapDirectionRequest(Context context, String origin, String destination)
    {
        super(context);
        mOrigin = origin;
        mDestination = destination;
    }

    public GoogleMapDirectionRequest(Context context, LatLng originLatLng, LatLng destinationLatLng)
    {
        this(context,
                String.valueOf(originLatLng.latitude) + "," + String.valueOf(originLatLng.longitude),
                String.valueOf(destinationLatLng.latitude) + "," + String.valueOf(destinationLatLng.longitude));
    }

    @Override
    public Call<Direction> getCall()
    {
        return RetrofitProvider.getMapService().getDirection(mOrigin, mDestination);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Direction> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_get_direction_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_get_direction_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
