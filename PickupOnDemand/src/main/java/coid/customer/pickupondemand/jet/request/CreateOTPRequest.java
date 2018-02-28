package coid.customer.pickupondemand.jet.request;

/**
 * Created by Qitma on 2/21/2018.
 */

import android.content.Context;
import android.widget.Toast;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class CreateOTPRequest extends BaseNetworkRequest<Void> {

    private String mEmail;
    private String mRemarks;
    private String mPhoneNumber;

    public CreateOTPRequest(Context ctx , String _email , String _phoneNumber , String _remarks)
    {
        super(ctx);
        mEmail = _email;
        mRemarks = _remarks;
        mPhoneNumber = _phoneNumber;
    }

    @Override
    public Call<Void> getCall() {
        return RetrofitProvider.getAuthService().createOtp(mEmail,mPhoneNumber,mRemarks);
    }

    @Override
    protected void onSuccessOnUIThread(Response<Void> response)
    {
//


    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_create_otp_failed, ex));
        return;
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }

}
