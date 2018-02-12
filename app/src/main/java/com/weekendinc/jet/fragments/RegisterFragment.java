package com.weekendinc.jet.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.weekendinc.jet.R;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.request.RegisterRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends coid.customer.pickupondemand.jet.base.BaseFragment
{
    private TextInputLayout input_layout_full_name, input_layout_email, input_layout_password, input_layout_password_repeat;
    private TextInputEditText et_full_name, et_email, et_password, et_password_repeat;
    private Button btn_register;
    private TextView tv_login;

    public RegisterFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
    }

    private void setView()
    {
        input_layout_full_name = (TextInputLayout) findViewById(R.id.input_layout_full_name);
        input_layout_email = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        input_layout_password_repeat = (TextInputLayout) findViewById(R.id.input_layout_password_repeat);
        et_full_name = (TextInputEditText) findViewById(R.id.et_full_name);
        et_email = (TextInputEditText) findViewById(R.id.et_email);
        et_password = (TextInputEditText) findViewById(R.id.et_password);
        et_password_repeat = (TextInputEditText) findViewById(R.id.et_password_repeat);
        btn_register = (Button) findViewById(R.id.btn_register);
        tv_login = (TextView) findViewById(R.id.tv_login);
    }

    private void setEvent()
    {
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                register();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getNavigator().popResumeToDefaultFragment();
            }
        });
//        et_full_name.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_full_name, et_full_name));
//        et_email.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_email, et_email));
//        et_password.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_password, et_password));
    }

    private void register()
    {
        if (!isValid())
            return;

        String fullName = et_full_name.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirmPassword = et_password_repeat.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        RegisterRequest registerRequest = new RegisterRequest(mContext, fullName, password, confirmPassword, email)
        {
            @Override
            protected void onResponseFailedOnUIThread(Response<Void> response)
            {
                super.onResponseFailedOnUIThread(response);
                String responseMessage = "";
                try
                {
                    responseMessage = response.errorBody().string();
                    if (responseMessage.contains(ApiConfig.REGISTER_EMAIL_ALREADY_TAKEN)
                            || responseMessage.contains(ApiConfig.REGISTER_NAME_ALREADY_TAKEN))
                    {
                        input_layout_email.setError(Utility.Message.get(R.string.register_email_already_taken));
                        et_email.requestFocus();
                        return;
                    }
                }
                catch (Exception ex)
                {
                    showToast(Utility.Message.getNetworkFailureMessage(coid.customer.pickupondemand.jet.R.string.pod_register_failed, ex));
                    return;
                }

                showToast(Utility.Message.getResponseMessage(coid.customer.pickupondemand.jet.R.string.pod_register_failed, response));
            }
        };
        registerRequest.executeAsync();
    }

    private boolean isValid()
    {
        int errorCount = 0;

        if (Utility.Validation.isEditTextEmpty(input_layout_password, et_password))
            errorCount++;
        else
        {
            if (et_password.getText().toString().trim().length() < 6)
            {
                input_layout_password.setError(Utility.Message.get(R.string.register_password_minimum_six_characters));
                et_password.requestFocus();
                errorCount++;
            }
            else
            {
                if (!(et_password.getText().toString().trim().equals(et_password_repeat.getText().toString().trim())))
                {
                    input_layout_password.setError(Utility.Message.get(R.string.register_password_repeat_mismatch));
                    input_layout_password_repeat.setError(Utility.Message.get(R.string.register_password_repeat_mismatch));
                    et_password_repeat.requestFocus();
                    errorCount++;
                }
            }
        }

        if (Utility.Validation.isEditTextEmpty(input_layout_email, et_email))
            errorCount++;
        else
        {
            if (!Utility.Validation.isEmailFormatValid(et_email.getText().toString().trim()))
            {
                input_layout_email.setError(Utility.Message.get(R.string.register_invalid_email_format));
                et_email.requestFocus();
                errorCount++;
            }
        }
        if (Utility.Validation.isEditTextEmpty(input_layout_full_name, et_full_name))
            errorCount++;

        return errorCount <= 0;
    }
}
