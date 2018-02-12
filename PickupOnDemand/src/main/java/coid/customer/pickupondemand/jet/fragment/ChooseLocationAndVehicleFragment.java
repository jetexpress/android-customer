package coid.customer.pickupondemand.jet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.custom.ChooseVehicleBottomView;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.Vehicle;

public class ChooseLocationAndVehicleFragment extends ChooseLocationFragment
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";
    private Pickup mPickup;

    public ChooseLocationAndVehicleFragment()
    {

    }

    public static ChooseLocationAndVehicleFragment newInstance(Pickup pickup)
    {
        ChooseLocationAndVehicleFragment fragment = new ChooseLocationAndVehicleFragment();
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

    @Nullable
    @Override
    public View onCreateBottomView(LayoutInflater inflater, ViewGroup container)
    {
        return new ChooseVehicleBottomView(mContext)
        {
            @Override
            public void onBikeEnabled(View view)
            {
                mPickup.setVehicleCode(Vehicle.BIKE.getCode());
            }

            @Override
            public void onCarEnabled(View view)
            {
                mPickup.setVehicleCode(Vehicle.CAR.getCode());
            }
        };
    }

    @Override
    public void onBottomViewCreated(View view)
    {
        if (view instanceof ChooseVehicleBottomView)
            ((ChooseVehicleBottomView)view).setEstimatedPrice(mPickup.getTotalFee());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(getString(R.string.pod_choose_pickup_location));
        setAreaLabel(Utility.Message.get(R.string.pod_pickup_location));
        setCenterMarkerOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPickup.setLatitude(getCurrentLatLng().latitude);
                mPickup.setLongitude(getCurrentLatLng().longitude);
                mPickup.setAddress(getAreaName());
                getNavigator().showFragment(PickupDetailConfirmationFragment.newInstance(mPickup));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        super.onMapReady(googleMap);
        if (getStartingLatLng() != null)
            getMapFragment().moveCamera(getStartingLatLng(), 15f);
        else
            getMapFragment().moveCameraToOriginPosition(15f);
    }

    @Override
    protected LatLng getStartingLatLng()
    {
        if (!mPickup.hasLocation())
            return null;
        return new LatLng(mPickup.getLatitude(), mPickup.getLongitude());
    }
}
