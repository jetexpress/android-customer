package coid.customer.pickupondemand.jet.request;

/**
 * Created by Qitma on 2/21/2018.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.ValidateOTP;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import coid.customer.pickupondemand.jet.model.APIResult;
import retrofit2.Call;
import retrofit2.Response;

public class ValidateOTPRequest extends  BaseNetworkRequest<ValidateOTP> {

    private String mEmail;
    private String mCode;
    private String mPhoneNumber;
    private String mPassword;

    public ValidateOTPRequest(Context ctx , String _email, String _code , String _phoneNumber , String _password)
    {
        super(ctx);
        mEmail = _email;
        mCode = _code;
        mPhoneNumber = _phoneNumber;
        mPassword = _password;
    }

    @Override
    public Call<ValidateOTP> getCall() {
        return RetrofitProvider.getAuthService().validateOtp(mEmail,mCode,mPhoneNumber);
    }


    @Override
    protected void onSuccessOnUIThread(Response<ValidateOTP> response)
    {
//        remark jordan 20180215
//
//        RetrofitProvider.getResourcesService().register(mUserName, mPassword, mConfirmPassword, mFullName, mEmail,mPhoneNumber);

      /*  if(response.isSuccessful())
       {
           try{
               ValidateOTP validate = response.body();
               //Log.w("2.0 getFeed > Full json res wrapped in pretty printed gson => ",new GsonBuilder().setPrettyPrinting().create().toJson(response));
               if(validate.isValid)
               {
                   LoginRequest loginRequest = new LoginRequest(getContext(), mEmail, mPassword)
                    {
                        @Override
                        protected void onStartOnUIThread()
                        {
                            hideLoadingDialog();

                        }
                    };
                    loginRequest.executeAsync();
               }
               else
               {

               }

           }
           catch(Exception ex)
           {
               Toast.makeText(getContext(), "gagal parse", Toast.LENGTH_SHORT).show();
           }
       }else{

           try {
               JSONObject jObjError = new JSONObject(response.errorBody().string());
               Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
           } catch (Exception e) {
               Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
           }
       }*/
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_validate_otp_failed, ex));
        return;
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }
}
