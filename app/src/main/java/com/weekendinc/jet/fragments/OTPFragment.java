package com.weekendinc.jet.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.APIResult;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.ValidateOTP;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import coid.customer.pickupondemand.jet.request.CreateOTPRequest;
import coid.customer.pickupondemand.jet.request.LoginRequest;
import coid.customer.pickupondemand.jet.request.ValidateOTPRequest;
import retrofit2.Call;
import retrofit2.Response;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.weekendinc.jet.R;

import org.json.JSONObject;

import coid.customer.pickupondemand.jet.base.BaseFragment;

/**
 * Created by jordan.leonardi on 2/15/2018.
 */

public class OTPFragment extends BaseFragment{

    private EditText otp_phone_number,
            otp_code_1,
            otp_code_2,
            otp_code_3,
            otp_code_4;
    private TextView
            otp_resend_code,
            otp_label_retry_1,
            otp_tick,
            otp_skip;
    private String otp_codes;
    private ProgressBar loading_verifiy_otp;
    private Button btn_verifikasi;

    private String phoneNumber;
    private String email;
    private String remarks;
    private String password;

    public OTPFragment(){

    }
    public static OTPFragment newInstance(String email , String phoneNumber , String remarks, String password) {
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("phoneNumber", phoneNumber);
        bundle.putString("remarks",remarks);
        bundle.putString("password",password);

        OTPFragment otpFragment = new OTPFragment();
        otpFragment.setArguments(bundle);
        return otpFragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            email = bundle.getString("email");
            phoneNumber = bundle.getString("phoneNumber");
            remarks = bundle.getString("remarks");
            password = bundle.getString("password");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submit_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        readBundle(getArguments());

        setView();
        setEvent();
    }

    private void setView()
    {
        otp_phone_number = (EditText) findViewById(R.id.otp_phone_number);
        otp_code_1 = (EditText) findViewById(R.id.otp_code_1);
        otp_code_2 = (EditText) findViewById(R.id.otp_code_2);
        otp_code_3 = (EditText) findViewById(R.id.otp_code_3);
        otp_code_4 = (EditText) findViewById(R.id.otp_code_4);
        otp_resend_code = (TextView)findViewById(R.id.otp_resend_code);
        otp_label_retry_1 = (TextView)findViewById(R.id.otp_label_retry_1);
        otp_tick = (TextView)findViewById(R.id.otp_tick);
        otp_skip = (TextView)findViewById(R.id.otp_skip);
        btn_verifikasi = (Button)findViewById(R.id.btn_verifikasi);
        loading_verifiy_otp = (ProgressBar)findViewById(R.id.loading_verifiy_otp);
        otp_phone_number.setText(phoneNumber);
        otp_code_1.requestFocus();
        otp_phone_number.setEnabled(false);
        stopLoading();
    }

    private void setEvent(){

        timer();

        btn_verifikasi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //startLoading();
                otp_codes = otp_code_1.getText().toString()+otp_code_2.getText().toString()+otp_code_3.getText().toString()+otp_code_4.getText().toString();
                ValidateOTPRequest validateOtp = new ValidateOTPRequest(getContext(),email,otp_codes,phoneNumber,password){
                    @Override
                    protected void onStartOnUIThread() {
                        super.onStartOnUIThread();
                    }

                    @Override
                    protected void onSuccessOnUIThread(Response<ValidateOTP> response) {
                        super.onSuccessOnUIThread(response);
                        //stopLoading();
                        if(response.isSuccessful())
                        {
                            try{
                                ValidateOTP validate = response.body();
                                //Log.w("2.0 getFeed > Full json res wrapped in pretty printed gson => ",new GsonBuilder().setPrettyPrinting().create().toJson(response));
                                if(validate.isValid)
                                {
                                    LoginRequest loginRequest = new LoginRequest(getContext(), email, password)
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
                                    hideLoadingDialog();
                                    resetAll();
                                    Toast.makeText(getContext(), "Gagal Verifikasi", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch(Exception ex)
                            {
                                hideLoadingDialog();
                                resetAll();
                                Toast.makeText(getContext(), "gagal parse", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            resetAll();
                            try {
                                String message = Utility.Message.responseFailedMessage;
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    protected void onFailedOnUIThread(Exception ex) {
                        super.onFailedOnUIThread(ex);
                        resetAll();
                        Toast.makeText(getContext(),ex.toString() , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onTimeOutOnUIThread() {
                        super.onTimeOutOnUIThread();
                    }
                };
                validateOtp.executeAsync();
                //Toast.makeText(getContext(),"coming soon",Toast.LENGTH_LONG).show();
            }
        });

        otp_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _remarks = "retry";
                CreateOTPRequest createOtp = new CreateOTPRequest(getContext(),email,phoneNumber,_remarks){
                    @Override
                    protected void onSuccessOnUIThread(Response<Void> response) {
                        super.onSuccessOnUIThread(response);
                        hideLoadingDialog();
                        Toast.makeText(getContext(),"Resend OTP Success",Toast.LENGTH_LONG).show();
                        resetAll();
                        otp_tick.setVisibility(View.VISIBLE);
                        otp_label_retry_1.setVisibility(View.VISIBLE);
                        otp_resend_code.setVisibility(View.GONE);
                        otp_phone_number.setEnabled(false);
                        timer();
                    }

                    @Override
                    protected void onFailedOnUIThread(Exception ex) {
                        super.onFailedOnUIThread(ex);
                    }

                    @Override
                    protected void onTimeOutOnUIThread() {
                        super.onTimeOutOnUIThread();
                    }

                };
                createOtp.executeAsync();

            }
        });

        otp_skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                LoginRequest loginRequest = new LoginRequest(getContext(), email, password)
                {
                    @Override
                    protected void onStartOnUIThread()
                    {

                    }
                };
                loginRequest.executeAsync();
            }
        });

        otp_code_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                otp_code_2.requestFocus();
            }
        });
        otp_code_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                otp_code_3.requestFocus();
            }
        });
        otp_code_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    otp_code_4.requestFocus();

            }
        });

    }

    public void stopLoading(){
        loading_verifiy_otp.setVisibility(View.GONE);
    }
    public void startLoading(){
        loading_verifiy_otp.setVisibility(View.VISIBLE);
    }

    public void timer(){
        new CountDownTimer(60000,1000){
            public void onTick(long millisUntilFinished) {
                otp_tick.setText( ""+ millisUntilFinished / 1000);
            }

            public void onFinish() {
                otp_tick.setVisibility(View.GONE);
                otp_label_retry_1.setVisibility(View.GONE);
                otp_resend_code.setVisibility(View.VISIBLE);
                otp_phone_number.setEnabled(true);
            }
        }.start();
    }

    private void resetAll()
    {
        otp_code_1.setText("");
        otp_code_2.setText("");
        otp_code_3.setText("");
        otp_code_4.setText("");
        otp_code_1.requestFocus();
    }


}
