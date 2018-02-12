package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.support.annotation.Nullable;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.model.APIResult;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class ChangePasswordRequest extends BaseNetworkRequest<APIResult>
{
    private String mUsername;
    private String mCurrentPassword;
    private String mNewPassword;

    public ChangePasswordRequest(@Nullable Context context, String username, String currentPassword, String newPassword)
    {
        super(context);
        mUsername = username;
        mCurrentPassword = currentPassword;
        mNewPassword = newPassword;
    }

    @Override
    public Call<APIResult> getCall()
    {
        return RetrofitProvider.getAuthService().changePassword(mUsername, mCurrentPassword, mNewPassword);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<APIResult> response)
    {
        super.onResponseFailedOnUIThread(response);
        if (Utility.Message.responseFailedMessage.contains(ApiConfig.INCORRECT_PASSWORD))
            showToast(getString(R.string.pod_setting_incorrect_password));
        else if (Utility.Message.responseFailedMessage.contains(ApiConfig.PASSWORD_AT_LEAST_6_CHARACTERS))
            showToast(getString(R.string.pod_setting_password_length_at_least_6_characters));
        else
            showToast(Utility.Message.getResponseFailedMessage(R.string.pod_setting_change_password_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_setting_change_password_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
