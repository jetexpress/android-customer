package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.VerificationError;

import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.model.Config;
import coid.customer.pickupondemand.jet.model.PackagingItem;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.model.Product;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.DBQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupItemStep3Fragment extends PickupItemStepFragment
{
    private TextInputLayout input_layout_drop_shipper_name, input_layout_drop_shipper_phone_number, input_layout_drop_shipper_address;
    private TextInputEditText et_drop_shipper_name, et_drop_shipper_phone_number, et_drop_shipper_address;
    private TextView tv_show_optional_content;
    private LinearLayout ll_optional_content;
    private Boolean mIsOptionalOpened = false;
    private Config mConfig;

    public PickupItemStep3Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_item_step3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
        setUIState();
    }

    @Override
    public VerificationError verifyStep()
    {
        if (getStepListener() == null)
            return new VerificationError("");

        updatePickupItem();
        getStepListener().onSave(mStepPickupItem);
        return null;
    }

    @Override
    protected void updatePickupItem()
    {
        if (getView() == null)
            return;

        mStepPickupItem.setDropshipperName(et_drop_shipper_name.getText().toString().trim());
        mStepPickupItem.setDropshipperPhone(et_drop_shipper_phone_number.getText().toString().trim());
        mStepPickupItem.setDropshipperAddress(et_drop_shipper_address.getText().toString().trim());
    }

    @Override
    protected void setValue(PickupItem pickupItem)
    {
        et_drop_shipper_name.setText(pickupItem.getDropshipperName());
        et_drop_shipper_phone_number.setText(pickupItem.getDropshipperPhone());
        et_drop_shipper_address.setText(pickupItem.getDropshipperAddress());
    }

    @Override
    protected Boolean isValid(PickupItem pickupItem)
    {
        int errorCount = 0;

        if (Utility.Validation.isEditTextEmpty(input_layout_drop_shipper_phone_number, et_drop_shipper_phone_number))
            errorCount++;
        if (Utility.Validation.isEditTextEmpty(input_layout_drop_shipper_name, et_drop_shipper_name))
            errorCount++;

//        if (errorCount > 0)
//        {
//            et_drop_shipper_name.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_drop_shipper_name, et_drop_shipper_name));
//            et_drop_shipper_phone_number.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_drop_shipper_phone_number, et_drop_shipper_phone_number));
//        }

        return errorCount == 0;
    }

    private void setView()
    {
        input_layout_drop_shipper_name = (TextInputLayout) findViewById(R.id.input_layout_drop_shipper_name);
        input_layout_drop_shipper_phone_number = (TextInputLayout) findViewById(R.id.input_layout_drop_shipper_phone_number);
        input_layout_drop_shipper_address = (TextInputLayout) findViewById(R.id.input_layout_drop_shipper_address);
        et_drop_shipper_name = (TextInputEditText) findViewById(R.id.et_drop_shipper_name);
        et_drop_shipper_phone_number = (TextInputEditText) findViewById(R.id.et_drop_shipper_phone_number);
        et_drop_shipper_address = (TextInputEditText) findViewById(R.id.et_drop_shipper_address);
        tv_show_optional_content = (TextView) findViewById(R.id.tv_show_optional_content);
        ll_optional_content = (LinearLayout) findViewById(R.id.ll_optional_content);
    }

    private void setEvent()
    {
        tv_show_optional_content.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showOptionalContent();
                mIsOptionalOpened = true;
            }
        });
    }

    private void setUIState()
    {
        if (getStepListener() != null && getStepListener().isView())
        {
            et_drop_shipper_name.setEnabled(false);
            et_drop_shipper_address.setEnabled(false);
            et_drop_shipper_phone_number.setEnabled(false);
            showOptionalContent();
        }
        else
        {
            if (mIsOptionalOpened)
                showOptionalContent();
            else
                hideOptionalContent();
        }
    }

    private void showOptionalContent()
    {
        ll_optional_content.setVisibility(View.VISIBLE);
        tv_show_optional_content.setVisibility(View.GONE);
    }

    private void hideOptionalContent()
    {
        ll_optional_content.setVisibility(View.GONE);
        tv_show_optional_content.setVisibility(View.VISIBLE);
    }
}
