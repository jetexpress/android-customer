package coid.customer.pickupondemand.jet.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.BaseDialogFragment;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.request.RateCourierRequest;
import retrofit2.Response;

public class RatePickupDialogFragment extends BaseDialogFragment
{
    private static final String PICKUP_ARGS_PARAM = "pickupParam";
    private Pickup mPickup;

    private ImageView img_courier;
    private TextView tv_courier_name;
    private RatingBar rb_courier;
    private RelativeLayout rl_submit;
    private RateCourierRequest mRateCourierRequest;

    public RatePickupDialogFragment()
    {
    }

    public static RatePickupDialogFragment newInstance(Pickup pickup)
    {
        RatePickupDialogFragment fragment = new RatePickupDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.JETBaseDialog);
        if (getArguments() != null)
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.dialog_fragment_rate_courier, container, false);
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
    public void onStart()
    {
        super.onStart();
        Dialog d = getDialog();
        if (d != null && d.getWindow() != null)
        {
            d.getWindow().setGravity(Gravity.CENTER);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialogInterface)
                {
                    dismiss();
                    getActivity().finish();
                }
            });
        }
    }

    @Override
    public void onDestroy()
    {
        if (mRateCourierRequest != null)
        {
            mRateCourierRequest.clear();
            mRateCourierRequest = null;
        }
        super.onDestroy();
    }

    private void setView()
    {
        img_courier = (ImageView) findViewById(R.id.img_courier);
        tv_courier_name = (TextView) findViewById(R.id.tv_courier_name);
        rb_courier = (RatingBar) findViewById(R.id.rb_courier);
        rl_submit = (RelativeLayout) findViewById(R.id.rl_submit);
    }

    private void setValue()
    {
        if (mPickup == null || mPickup.getCourier() == null)
            return;

        tv_courier_name.setText(mPickup.getCourier().getFullname());
        if (mPickup.getCourier().getProfilePictureUrl() != null && !mPickup.getCourier().getProfilePictureUrl().isEmpty())
            Picasso.with(mContext)
                    .load(mPickup.getCourier().getProfilePictureUrl())
                    .placeholder(R.drawable.ic_default_courier_photo)
                    .error(R.drawable.ic_default_courier_photo)
                    .into(img_courier);
    }

    private void setEvent()
    {
        rl_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                submit();
            }
        });
    }

    private void submit()
    {
        Float rating = rb_courier.getRating();
        mRateCourierRequest = new RateCourierRequest(mContext, rating, mPickup.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<Void> response)
            {
                super.onSuccessOnUIThread(response);
                dismiss();
            }
        };
        mRateCourierRequest.executeAsync();
    }
}
