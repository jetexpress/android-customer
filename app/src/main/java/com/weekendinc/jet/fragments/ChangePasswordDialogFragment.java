package com.weekendinc.jet.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.weekendinc.jet.R;
import com.weekendinc.jet.model.AuthenticationModel;
import com.weekendinc.jet.model.pojo.ResultChangePassword;
import com.weekendinc.jet.utils.SubscriptionUtils;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseDialogFragment;
import coid.customer.pickupondemand.jet.model.APIResult;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.request.ChangePasswordRequest;
import retrofit2.Response;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class ChangePasswordDialogFragment extends BaseDialogFragment
{
    private EditText et_current_password, et_new_password, et_confirm_new_password;
    private TextView tv_error_current_password, tv_error_new_password, tv_error_confirm_new_password, tv_cancel, tv_submit;
    private ChangePasswordRequest mChangePasswordRequest;

    public ChangePasswordDialogFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, coid.customer.pickupondemand.jet.R.style.JETBaseDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog d = getDialog();
        if (d != null && d.getWindow() != null)
        {
            d.getWindow().setGravity(Gravity.CENTER);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroy()
    {
        if (mChangePasswordRequest != null)
        {
            mChangePasswordRequest.clear();
            mChangePasswordRequest = null;
        }
        super.onDestroy();
    }

    private void setView()
    {
        et_current_password = (EditText) findViewById(R.id.et_current_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_new_password = (EditText) findViewById(R.id.et_confirm_new_password);
        tv_error_current_password = (TextView) findViewById(R.id.tv_error_current_password);
        tv_error_new_password = (TextView) findViewById(R.id.tv_error_new_password);
        tv_error_confirm_new_password = (TextView) findViewById(R.id.tv_error_confirm_new_password);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    private void setEvent()
    {
        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        tv_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isValid())
                    submitChangePassword();
            }
        });
    }

    private boolean isValid()
    {
        int errorCount = 0;

        String currentPassword = et_current_password.getText().toString().trim();
        String newPassword = et_new_password.getText().toString().trim();
        String confirmNewPassword = et_confirm_new_password.getText().toString().trim();

        if (confirmNewPassword.isEmpty())
        {
            errorCount++;
            tv_error_confirm_new_password.setText(Utility.Message.get(R.string.pod_required));
            tv_error_confirm_new_password.setVisibility(View.VISIBLE);
        }

        if (newPassword.isEmpty())
        {
            errorCount++;
            tv_error_new_password.setText(Utility.Message.get(R.string.pod_required));
            tv_error_new_password.setVisibility(View.VISIBLE);
        }
        if (currentPassword.isEmpty())
        {
            errorCount++;
            tv_error_current_password.setText(Utility.Message.get(R.string.pod_required));
            tv_error_current_password.setVisibility(View.VISIBLE);
        }

        if (errorCount <= 0)
        {
            if (!newPassword.equals(confirmNewPassword))
            {
                errorCount++;
                tv_error_confirm_new_password.setText(Utility.Message.get(R.string.setting_confirm_new_password_not_match));
                tv_error_confirm_new_password.setVisibility(View.VISIBLE);
            }
        }

        return errorCount <= 0;
    }

    private void hideErrorMessage()
    {
        tv_error_current_password.setVisibility(View.GONE);
        tv_error_new_password.setVisibility(View.GONE);
        tv_error_confirm_new_password.setVisibility(View.GONE);
    }

    private void submitChangePassword()
    {
        hideErrorMessage();
        UserProfile userProfile = DBQuery.getSingle(UserProfile.class);
        if (userProfile == null)
        {
            showToast(Utility.Message.get(R.string.pod_setting_change_password_failed));
            return;
        }

        String currentPassword = et_current_password.getText().toString().trim();
        String newPassword = et_new_password.getText().toString().trim();

        mChangePasswordRequest = new ChangePasswordRequest(getActivity(), userProfile.getUsername(), currentPassword, newPassword)
        {
            @Override
            protected void onSuccessOnUIThread(Response<APIResult> response)
            {
                super.onSuccessOnUIThread(response);
                showToast(Utility.Message.get(R.string.setting_change_password_success));
                dismiss();
            }
        };
        mChangePasswordRequest.executeAsync();
    }
}
