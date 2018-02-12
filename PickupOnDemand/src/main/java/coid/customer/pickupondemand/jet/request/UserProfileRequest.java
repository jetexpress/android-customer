package coid.customer.pickupondemand.jet.request;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.Wallet;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class UserProfileRequest extends BaseNetworkRequest<UserProfile>
{
    public UserProfileRequest()
    {
        super(null);
    }

    public UserProfileRequest(Context context)
    {
        super(context);
    }

    @Override
    public Call<UserProfile> getCall()
    {
        return RetrofitProvider.getAuthorizedAuthService().getProfile();
    }

    @Override
    protected void onSuccessOnNetworkThread(Response<UserProfile> response)
    {
        DBQuery.truncate(UserProfile.class);
        DBQuery.truncate(Wallet.class);
        UserProfile userProfile = response.body();
        userProfile.saveComplete();
    }

    @Override
    protected void onTimeOutOnNetworkThread()
    {
        if (getContext() != null)
            return;

        execute();
    }

    @Override
    protected void onSuccessOnUIThread(Response<UserProfile> response)
    {
        super.onSuccessOnUIThread(response);
        Intent intent = new Intent();
        intent.putExtra(AppConfig.MAIN_ACTIVITY_EXTRA_PARAM, ApiConfig.LOGIN_GRANT_TYPE);
        getBaseActivity().setResult(Activity.RESULT_OK, intent);
        getBaseActivity().finish();
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<UserProfile> response)
    {
        super.onResponseFailedOnUIThread(response);
        DBQuery.truncate(Login.class);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_user_profile_get_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        DBQuery.truncate(Login.class);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_user_profile_get_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        DBQuery.truncate(Login.class);
        showToast(getString(R.string.pod_request_timed_out));
    }
}
