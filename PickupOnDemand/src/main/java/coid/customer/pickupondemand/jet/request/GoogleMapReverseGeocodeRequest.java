package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.map.ReverseGeocode;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class GoogleMapReverseGeocodeRequest extends BaseNetworkRequest<ReverseGeocode>
{
    private String mLatLng;

    public GoogleMapReverseGeocodeRequest(Context context, String latLng)
    {
        super(context);
        mLatLng = latLng;
    }

    @Override
    public Call<ReverseGeocode> getCall()
    {
        return RetrofitProvider.getMapService().getReverseGeocode(mLatLng);
    }

    @Override
    protected void onStartOnUIThread()
    {

    }

    @Override
    protected void onResponseFailedOnUIThread(Response<ReverseGeocode> response)
    {
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_get_reverse_geocode_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_get_reverse_geocode_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        showToast(getString(R.string.pod_request_timed_out));
    }
}
