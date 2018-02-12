package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class ChangeProfileRequest extends BaseNetworkRequest<Void>
{
    private String mFullName;
    private String mPhoneNumber;
    private String mAddress;

    public ChangeProfileRequest(Context context, String fullName, String phoneNumber, String address)
    {
        super(context);
        mFullName = fullName;
        mPhoneNumber = phoneNumber;
        mAddress = address;
    }

    @Override
    public Call<Void> getCall()
    {
        return RetrofitProvider.getAuthorizedAuthService().changeProfile(mFullName, mPhoneNumber, mAddress);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Void> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_change_profile_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_change_profile_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
