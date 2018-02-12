package coid.customer.pickupondemand.jet.request;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class RefreshTokenRequest extends BaseNetworkRequest<Login>
{
    private BaseNetworkRequest mUnauthorizedRequest;

    public RefreshTokenRequest(Context context, BaseNetworkRequest unauthorizedRequest)
    {
        super(context);
        mUnauthorizedRequest = unauthorizedRequest;
    }

    @Override
    public Call<Login> getCall()
    {
        Login login = DBQuery.getSingle(Login.class);
        if (login == null)
        {
            navigateToLogin();
            return null;
        }

        return RetrofitProvider.getAuthService().refresh(login.getRefreshToken(), ApiConfig.LOGIN_CLIENT_ID, ApiConfig.REFRESH_GRANT_TYPE);
    }

    @Override
    protected void onSuccessOnNetworkThread(Response<Login> response)
    {
        DBQuery.truncate(Login.class);
        Login login = response.body();
        login.save();

        if (getContext() == null && mUnauthorizedRequest != null)
            mUnauthorizedRequest.execute();
    }

    @Override
    protected void onStartOnUIThread(){}

    @Override
    protected void onSuccessOnUIThread(Response<Login> response)
    {
        if (mUnauthorizedRequest != null)
            mUnauthorizedRequest.executeAsync();
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<Login> response)
    {
        navigateToLogin();
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        navigateToLogin();
    }

    private void navigateToLogin()
    {
        if (getBaseActivity() == null)
            return;

        showToast("Login expired, silakan Login kembali");
        Intent intent = new Intent();
        intent.putExtra(AppConfig.MAIN_ACTIVITY_EXTRA_PARAM, ApiConfig.REFRESH_GRANT_TYPE);
        getBaseActivity().setResult(Activity.RESULT_OK, intent);
        getBaseActivity().finish();
    }
}