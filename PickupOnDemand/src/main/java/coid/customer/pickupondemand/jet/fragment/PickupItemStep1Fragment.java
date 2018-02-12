package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseActivity;
import coid.customer.pickupondemand.jet.model.PickupItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupItemStep1Fragment extends PickupItemStepFragment
        implements ChooseLocationFragment.ChooseLocationFragmentListener,
        BaseActivity.ILocationPermissionListener
{
    private TextInputLayout input_layout_consignee_jet_id, input_layout_consignee_name, input_layout_consignee_phone_number, input_layout_consignee_address, input_layout_consignee_address_detail;
    private TextInputEditText et_consignee_jet_id, et_consignee_name, et_consignee_phone_number, et_consignee_address, et_consignee_address_detail;
    private TextView tv_show_optional_content;
    private LinearLayout ll_optional_content;
    private Boolean mIsOptionalOpened = false;
//    private LatLng mLatLng;

    public PickupItemStep1Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_item_step1, container, false);
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
    protected void updatePickupItem()
    {
        if (getView() == null)
            return;

        mStepPickupItem.setConsigneeJetIDCode(et_consignee_jet_id.getText().toString().trim());
        mStepPickupItem.setConsigneeName(et_consignee_name.getText().toString().trim());
        mStepPickupItem.setConsigneePhone(et_consignee_phone_number.getText().toString().trim());
        mStepPickupItem.setConsigneeAddress(et_consignee_address.getText().toString().trim());
//        if (mLatLng != null)
//        {
//            mStepPickupItem.setConsigneeLatitude(mLatLng.latitude);
//            mStepPickupItem.setConsigneeLongitude(mLatLng.longitude);
//        }
        mStepPickupItem.setConsigneeAddressDetail(et_consignee_address_detail.getText().toString().trim());
    }

    @Override
    protected void setValue(PickupItem pickupItem)
    {
        et_consignee_name.setText(pickupItem.getConsigneeName());
        et_consignee_phone_number.setText(pickupItem.getConsigneePhone());
        et_consignee_address_detail.setText(pickupItem.getConsigneeAddressDetail());
        et_consignee_jet_id.setText(pickupItem.getConsigneeJetIDCode());
        et_consignee_address.setText(pickupItem.getConsigneeAddress());
    }

    @Override
    protected Boolean isValid(PickupItem pickupItem)
    {
        int errorCount = 0;

        if (Utility.Validation.isEditTextEmpty(input_layout_consignee_address_detail, et_consignee_address_detail))
            errorCount++;
        if (Utility.Validation.isEditTextEmpty(input_layout_consignee_phone_number, et_consignee_phone_number))
            errorCount++;
        if (Utility.Validation.isEditTextEmpty(input_layout_consignee_name, et_consignee_name))
            errorCount++;

//        if (errorCount > 0)
//        {
//            et_consignee_name.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_consignee_name, et_consignee_name));
//            et_consignee_phone_number.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_consignee_phone_number, et_consignee_phone_number));
//            et_consignee_address.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_consignee_address, et_consignee_address));
//        }

        return errorCount == 0;
    }

    @Override
    public void onChooseLocation(LatLng latLng, String areaName)
    {
        if (getStepListener() != null)
            getStepListener().showStepper();

        mStepPickupItem.setConsigneeLatitude(latLng.latitude);
        mStepPickupItem.setConsigneeLongitude(latLng.longitude);

//        mStepPickupItem.setConsigneeAddress(areaName);
        et_consignee_address.setText(areaName);
    }

    @Override
    public void onCancelLocation()
    {
        if (getStepListener() != null)
            getStepListener().showStepper();
    }

    @Override
    public void onLocationPermissionGranted()
    {
        chooseAddressMapLocation();
    }

    @Override
    public void onLocationPermissionDenied()
    {

    }

    private void setView()
    {
        input_layout_consignee_jet_id = (TextInputLayout) findViewById(R.id.input_layout_consignee_jet_id);
        input_layout_consignee_name = (TextInputLayout) findViewById(R.id.input_layout_consignee_name);
        input_layout_consignee_phone_number = (TextInputLayout) findViewById(R.id.input_layout_consignee_phone_number);
        input_layout_consignee_address = (TextInputLayout) findViewById(R.id.input_layout_consignee_address);
        input_layout_consignee_address_detail = (TextInputLayout) findViewById(R.id.input_layout_consignee_address_detail);
        et_consignee_jet_id = (TextInputEditText) findViewById(R.id.et_consignee_jet_id);
        et_consignee_name = (TextInputEditText) findViewById(R.id.et_consignee_name);
        et_consignee_phone_number = (TextInputEditText) findViewById(R.id.et_consignee_phone_number);
        et_consignee_address = (TextInputEditText) findViewById(R.id.et_consignee_address);
        et_consignee_address_detail = (TextInputEditText) findViewById(R.id.et_consignee_address_detail);
        tv_show_optional_content = (TextView) findViewById(R.id.tv_show_optional_content);
        ll_optional_content = (LinearLayout) findViewById(R.id.ll_optional_content);
    }

    private void setEvent()
    {
        et_consignee_address.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updatePickupItem();
//                chooseAddressMapLocation();
                getBaseActivity().setLocationPermissionListener(PickupItemStep1Fragment.this);
                getBaseActivity().checkLocationPermission();
            }
        });

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
            et_consignee_jet_id.setEnabled(false);
            et_consignee_name.setEnabled(false);
            et_consignee_phone_number.setEnabled(false);
            et_consignee_address.setEnabled(false);
            et_consignee_address_detail.setEnabled(false);
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

    public void chooseAddressMapLocation()
    {
        if (getStepListener() != null)
            getStepListener().hideStepper();

        LatLng latLng = null;
        if (mStepPickupItem.getConsigneeLatitude() != null && mStepPickupItem.getConsigneeLongitude() != null)
            latLng = new LatLng(mStepPickupItem.getConsigneeLatitude(), mStepPickupItem.getConsigneeLongitude());

        updatePickupItem();

        ChooseLocationFragment chooseLocationFragment = ChooseLocationFragment.newInstance(latLng);
        chooseLocationFragment.setTargetFragment(mFragment, ChooseLocationFragment.CHOOSE_LOCATION_REQUEST_CODE);
        getNavigator().showFragment(chooseLocationFragment, getFragmentManager(), R.id.fl_step_container);
    }

    public void showOptionalContent()
    {
        ll_optional_content.setVisibility(View.VISIBLE);
        tv_show_optional_content.setVisibility(View.GONE);
    }

    public void hideOptionalContent()
    {
        ll_optional_content.setVisibility(View.GONE);
        tv_show_optional_content.setVisibility(View.VISIBLE);
    }


}
