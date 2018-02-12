package coid.customer.pickupondemand.jet.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseItemSelectDialogFragment;
import coid.customer.pickupondemand.jet.model.Config;
import coid.customer.pickupondemand.jet.model.PackagingItem;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.model.Product;
import coid.customer.pickupondemand.jet.utility.NumberFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupItemStep2Fragment extends PickupItemStepFragment implements PickupItemFragment.PickupItemFragmentDataListener
{
    private static final int PRODUCT_REQUEST_CODE = 301;
    private static final int PACKAGING_ITEM_REQUEST_CODE = 302;
    private LinearLayout ll_optional_content;
    private TextInputLayout input_layout_weight, input_layout_length, input_layout_width, input_layout_height, input_layout_product, input_layout_packaging_item, input_layout_item_value, input_layout_description;
    private TextInputEditText et_weight, et_length, et_width, et_height, et_product, et_packaging_item, et_item_value, et_description;
    private TextView tv_volume_label, tv_volume, tv_show_optional_content;
    private CheckBox checkbox_insured;
    private ProgressBar progress_bar_product, progress_bar_packaging_item;
    private Product mSelectedProduct;
    private PackagingItem mSelectedPackagingItem;
    private List<Product> mProductList;
    private List<PackagingItem> mPackagingItemList;
    private Boolean mIsOptionalOpened = false;

    public PickupItemStep2Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getStepListener() != null)
        {
            mSelectedProduct = new Product();
            mSelectedProduct.setCode(mStepPickupItem.getProductCode());
            mSelectedProduct.setDescription(mStepPickupItem.getProductName());
            mSelectedPackagingItem = new PackagingItem();
            mSelectedPackagingItem.setCode(mStepPickupItem.getPackagingItemCode());
            mSelectedPackagingItem.setName(mStepPickupItem.getPackagingItemName());
        }

        mProductList = new ArrayList<>();
        mPackagingItemList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_item_step2, container, false);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            CharSequence code = data.getStringExtra(BaseItemSelectDialogFragment.SELECTED_ITEM_CODE_ARGS_PARAM);
            CharSequence description = data.getStringExtra(BaseItemSelectDialogFragment.SELECTED_ITEM_DESCRIPTION_ARGS_PARAM);
            switch (requestCode)
            {
                case PRODUCT_REQUEST_CODE :
                    mSelectedProduct = new Product(code.toString(), description.toString());
                    et_product.setText(description.toString());
                    break;
                case PACKAGING_ITEM_REQUEST_CODE :
                    mSelectedPackagingItem = new PackagingItem(code.toString(), description.toString());
                    et_packaging_item.setText(description);
                    break;
                default: break;
            }
        }
    }

    @Override
    protected void setValue(PickupItem pickupItem)
    {
        et_weight.setText(pickupItem.getWeightString());
        et_length.setText(pickupItem.getLengthString());
        et_width.setText(pickupItem.getWidthString());
        et_height.setText(pickupItem.getHeightString());
        et_product.setText(pickupItem.getFormattedProductName());
        et_packaging_item.setText(pickupItem.getPackagingItemName());
        checkbox_insured.setChecked(pickupItem.getInsured());
        et_item_value.setText(pickupItem.getItemValueString());
        et_description.setText(pickupItem.getDescription());

        String volumeLabel = getString(R.string.pod_pickup_item_volume) + " (cm3)";
        SpannableString spannableVolumeLabel = new SpannableString(volumeLabel);
        spannableVolumeLabel.setSpan(new SuperscriptSpan(), volumeLabel.length() - 2, volumeLabel.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableVolumeLabel.setSpan(new RelativeSizeSpan(0.5f), volumeLabel.length() - 2, volumeLabel.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_volume_label.setText(spannableVolumeLabel);

        calculateVolume();
    }

    @Override
    protected void updatePickupItem()
    {
        if (getView() == null)
            return;

        mStepPickupItem.setWeight(getWeight());
        mStepPickupItem.setLength(getDimensionLength());
        mStepPickupItem.setWidth(getDimensionWidth());
        mStepPickupItem.setHeight(getDimensionHeight());
        mStepPickupItem.setProductCode(mSelectedProduct.getCode());
        mStepPickupItem.setProductName(mSelectedProduct.getDescription());
        mStepPickupItem.setPackagingItemCode(mSelectedPackagingItem.getCode());
        mStepPickupItem.setPackagingItemName(mSelectedPackagingItem.getName());
        mStepPickupItem.setInsured(checkbox_insured.isChecked());
        if (checkbox_insured.isChecked())
            mStepPickupItem.setItemValue(NumberFormatter.stringToDouble(et_item_value.getText().toString().trim()));
        mStepPickupItem.setDescription(et_description.getText().toString().trim());
    }

    @Override
    public void onGetProductListStart()
    {
        showProductProgressBar();
    }

    @Override
    public void onGetProductListSuccess(List<Product> productList)
    {
        mProductList = productList;
    }

    @Override
    public void hideProductListProgress()
    {
        hideProductProgressBar();
        et_product.setOnClickListener(null);
        et_product.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProductItemSelectDialogFragment dialog = ProductItemSelectDialogFragment.newInstance(mProductList);
                dialog.setTargetFragment(mFragment, PRODUCT_REQUEST_CODE);
                dialog.show(getFragmentManager(), ProductItemSelectDialogFragment.class.getSimpleName());
            }
        });
    }

    @Override
    public void onGetPackagingItemListStart()
    {
        showPackagingItemProgressBar();
    }

    @Override
    public void onGetPackagingItemListSuccess(List<PackagingItem> packagingItemList)
    {
        mPackagingItemList.clear();
        mPackagingItemList.add(PackagingItem.getDefaultPackaging());
        mPackagingItemList.addAll(packagingItemList);
    }

    @Override
    public void hidePackagingItemListProgress()
    {
        hidePackagingItemProgressBar();
        et_packaging_item.setOnClickListener(null);
        et_packaging_item.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PackagingItemSelectDialogFragment dialog = PackagingItemSelectDialogFragment.newInstance(mPackagingItemList);
                dialog.setTargetFragment(mFragment, PACKAGING_ITEM_REQUEST_CODE);
                dialog.show(getFragmentManager(), PackagingItemSelectDialogFragment.class.getSimpleName());
            }
        });
    }

    @Override
    protected Boolean isValid(PickupItem pickupItem)
    {
        int errorCount = 0;

        if (Utility.Validation.isEditTextEmpty(input_layout_product, et_product))
            errorCount++;

//        if (errorCount > 0 )
//        {
//            et_weight.addTextChangedListener(Utility.Validation.getValidateNominalTextWatcher(input_layout_weight, et_weight, 0.1D));
//            et_length.addTextChangedListener(Utility.Validation.getValidateNominalTextWatcher(input_layout_length, et_length, 0.1D));
//            et_width.addTextChangedListener(Utility.Validation.getValidateNominalTextWatcher(input_layout_width, et_width, 0.1D));
//            et_height.addTextChangedListener(Utility.Validation.getValidateNominalTextWatcher(input_layout_height, et_height, 0.1D));
//            et_product.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_product, et_product));
//            et_packaging_item.addTextChangedListener(Utility.Validation.getValidateEmptyTextWatcher(input_layout_packaging_item, et_packaging_item));
//            if (checkbox_insured.isChecked())
//            {
//                et_item_value.addTextChangedListener(Utility.Validation.getValidateNominalTextWatcher(input_layout_item_value, et_item_value, 0D));
//            }
//        }

        return errorCount == 0;
    }

    private void setView()
    {
        ll_optional_content = (LinearLayout) findViewById(R.id.ll_optional_content);
        input_layout_weight = (TextInputLayout) findViewById(R.id.input_layout_weight);
        input_layout_length = (TextInputLayout) findViewById(R.id.input_layout_length);
        input_layout_width = (TextInputLayout) findViewById(R.id.input_layout_width);
        input_layout_height = (TextInputLayout) findViewById(R.id.input_layout_height);
        input_layout_product = (TextInputLayout) findViewById(R.id.input_layout_product);
        input_layout_packaging_item = (TextInputLayout) findViewById(R.id.input_layout_packaging_item);
        input_layout_item_value = (TextInputLayout) findViewById(R.id.input_layout_item_value);
        input_layout_description = (TextInputLayout) findViewById(R.id.input_layout_description);
        et_weight = (TextInputEditText) findViewById(R.id.et_weight);
        et_length = (TextInputEditText) findViewById(R.id.et_length);
        et_width = (TextInputEditText) findViewById(R.id.et_width);
        et_height = (TextInputEditText) findViewById(R.id.et_height);
        et_product = (TextInputEditText) findViewById(R.id.et_product);
        et_packaging_item = (TextInputEditText) findViewById(R.id.et_packaging_item);
        et_item_value = (TextInputEditText) findViewById(R.id.et_item_value);
        et_description = (TextInputEditText) findViewById(R.id.et_description);
        tv_volume_label = (TextView) findViewById(R.id.tv_volume_label);
        tv_volume = (TextView) findViewById(R.id.tv_volume);
        tv_show_optional_content = (TextView) findViewById(R.id.tv_show_optional_content);
        checkbox_insured = (CheckBox) findViewById(R.id.checkbox_insured);
        progress_bar_product = (ProgressBar) findViewById(R.id.progress_bar_product);
        progress_bar_packaging_item = (ProgressBar) findViewById(R.id.progress_bar_packaging_item);
    }

    private void setEvent()
    {
        if (getStepListener() != null && !getStepListener().isView())
        {
            TextWatcher volumeTextWatcher = new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){}
                @Override
                public void afterTextChanged(Editable s)
                {
                    calculateVolume();
                }
            };
            et_length.addTextChangedListener(volumeTextWatcher);
            et_width.addTextChangedListener(volumeTextWatcher);
            et_height.addTextChangedListener(volumeTextWatcher);

            checkbox_insured.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                        input_layout_item_value.setVisibility(View.VISIBLE);
                    else
                        input_layout_item_value.setVisibility(View.GONE);
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
    }

    private void setUIState()
    {
        if (getStepListener() != null && getStepListener().isView())
        {
            et_weight.setEnabled(false);
            et_length.setEnabled(false);
            et_width.setEnabled(false);
            et_height.setEnabled(false);
            et_product.setEnabled(false);
            et_packaging_item.setEnabled(false);
            progress_bar_product.setVisibility(View.GONE);
            progress_bar_packaging_item.setVisibility(View.GONE);
            et_item_value.setEnabled(false);
            checkbox_insured.setEnabled(false);
            et_description.setEnabled(false);
            showOptionalContent();
        }
        else
        {
            if (mIsOptionalOpened)
                showOptionalContent();
            else
                hideOptionalContent();
        }

        if (mStepPickupItem.getInsured())
            input_layout_item_value.setVisibility(View.VISIBLE);
        else
            input_layout_item_value.setVisibility(View.GONE);
    }

    private void calculateVolume()
    {
        String volumeString = String.valueOf(getVolume()) + " cm3";
        SpannableString spannableVolumeString = new SpannableString(volumeString);
        spannableVolumeString.setSpan(new SuperscriptSpan(), volumeString.length() - 1, volumeString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableVolumeString.setSpan(new RelativeSizeSpan(0.5f), volumeString.length() - 1, volumeString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_volume.setText(spannableVolumeString);
    }

    public Double getVolume()
    {
        return getDimensionLength() * getDimensionWidth() * getDimensionHeight();
    }

    public Double getWeight()
    {
        return NumberFormatter.stringToDouble(et_weight.getText().toString().trim());
    }

    public Double getDimensionLength()
    {
        return NumberFormatter.stringToDouble(et_length.getText().toString().trim());
    }
    public Double getDimensionWidth()
    {
        return NumberFormatter.stringToDouble(et_width.getText().toString().trim());
    }
    public Double getDimensionHeight()
    {
        return NumberFormatter.stringToDouble(et_height.getText().toString().trim());
    }

    public void showProductProgressBar()
    {
        if (getStepListener() != null && getStepListener().isView())
            progress_bar_product.setVisibility(View.GONE);
        else
            progress_bar_product.setVisibility(View.VISIBLE);
        et_product.setEnabled(false);
    }

    public void hideProductProgressBar()
    {
        progress_bar_product.setVisibility(View.GONE);
        if (getStepListener() != null && getStepListener().isView())
            et_product.setEnabled(false);
        else
            et_product.setEnabled(true);
    }

    public void showPackagingItemProgressBar()
    {
        if (getStepListener() != null && getStepListener().isView())
            progress_bar_packaging_item.setVisibility(View.GONE);
        else
            progress_bar_packaging_item.setVisibility(View.VISIBLE);
        et_packaging_item.setEnabled(false);
    }

    public void hidePackagingItemProgressBar()
    {
        progress_bar_packaging_item.setVisibility(View.GONE);
        if (getStepListener() != null && getStepListener().isView())
            et_packaging_item.setEnabled(false);
        else
            et_packaging_item.setEnabled(true);
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