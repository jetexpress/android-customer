package com.weekendinc.jet;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.weekendinc.jet.fragments.ChangePasswordDialogFragment;

import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseActivity;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.ModelLoader;
import coid.customer.pickupondemand.jet.request.ChangeProfileRequest;
import retrofit2.Response;

public class ProfileSettingActivity extends AppCompatActivity
{

    private ScrollView sv_content_container;
    private ProgressBar progress_bar_profile;
    private TextInputLayout input_layout_full_name, input_layout_phone_number, input_layout_address;
    private TextInputEditText et_full_name, et_phone_number, et_address;
    private Button btn_submit_change_profile, btn_change_password;
    private UserProfile mUserProfile;
    private ChangeProfileRequest mChangeProfileRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showProgressBar();
        if (getSupportLoaderManager().getLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID) != null)
            getSupportLoaderManager().restartLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID, null, userProfileLoader);
        else
            getSupportLoaderManager().initLoader(AppConfig.LoaderId.USER_PROFILE_LOADER_ID, null, userProfileLoader);
    }

    @Override
    protected void onDestroy()
    {
        if (mChangeProfileRequest != null)
        {
            mChangeProfileRequest.cancel();
            mChangeProfileRequest.clear();
            mChangeProfileRequest = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setView()
    {
        setContentView(R.layout.activity_profile_setting);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.setting);
        }

        sv_content_container = findViewById(R.id.sv_content_container);
        progress_bar_profile = findViewById(R.id.progress_bar_profile);
        input_layout_full_name = findViewById(R.id.input_layout_full_name);
        input_layout_phone_number = findViewById(R.id.input_layout_phone_number);
        input_layout_address = findViewById(R.id.input_layout_address);
        et_full_name = findViewById(R.id.et_full_name);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_address = findViewById(R.id.et_address);
        btn_submit_change_profile = findViewById(R.id.btn_submit_change_profile);
        btn_change_password = findViewById(R.id.btn_change_password);
    }

    private void setEvent()
    {
        et_full_name.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_full_name, et_full_name));
        et_phone_number.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_phone_number, et_phone_number));
        et_address.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_address, et_address));

        btn_submit_change_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isValid())
                    submitProfile();
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePasswordDialogFragment dialog = new ChangePasswordDialogFragment();
                dialog.show(getSupportFragmentManager(), ProfileSettingActivity.this.getClass().getSimpleName());
            }
        });
    }

    private void setValue()
    {
        if (mUserProfile == null)
            return;

        et_full_name.setText(mUserProfile.getFullName());
        et_phone_number.setText(mUserProfile.getPhoneNumber());
        et_address.setText(mUserProfile.getAddress());
    }

    private void submitProfile()
    {
        final String fullName = et_full_name.getText().toString().trim();
        final String phoneNumber = et_phone_number.getText().toString().trim();
        final String address = et_address.getText().toString().trim();

        mChangeProfileRequest = new ChangeProfileRequest(ProfileSettingActivity.this, fullName, phoneNumber, address)
        {
            @Override
            protected void onSuccessOnUIThread(Response<Void> response)
            {
                super.onSuccessOnUIThread(response);
                if (mUserProfile != null)
                {
                    mUserProfile.setFullName(fullName);
                    mUserProfile.setPhoneNumber(phoneNumber);
                    mUserProfile.setAddress(address);
                    mUserProfile.save();
                }
                showToast(Utility.Message.get(R.string.setting_change_profile_success));
                finish();
            }
        };
        mChangeProfileRequest.executeAsync();
    }

    private boolean isValid()
    {
        int errorCount = 0;

        if (Utility.Validation.isEditTextEmpty(input_layout_address, et_address))
            errorCount++;
        if (Utility.Validation.isEditTextEmpty(input_layout_phone_number, et_phone_number))
            errorCount++;
        if (Utility.Validation.isEditTextEmpty(input_layout_full_name, et_full_name))
            errorCount++;

        return errorCount <= 0;
    }

    private void showProgressBar()
    {
        progress_bar_profile.setVisibility(View.VISIBLE);
        sv_content_container.setVisibility(View.GONE);
    }

    private void showContent()
    {
        progress_bar_profile.setVisibility(View.GONE);
        sv_content_container.setVisibility(View.VISIBLE);
    }

    LoaderManager.LoaderCallbacks<List<UserProfile>> userProfileLoader = new LoaderManager.LoaderCallbacks<List<UserProfile>>()
    {
        @Override
        public Loader<List<UserProfile>> onCreateLoader(int id, Bundle args)
        {
            return new ModelLoader<>(ProfileSettingActivity.this, UserProfile.class, true);
        }
        @Override
        public void onLoadFinished(Loader<List<UserProfile>> loader, List<UserProfile> data)
        {
            if (data.size() > 0)
            {
                showContent();
                mUserProfile = data.get(0);
                setValue();
            }
        }
        @Override
        public void onLoaderReset(Loader<List<UserProfile>> loader){}
    };
}
