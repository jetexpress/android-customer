package coid.customer.pickupondemand.jet.request;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterRequest extends BaseNetworkRequest<Void>
{
    private String mFullName;
    private String mUserName;
    private String mPassword;
    private String mConfirmPassword;
    private String mEmail;
    private String mPhoneNumber;

    public RegisterRequest(Context context, String fullName, String userName, String phoneNumber,String password, String confirmPassword, String email)
    {
        super(context);
        mFullName = fullName;
        mUserName = userName;
        mPassword = password;
        mConfirmPassword = confirmPassword;
        mEmail = email;
        mPhoneNumber = phoneNumber;

    }

    @Override
    public Call<Void> getCall()
    {
        return RetrofitProvider.getAuthService().register(mUserName, mPassword, mConfirmPassword, mFullName, mEmail);
    }

    @Override
    protected void onSuccessOnUIThread(Response<Void> response)
    {
//        remark jordan 20180215
//        LoginRequest loginRequest = new LoginRequest(getContext(), mEmail, mPassword)
//        {
//            @Override
//            protected void onStartOnUIThread()
//            {
//                getBaseActivity().getNavigator().popResumeToDefaultFragment();
//
//            }
//        };
//        loginRequest.executeAsync();

        /*RegisterOTPRequest registerOTPRequest = new RegisterOTPRequest(getContext(), mFullName, mUserName, mPhoneNumber, mPassword, mConfirmPassword,  mEmail)
        {
            @Override
            protected void onStartOnUIThread() {

            }
        };
        registerOTPRequest.executeAsync();*/
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_register_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
