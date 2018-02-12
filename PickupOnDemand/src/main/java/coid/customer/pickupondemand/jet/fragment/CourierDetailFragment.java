package coid.customer.pickupondemand.jet.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.request.PickupGetByCodeRequest;
import coid.customer.pickupondemand.jet.request.PickupRequestedCancelRequest;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourierDetailFragment extends BaseHasBasicLayoutFragment
{
    private static final String NOTIFICATION_PICKUP_CODE_ARGS_PARAM = "notificationPickupCodeParam";
    private static final String PICKUP_ARGS_PARAM = "pickupParam";
    private static final String READ_ONLY_ARGS_PARAM = "readOnlyParam";

    private RelativeLayout rl_content_container;
    private LinearLayout ll_bottom_container;
    private TextView tv_courier_cancel, tv_courier_sms, tv_courier_call, tv_courier_name;
    private RatingBar rb_courier;
    private CircleImageView img_courier;
    private String mNotificationPickupCode;
    private Pickup mPickup;
    private PickupGetByCodeRequest mPickupGetByCodeRequest;
    private PickupRequestedCancelRequest mPickupRequestedCancelRequest;
    private ICourierDetailFragmentListener mCourierDetailFragmentListener;
    private boolean mIsReadOnly;

    public CourierDetailFragment()
    {
        // Required empty public constructor
    }

    public static CourierDetailFragment newInstance(String notificationPickupCode)
    {
        CourierDetailFragment fragment = new CourierDetailFragment();
        Bundle args = new Bundle();
        args.putString(NOTIFICATION_PICKUP_CODE_ARGS_PARAM, notificationPickupCode);
        fragment.setArguments(args);
        return fragment;
    }

    public static CourierDetailFragment newInstance(Pickup pickup)
    {
        CourierDetailFragment fragment = new CourierDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        fragment.setArguments(args);
        return fragment;
    }

    public static CourierDetailFragment readOnlyInstance(Pickup pickup)
    {
        CourierDetailFragment fragment = new CourierDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        args.putBoolean(READ_ONLY_ARGS_PARAM, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mIsReadOnly = getArguments().getBoolean(READ_ONLY_ARGS_PARAM, false);
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
            if (mPickup == null)
                mNotificationPickupCode = getArguments().getString(NOTIFICATION_PICKUP_CODE_ARGS_PARAM);
        }

        if (getTargetFragment() instanceof ICourierDetailFragmentListener)
            mCourierDetailFragmentListener = (ICourierDetailFragmentListener) getTargetFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courier_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setUIState();
        setValue();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(getString(R.string.pod_courier_detail));
        if (mPickup == null)
            requestPickup();
    }

    @Override
    public void onDestroy()
    {
        if (mPickupRequestedCancelRequest != null)
        {
            mPickupRequestedCancelRequest.clear();
            mPickupRequestedCancelRequest = null;
        }
        if (mPickupGetByCodeRequest != null)
        {
            mPickupGetByCodeRequest.clear();
            mPickupGetByCodeRequest = null;
        }
        super.onDestroy();
    }

    @Override
    protected View getBaseContentLayout()
    {
        return rl_content_container;
    }

    @Override
    protected void onRetry()
    {

    }

    private void setView()
    {
        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        rl_content_container = (RelativeLayout) findViewById(R.id.rl_content_container);
        tv_courier_cancel = (TextView) findViewById(R.id.tv_courier_cancel);
        tv_courier_sms = (TextView) findViewById(R.id.tv_courier_sms);
        tv_courier_call = (TextView) findViewById(R.id.tv_courier_call);
        tv_courier_name = (TextView) findViewById(R.id.tv_courier_name);
        rb_courier = (RatingBar) findViewById(R.id.rb_courier);
        img_courier = (CircleImageView) findViewById(R.id.img_courier);
    }

    private void setUIState()
    {
        if (mIsReadOnly)
            ll_bottom_container.setVisibility(View.GONE);
        else
            ll_bottom_container.setVisibility(View.VISIBLE);
    }

    private void setEvent()
    {
        tv_courier_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                requestCancelPickup();
            }
        });
        tv_courier_sms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSMS();
            }
        });
        tv_courier_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doPhoneCall();
            }
        });
    }

    private void setValue()
    {
        if (mPickup != null && mPickup.getCourier() != null)
        {
            tv_courier_name.setText(mPickup.getCourier().getFullname());
            rb_courier.setRating(mPickup.getCourier().getRating());
            if (mPickup.getCourier().getProfilePictureUrl() != null && !mPickup.getCourier().getProfilePictureUrl().isEmpty())
                Picasso.with(mContext)
                        .load(mPickup.getCourier().getProfilePictureUrl())
                        .placeholder(R.drawable.ic_default_courier_photo)
                        .error(R.drawable.ic_default_courier_photo)
                        .into(img_courier);
        }
    }

    private void requestPickup()
    {
        if (mNotificationPickupCode != null && !mNotificationPickupCode.isEmpty())
        {
            mPickupGetByCodeRequest = new PickupGetByCodeRequest(mContext, mNotificationPickupCode)
            {
                @Override
                protected void showLoadingDialog()
                {
                    showProgressBar();
                }

                @Override
                protected void hideLoadingDialog()
                {
                    showRetry(R.string.pod_request_timed_out);
                }

                @Override
                protected void onSuccessOnUIThread(Response<Pickup> response)
                {
                    mPickup = response.body();
                    if (mPickup != null && mPickup.getCourier() != null)
                    {
                        setValue();
                        showContent();
                    } else
                        showRetry(R.string.pod_request_timed_out);
                }
            };
            mPickupGetByCodeRequest.executeAsync();
        }
    }

    private void requestCancelPickup()
    {
        if (mPickup == null)
        {
            showToast(getString(R.string.pod_pickup_not_found));
            return;
        }

        mPickupRequestedCancelRequest = new PickupRequestedCancelRequest(mContext, mPickup.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                super.onSuccessOnUIThread(response);
                if (mCourierDetailFragmentListener != null)
                    mCourierDetailFragmentListener.onCourierDetailPickupCancelled(response.body());
                getNavigator().popResumeToDefaultFragment();
            }
        };
        mPickupRequestedCancelRequest.executeAsync();
    }

    private void doPhoneCall()
    {
        if (mPickup != null && mPickup.getCourier() != null && mPickup.getCourier().getPhoneNumber() != null && !mPickup.getCourier().getPhoneNumber().isEmpty())
        {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                showToast(getString(R.string.pod_failed_to_connect_phone_call));
                return;
            }

            String uri = "tel:" + mPickup.getCourier().getPhoneNumber().trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
        else
            showToast(getString(R.string.pod_empty_phone_number));
    }

    private void doSMS()
    {
        if (mPickup != null && mPickup.getCourier() != null && mPickup.getCourier().getPhoneNumber() != null && !mPickup.getCourier().getPhoneNumber().isEmpty())
        {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);

            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", mPickup.getCourier().getPhoneNumber().trim());

            try
            {
                startActivity(smsIntent);
            }
            catch (android.content.ActivityNotFoundException ex)
            {
                showToast(getString(R.string.pod_failed_to_sms));
            }
        }
        else
            showToast(getString(R.string.pod_empty_phone_number));
    }

    public interface ICourierDetailFragmentListener
    {
        void onCourierDetailPickupCancelled(Pickup pickup);
    }
}
