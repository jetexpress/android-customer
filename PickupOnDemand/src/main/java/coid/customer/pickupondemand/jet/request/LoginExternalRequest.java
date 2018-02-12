package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class LoginExternalRequest extends BaseNetworkRequest<Login>
{
    public static final String PROVIDER_FACEBOOK = "facebook";
    public static final String PROVIDER_GOOGLE = "google";
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PROVIDER_FACEBOOK, PROVIDER_GOOGLE})
    public @interface Provider {}

    private String mAccessToken;
    private String mProvider;

    /** @param provider One of {@link #PROVIDER_FACEBOOK}, {@link #PROVIDER_GOOGLE}.*/
    public LoginExternalRequest(Context context, String accessToken, @Provider String provider)
    {
        super(context);
        mAccessToken = accessToken;
        mProvider = provider;
    }

    @Override
    public Call<Login> getCall()
    {
        return RetrofitProvider.getAuthorizedAuthService().loginExternal(mAccessToken, mProvider);
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
