package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class LoginRequest extends BaseNetworkRequest<Login>
{
    private String mEmail;
    private String mPassword;

    public LoginRequest(Context context, String email, String password)
    {
        super(context);
        mEmail = email;
        mPassword = password;
    }

    @Override
    public Call<Login> getCall()
    {
        return RetrofitProvider.getAuthService().login(mEmail, mPassword, ApiConfig.LOGIN_CLIENT_ID, ApiConfig.LOGIN_GRANT_TYPE);
    }

    @Override
    protected void onSuccessOnNetworkThread(Response<Login> response)
    {
        DBQuery.truncate(Login.class);
        Login login = response.body();
        login.save();
    }

    @Override
    protected void onSuccessOnUIThread(Response<Login> response)
    {
        UserProfileRequest userProfileRequest =  new UserProfileRequest(getContext())
        {
            @Override
            protected void onStartOnUIThread(){}
        };
        userProfileRequest.executeAsync();
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Login> response)
    {
        super.onResponseFailedOnUIThread(response);
        if (Utility.Message.responseFailedMessage.contains(ApiConfig.LOGIN_INVALID_USERNAME_OR_PASSWORD))
            showToast(getString(R.string.pod_login_invalid));
        else
            showToast(Utility.Message.getResponseFailedMessage(R.string.pod_login_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_login_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
