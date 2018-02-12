package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.model.map.ReverseGeocode;
import coid.customer.pickupondemand.jet.request.GoogleMapReverseGeocodeRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseLocationFragment extends MapWithBottomViewFragment implements PlaceSelectionListener
{
    public static final int CHOOSE_LOCATION_REQUEST_CODE = 301;
    private static final String LAT_LNG_ARGS_PARAM = "latLngParam";
    private String mAreaName;
    private LatLng mLatLng;
    private LatLng mStartingLatLng;
    private ChooseLocationFragmentListener mChooseLocationFragmentListener;
    private GoogleMapReverseGeocodeRequest mGoogleMapReverseGeocodeRequest;

    public ChooseLocationFragment()
    {
        // Required empty public constructor
    }

    public static ChooseLocationFragment newInstance(@Nullable LatLng latLng)
    {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        if (latLng != null)
        {
            Bundle args = new Bundle();
            args.putParcelable(LAT_LNG_ARGS_PARAM, latLng);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mStartingLatLng = getArguments().getParcelable(LAT_LNG_ARGS_PARAM);

        if (getTargetFragment() != null && getTargetFragment() instanceof ChooseLocationFragmentListener)
            mChooseLocationFragmentListener = (ChooseLocationFragmentListener) getTargetFragment();
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        setCenterMarkerEnabled(true);
        setAreaLabel(Utility.Message.get(R.string.pod_consignee_location));
        setCenterMarkerOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mChooseLocationFragmentListener != null)
                {
                    mChooseLocationFragmentListener = (ChooseLocationFragmentListener) getTargetFragment();
                    mChooseLocationFragmentListener.onChooseLocation(mLatLng, mAreaName);
                }
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if (mGoogleMapReverseGeocodeRequest != null)
        {
            mGoogleMapReverseGeocodeRequest.clear();
            mGoogleMapReverseGeocodeRequest = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (mChooseLocationFragmentListener != null)
        {
            if (getTargetFragment() instanceof BaseFragment)
                ((BaseFragment) getTargetFragment()).onBackPressed();
            mChooseLocationFragmentListener.onCancelLocation();
        }
        else
            super.onBackPressed();
    }

    @Override
    public void onArrowBackPressed()
    {
        if (mChooseLocationFragmentListener != null)
        {
            if (getTargetFragment() instanceof BaseFragment)
                ((BaseFragment) getTargetFragment()).onArrowBackPressed();
            mChooseLocationFragmentListener.onCancelLocation();
        }
        else
            super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        super.onMapReady(googleMap);

        setPlaceSelectionListener(this);

        if (getMapFragment().getOriginLatLng() != null)
            onCameraIdle();
    }

    @Override
    public void onMapClicked(final LatLng latLng)
    {
    }

    @Override
    public void onCameraMove()
    {
        Log.d("CAMERA_MOVE", "MOVE");

        setCenterMarkerBackgroundVisible(false);
        setClickableCenterMarkerEnabled(false);
    }

    @Override
    public void onCameraIdle()
    {
        if (!isReadOnly())
            setCenterMarkerBackgroundVisible(true);
        else
            setCenterMarkerBackgroundVisible(false);

        if (!isCameraPositionChanged())
        {
            Log.d("CAMERA_IDLE", "EQUALS");
            setClickableCenterMarkerEnabled(true);
            return;
        }

        Log.d("CAMERA_IDLE", "POSITION CHANGED");

        if (mLatLng != null)
            mLatLng = getMapFragment().getCenterLatLng();
        else
        {
            if (getStartingLatLng() != null)
                mLatLng = getStartingLatLng();
            else
                mLatLng = getMapFragment().getOriginLatLng();
        }

        String latLngString = String.valueOf(mLatLng.latitude) + "," + String.valueOf(mLatLng.longitude);

        mGoogleMapReverseGeocodeRequest = new GoogleMapReverseGeocodeRequest(mContext, latLngString)
        {
            @Override
            protected void onStartOnUIThread()
            {
                setClickableCenterMarkerEnabled(false);
            }

            @Override
            protected void onSuccessOnUIThread(Response<ReverseGeocode> response)
            {
                super.onSuccessOnUIThread(response);
                ReverseGeocode reverseGeocode = response.body();

                if (reverseGeocode.getResults() != null && reverseGeocode.getResults().size() > 0)
                {
                    mAreaName = reverseGeocode.getResults().get(0).getFormattedAddress();
                    setClickableCenterMarkerEnabled(true);
                }
                else
                {
                    mAreaName = getString(R.string.pod_pickup_location_not_found);
                    setCenterMarkerError();
                }

                updateLocation(mAreaName, mLatLng);
            }
        };
        mGoogleMapReverseGeocodeRequest.cancel();
        mGoogleMapReverseGeocodeRequest.executeAsync();
    }

    @Override
    public void onPlaceSelected(Place place)
    {
        updateLocation(place.getName().toString(), place.getLatLng());
        getMapFragment().moveCamera(place.getLatLng(), 15f);
    }

    @Override
    public void onError(Status status)
    {
        Log.i("AUTO_COMPLETE_ERROR", status.getStatusMessage());
        showToast(status.getStatusMessage());
    }

    @Override
    protected LatLng getStartingLatLng()
    {
        if (mStartingLatLng == null)
            return getMapFragment().getOriginLatLng();
        return mStartingLatLng;
    }

    public String getAreaName()
    {
        return mAreaName;
    }

    private void updateLocation(String areaName, LatLng latLng)
    {
        mLatLng = latLng;
        setArea(areaName);
    }

    protected LatLng getCurrentLatLng()
    {
        return mLatLng;
    }

    private Boolean isCameraPositionChanged()
    {
        return mLatLng == null || !mLatLng.equals(getMapFragment().getCenterLatLng());
    }

    private Boolean isOriginPosition()
    {
        return mLatLng != null && mLatLng.equals(getMapFragment().getOriginLatLng());
    }

    public interface ChooseLocationFragmentListener
    {
        void onChooseLocation(LatLng latLng, String areaName);
        void onCancelLocation();
    }
}
