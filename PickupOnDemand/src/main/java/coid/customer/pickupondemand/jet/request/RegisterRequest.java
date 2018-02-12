package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterRequest extends BaseNetworkRequest<Void>
{
    private String mFullName;
    private String mPassword;
    private String mConfirmPassword;
    private String mEmail;

    public RegisterRequest(Context context, String fullName, String password, String confirmPassword, String email)
    {
        super(context);
        mFullName = fullName;
        mPassword = password;
        mConfirmPassword = confirmPassword;
        mEmail = email;
    }

    @Override
    public Call<Void> getCall()
    {
        return RetrofitProvider.getAuthService().register(mEmail, mPassword, mConfirmPassword, mFullName, mEmail);
    }

    @Override
    protected void onSuccessOnUIThread(Response<Void> response)
    {
        LoginRequest loginRequest = new LoginRequest(getContext(), mEmail, mPassword)
        {
            @Override
            protected void onStartOnUIThread()
            {
                getBaseActivity().getNavigator().popResumeToDefaultFragment();
            }
        };
        loginRequest.executeAsync();
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
