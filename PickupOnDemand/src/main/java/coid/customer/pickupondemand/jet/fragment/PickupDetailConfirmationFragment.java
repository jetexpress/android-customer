package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.DBQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupDetailConfirmationFragment extends BaseFragment
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";
    private Pickup mPickup;

    private LinearLayout ll_bottom_container;
    private RelativeLayout rl_continue;
    private TextInputLayout input_layout_quick_pickup_item_count, input_layout_pic, input_layout_phone, input_layout_sddress, input_layout_notes;
    private TextInputEditText et_quick_pickup_item_count, et_pic, et_phone, et_address, et_notes;
    private EstimatedPriceView mEstimatedPriceBottomView;

    public PickupDetailConfirmationFragment()
    {
        // Required empty public constructor
    }

    public static PickupDetailConfirmationFragment newInstance(Pickup pickup)
    {
        PickupDetailConfirmationFragment fragment = new PickupDetailConfirmationFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_detail_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setValue();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(mPickup.getCode());
    }

    private void setView()
    {
        mEstimatedPriceBottomView = new EstimatedPriceView(mContext);
        mEstimatedPriceBottomView.showPriceOnly();
        mEstimatedPriceBottomView.setEstimatedPrice(mPickup.getTotalFee());

        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        ll_bottom_container.addView(mEstimatedPriceBottomView, 0);

        rl_continue = (RelativeLayout) findViewById(R.id.rl_continue);
        input_layout_quick_pickup_item_count = (TextInputLayout) findViewById(R.id.input_layout_quick_pickup_item_count);
        input_layout_pic = (TextInputLayout) findViewById(R.id.input_layout_pic);
        input_layout_phone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        input_layout_sddress = (TextInputLayout) findViewById(R.id.input_layout_sddress);
        input_layout_notes = (TextInputLayout) findViewById(R.id.input_layout_notes);
        et_quick_pickup_item_count = (TextInputEditText) findViewById(R.id.et_quick_pickup_item_count);
        et_pic = (TextInputEditText) findViewById(R.id.et_pic);
        et_phone = (TextInputEditText) findViewById(R.id.et_phone);
        et_address = (TextInputEditText) findViewById(R.id.et_address);
        et_notes = (TextInputEditText) findViewById(R.id.et_notes);

        if (!mPickup.hasPickupItems())
        {
            input_layout_quick_pickup_item_count.setVisibility(View.VISIBLE);
            et_quick_pickup_item_count.setVisibility(View.VISIBLE);
        }
        else
        {
            input_layout_quick_pickup_item_count.setVisibility(View.GONE);
            et_quick_pickup_item_count.setVisibility(View.GONE);
        }
    }

    private void setValue()
    {
        UserProfile userProfile = DBQuery.getSingle(UserProfile.class);

        if (mPickup.getPic() != null && !mPickup.getPic().isEmpty())
            et_pic.setText(mPickup.getPic());
        else if (userProfile != null)
            et_pic.setText(userProfile.getFullName());

        if (mPickup.getPhone() != null && !mPickup.getPhone().isEmpty())
            et_phone.setText(mPickup.getPhone());
        else if (userProfile != null)
            et_phone.setText(userProfile.getPhoneNumber());

        if (mPickup.getAddress() != null && !mPickup.getAddress().isEmpty())
            et_address.setText(mPickup.getAddress());
        else if (userProfile != null)
            et_address.setText(userProfile.getAddress());

        et_notes.setText(mPickup.getAddressDetail());
    }

    private void setEvent()
    {
        rl_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!isValid())
                    return;

                String pic = et_pic.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                String address = et_address.getText().toString().trim();
                String notes = et_notes.getText().toString().trim();

                mPickup.setPic(pic);
                mPickup.setPhone(phone);
                mPickup.setAddress(address);
                mPickup.setAddressDetail(notes);

                if (!mPickup.hasPickupItems())
                    mPickup.setQuickPickupItemCount(Long.valueOf(et_quick_pickup_item_count.getText().toString()));

                getNavigator().showFragment(PickupLocationConfirmationFragment.newInstance(mPickup));
            }
        });
    }

    private Boolean isValid()
    {
        int errorCount = 0;

        if (Utility.Validation.isEditTextEmpty(input_layout_phone, et_phone))
            errorCount++;
        if (Utility.Validation.isEditTextEmpty(input_layout_pic, et_pic))
            errorCount++;
        if (!mPickup.hasPickupItems())
        {
            if (Utility.Validation.isNominalInvalid(input_layout_quick_pickup_item_count, et_quick_pickup_item_count, 0D))
                errorCount++;
        }

        return errorCount == 0;
    }
}
