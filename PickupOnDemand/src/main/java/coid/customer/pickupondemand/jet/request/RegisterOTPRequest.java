package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.APIResult;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jordan.leonardi on 2/20/2018.
 */

public class RegisterOTPRequest extends BaseNetworkRequest<Void> {

    private String mFullName;
    private String mUserName;
    private String mPassword;
    private String mConfirmPassword;
    private String mEmail;
    private String mPhoneNumber;

    public RegisterOTPRequest(Context context, String fullName, String userName, String phoneNumber, String password, String confirmPassword, String email) {

        super(context);
        mFullName = fullName;
        mUserName = userName;
        mPassword = password;
        mConfirmPassword = confirmPassword;
        mEmail = email;
        mPhoneNumber = phoneNumber;
    }

    @Override
    public Call<Void> getCall() {
        return RetrofitProvider.getAuthService().registerOtp(mUserName, mPassword, mConfirmPassword, mFullName, mEmail,mPhoneNumber);
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
                //getBaseActivity().getNavigator().popResumeToDefaultFragment();
        //APIResult apiResult = response.body();
        hideLoadingDialog();
//
//            }
//        };
//        loginRequest.executeAsync();
//        RetrofitProvider.getResourcesService().register(mUserName, mPassword, mConfirmPassword, mFullName, mEmail,mPhoneNumber);

//        Toast.makeText(getContext(), "successOTP", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        return;
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }


}
