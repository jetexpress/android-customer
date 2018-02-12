package coid.customer.pickupondemand.jet.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseActivity;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.custom.ImagePicker;
import coid.customer.pickupondemand.jet.model.Pickup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupImageFragment extends BaseFragment implements BaseActivity.ICameraPermissionListener
{
    private static final int IMG_REQUEST_CODE = 234;
    private static final String PICKUP_ARGS_PARAM = "PickupParam";
    private LinearLayout ll_bottom_container;
    private RelativeLayout rl_choose_pickup_location, rl_take_photo;
    private ImageView img_pickup_photo;
    private EstimatedPriceView mEstimatedPriceBottomView;
    private Pickup mPickup;
    private Bitmap mBitmapPickup;
    private Intent mPickupImageIntent;

    public PickupImageFragment()
    {
        // Required empty public constructor
    }

    public static PickupImageFragment newInstance(Pickup pickup)
    {
        PickupImageFragment fragment = new PickupImageFragment();
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
        ImagePicker.setMinQuality(AppConfig.IMAGE_PICKER_MIN_WIDTH, AppConfig.IMAGE_PICKER_MIN_HEIGHT);
        getBaseActivity().setCameraPermissionListener(this);
        getBaseActivity().checkCameraPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setState();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(mPickup.getCode());
    }

    @Override
    public void onDestroy()
    {
        getBaseActivity().clearCameraPermissionListener();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMG_REQUEST_CODE)
        {
            mBitmapPickup = ImagePicker.getImageFromResult(mContext, requestCode, resultCode, data);
            if (mBitmapPickup != null)
            {
                img_pickup_photo.setImageBitmap(mBitmapPickup);
            }
        }
    }

    @Override
    public void onCameraPermissionGranted()
    {
        mPickupImageIntent = ImagePicker.getChooserPickImageIntent(mContext, getString(R.string.pod_pickup_image_take_photo));
        startActivityForResult(mPickupImageIntent, IMG_REQUEST_CODE);
    }

    @Override
    public void onCameraPermissionDenied()
    {
        mPickupImageIntent = ImagePicker.getPickImageIntent(mContext, getString(R.string.pod_pickup_image_pick_photo));
        startActivityForResult(mPickupImageIntent, IMG_REQUEST_CODE);
    }

    private void setView()
    {
        mEstimatedPriceBottomView = new EstimatedPriceView(mContext);
        mEstimatedPriceBottomView.showPriceOnly();
        mEstimatedPriceBottomView.setEstimatedPrice(mPickup.getTotalFee());

        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        ll_bottom_container.addView(mEstimatedPriceBottomView, 0);

        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        rl_choose_pickup_location = (RelativeLayout) findViewById(R.id.rl_choose_pickup_location);
        rl_take_photo = (RelativeLayout) findViewById(R.id.rl_take_photo);
        img_pickup_photo = (ImageView) findViewById(R.id.img_pickup_photo);
    }

    private void setState()
    {
        if (mPickup.getImageBase64() != null && !mPickup.getImageBase64().isEmpty())
            img_pickup_photo.setImageBitmap(Utility.Image.base64StringToBitmap(mPickup.getImageBase64()));
    }

    private void setEvent()
    {
        rl_take_photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(mPickupImageIntent, IMG_REQUEST_CODE);
            }
        });

        rl_choose_pickup_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mBitmapPickup != null)
                {
                    mPickup.setImageBase64(Utility.Image.bitmapToBase64String(mBitmapPickup));
                    getNavigator().showFragment(ChooseLocationAndVehicleFragment.newInstance(mPickup));
                }
                else
                    showToast(getString(R.string.pod_pickup_image_required));
            }
        });
    }
}