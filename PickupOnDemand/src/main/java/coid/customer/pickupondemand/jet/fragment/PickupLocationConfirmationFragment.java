package coid.customer.pickupondemand.jet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.onesignal.OneSignal;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.custom.LocationConfirmationBottomView;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.request.PickupRequestedCreateRequest;
import retrofit2.Response;

public class PickupLocationConfirmationFragment extends ChooseLocationFragment
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";
    private Pickup mPickup;
    private PickupRequestedCreateRequest mPickupRequestedCreateRequest;
    private ICourierOrderListener mCourierOrderListener;

    public static PickupLocationConfirmationFragment newInstance(Pickup pickup)
    {
        PickupLocationConfirmationFragment fragment = new PickupLocationConfirmationFragment();
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
    public void onResume()
    {
        super.onResume();
        setTitle(Utility.Message.get(R.string.pod_pickup_location_confirmation));
        setAreaLabel(Utility.Message.get(R.string.pod_pickup_location));
    }

    @Override
    public void onStop()
    {
        if (mPickupRequestedCreateRequest != null)
            mPickupRequestedCreateRequest.cancel();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        if (mPickupRequestedCreateRequest != null)
        {
            mPickupRequestedCreateRequest.clear();
            mPickupRequestedCreateRequest = null;
        }

        mCourierOrderListener = null;

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateBottomView(LayoutInflater inflater, ViewGroup container)
    {
        return new LocationConfirmationBottomView(mContext)
        {
            @Override
            public void onCourierOrderClicked(View view)
            {
                orderCourier();
            }
        };
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof PickupOnDemandActivity)
            mCourierOrderListener = (ICourierOrderListener) context;
    }

    @Override
    public void onBottomViewCreated(View view)
    {
        if (view instanceof LocationConfirmationBottomView)
            ((LocationConfirmationBottomView)view).setEstimatedPrice(mPickup.getTotalFee());
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        super.onMapReady(googleMap);
        setReadOnly(true);
        getMapFragment().removeOriginMarker();
        getMapFragment().moveCamera(getStartingLatLng(), 15f);
    }

    @Override
    protected LatLng getStartingLatLng()
    {
        return new LatLng(mPickup.getLatitude(), mPickup.getLongitude());
    }

    private void orderCourier()
    {
        if (mCourierOrderListener == null)
            return;

//        showLoadingDialog();
//        OneSignal.sendTags(Utility.OneSignal.getTag());
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                hideLoadingDialog();
//                mPickup.setStatus(Pickup.STATUS_REQUESTED);
//                mCourierOrderListener.onCreatePickupSuccess(mPickup);
//            }
//        }, 3000);

        mPickupRequestedCreateRequest = new PickupRequestedCreateRequest(mContext, mPickup)
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                super.onSuccessOnUIThread(response);
                mPickup.update(response.body());
                mCourierOrderListener.onCreatePickupSuccess(response.body());
            }
        };

        Utility.OneSignal.sendTags();

        if (mPickup.isDraft())
            mPickupRequestedCreateRequest.executeAsync();
        else
            mCourierOrderListener.onCreatePickupSuccess(mPickup);
    }

    public interface ICourierOrderListener
    {
        void onCreatePickupSuccess(Pickup pickup);
    }
}
