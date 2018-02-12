package coid.customer.pickupondemand.jet.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.CourierLocation;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.map.Direction;
import coid.customer.pickupondemand.jet.request.CourierLastLocationRequest;
import coid.customer.pickupondemand.jet.request.GoogleMapDirectionRequest;
import coid.customer.pickupondemand.jet.request.PickupGetByCodeRequest;
import retrofit2.Response;

public class ObserveCourierFragment extends MapWithBottomViewFragment
{
    private static final String NOTIFICATION_PICKUP_CODE_ARGS_PARAM = "notificationPickupCodeParam";
    private static final String PICKUP_ARGS_PARAM = "pickupParam";

    private TextView tv_courier_sms, tv_courier_name, tv_courier_rating, tv_courier_call;
    private ImageView img_courier;

    private String mNotificationPickupCode;
    private Pickup mPickup;
    private List<LatLng> mLatLngList;
    private PickupGetByCodeRequest mPickupGetByCodeRequest;
    private CourierLastLocationRequest mCourierLastLocationRequest;
    private GoogleMapDirectionRequest mDirectionRequest;
    private GoogleMapDirectionRequest mFullDirectionRequest;
    private Marker mOriginLocationMarker;
    private CourierLocation mOriginCourierLocation;
    private CourierLocation mDestinationCourierLocation;

    public ObserveCourierFragment()
    {

    }

    public static ObserveCourierFragment newInstance(String notificationPickupCode)
    {
        ObserveCourierFragment fragment = new ObserveCourierFragment();
        Bundle args = new Bundle();
        args.putString(NOTIFICATION_PICKUP_CODE_ARGS_PARAM, notificationPickupCode);
        fragment.setArguments(args);
        return fragment;
    }

    public static ObserveCourierFragment newInstance(Pickup pickup)
    {
        ObserveCourierFragment fragment = new ObserveCourierFragment();
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
        {
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
            if (mPickup == null)
                mNotificationPickupCode = getArguments().getString(NOTIFICATION_PICKUP_CODE_ARGS_PARAM);
        }
        mLatLngList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateBottomView(LayoutInflater inflater, ViewGroup container)
    {
        return inflater.inflate(R.layout.fragment_observe_courier, container, false);
    }

    @Override
    public void onBottomViewCreated(View view)
    {
        setView(view);
        setValue();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(getString(R.string.pod_courier_on_the_way));
        setSearchAreaEnabled(false);
    }

    @Override
    public void onDestroy()
    {
        if (mFullDirectionRequest != null)
        {
            mFullDirectionRequest.clear();
            mFullDirectionRequest = null;
        }
        if (mCourierLastLocationRequest != null)
        {
            mCourierLastLocationRequest.stopInterval();
            mCourierLastLocationRequest.clear();
            mCourierLastLocationRequest = null;
        }
        if (mDirectionRequest != null)
        {
            mDirectionRequest.clear();
            mDirectionRequest = null;
        }
        if (mPickupGetByCodeRequest != null)
        {
            mPickupGetByCodeRequest.clear();
            mPickupGetByCodeRequest = null;
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        super.onMapReady(googleMap);
        setReadOnly(true);
        getMapFragment().getMap().getUiSettings().setAllGesturesEnabled(true);
        getMapFragment().removeOriginMarker();
        setCenterMarkerEnabled(false);
        if (mPickup != null)
            startObserve();
        else
            requestPickup();
    }

    @Override
    public void onMapClicked(LatLng latLng)
    {

    }

    @Override
    public void onCameraMove()
    {

    }

    @Override
    public void onCameraIdle()
    {

    }

    @Override
    protected LatLng getStartingLatLng()
    {
        return null;
    }

    private void setView(View view)
    {
        tv_courier_sms = (TextView) view.findViewById(R.id.tv_courier_sms);
        tv_courier_name = (TextView) view.findViewById(R.id.tv_courier_name);
        tv_courier_rating = (TextView) view.findViewById(R.id.tv_courier_rating);
        tv_courier_call = (TextView) view.findViewById(R.id.tv_courier_call);
        img_courier = (ImageView) view.findViewById(R.id.img_courier);
    }

    private void setValue()
    {
        if (mPickup != null && mPickup.getCourier() != null)
        {
            tv_courier_name.setText(mPickup.getCourier().getFullname());
            tv_courier_rating.setText(String.valueOf(mPickup.getCourier().getRating()));
            if (mPickup.getCourier().getProfilePictureUrl() != null && !mPickup.getCourier().getProfilePictureUrl().isEmpty())
                Picasso.with(mContext)
                        .load(mPickup.getCourier().getProfilePictureUrl())
                        .placeholder(R.drawable.ic_default_courier_photo)
                        .error(R.drawable.ic_default_courier_photo)
                        .into(img_courier);
        }
    }

    private void setEvent()
    {
        tv_courier_sms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
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
        img_courier.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getNavigator().showFragment(CourierDetailFragment.newInstance(mPickup));
            }
        });
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

    private void requestPickup()
    {
        if (mNotificationPickupCode != null && !mNotificationPickupCode.isEmpty())
        {
            mPickupGetByCodeRequest = new PickupGetByCodeRequest(mContext, mNotificationPickupCode)
            {
                @Override
                protected void showLoadingDialog(){}
                @Override
                protected void hideLoadingDialog(){}
                @Override
                protected void onSuccessOnUIThread(Response<Pickup> response)
                {
                    mPickup = response.body();
                    setValue();
                    startObserve();
                }

                @Override
                protected void onResponseFailedOnUIThread(Response<Pickup> response)
                {
                    showToast(Utility.Message.getResponseFailedMessage(R.string.pod_get_courier_failed, response));
                }

                @Override
                protected void onFailedOnUIThread(Exception ex)
                {
                    showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_get_courier_failed, ex));
                }

                @Override
                protected void onTimeOutOnUIThread()
                {
                    showToast(getString(R.string.pod_request_timed_out));
                }
            };
            mPickupGetByCodeRequest.executeAsync();
        }
    }

    private void startObserve()
    {
        if (mPickup == null)
            return;

        mCourierLastLocationRequest = new CourierLastLocationRequest(mContext, mPickup.getCourier().getUserId())
        {
            @Override
            protected void showLoadingDialog(){}
            @Override
            protected void hideLoadingDialog(){}
            @Override
            protected void onSuccessOnUIThread(Response<CourierLocation> response)
            {
                if (response.body() == null)
                    return;

                if (mDestinationCourierLocation != null)
                    mOriginCourierLocation = mDestinationCourierLocation;
                else
                    mOriginCourierLocation = response.body();

                mDestinationCourierLocation = response.body();
                requestFullDirection(response.body());
            }
        };
        mCourierLastLocationRequest.executeInterval(AppConfig.LOCATION_LOG_INTERVAL_IN_MILLIS);
    }

    private void requestFullDirection(final CourierLocation courierLocation)
    {
        if (mPickup == null)
            return;

        if (mFullDirectionRequest != null && mFullDirectionRequest.isSuccess())
            requestDirection();
        else
        {
            mFullDirectionRequest = new GoogleMapDirectionRequest(mContext, courierLocation.getLatLng(), mPickup.getLatLng())
            {
                @Override
                protected void showLoadingDialog(){}
                @Override
                protected void hideLoadingDialog(){}
                @Override
                protected void onSuccessOnUIThread(Response<Direction> response)
                {
                    super.onSuccessOnUIThread(response);
                    initCourierMarker(courierLocation);
                    getMapFragment().drawDirectionLine(response.body());
                    requestDirection();
                }
            };
            mFullDirectionRequest.executeAsync();
        }
    }

    private void initCourierMarker(CourierLocation courierLocation)
    {
        if (mPickup == null)
            return;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(courierLocation.getLatLng());
        markerOptions.title(getString(R.string.pod_courier_position));
        if (mPickup.isCar())
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_truck_red));
        else
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_red));

        mOriginLocationMarker = getMapFragment().getMap().addMarker(markerOptions);
        getMapFragment().moveCamera(courierLocation.getLatLng(), 15f);
    }

    private void requestDirection()
    {
        mDirectionRequest = new GoogleMapDirectionRequest(mContext, mOriginCourierLocation.getLatLng(), mDestinationCourierLocation.getLatLng())
        {
            @Override
            protected void showLoadingDialog(){}
            @Override
            protected void hideLoadingDialog(){}
            @Override
            protected void onSuccessOnUIThread(Response<Direction> response)
            {
                super.onSuccessOnUIThread(response);
                mLatLngList = getMapFragment().getDirectionLatLngList(response.body());
                if (mCourierLastLocationRequest != null)
                    mCourierLastLocationRequest.setHalt(true);
                startMoveCourierMarker(0);
            }
        };
        mDirectionRequest.executeAsync();
    }

    private void startMoveCourierMarker(final Integer latLngListIndex)
    {
        if (mLatLngList == null || mLatLngList.size() <= 0 || latLngListIndex >= mLatLngList.size())
        {
//            mOriginLocation = getLocation(mOriginLocationMarker.getPosition());
            if (mCourierLastLocationRequest != null)
                mCourierLastLocationRequest.setHalt(false);
            return;
        }

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();

        final long duration = 500;

//        Log.d("ROTATION", "fromRotation = " + String.valueOf(fromRotation)
//                + ", toRotation = " + String.valueOf(toRotation)
//                + ", markerStartPosition = " + mOriginLocationMarker.getPosition().toString()
//                + ", destinationPosition = " + mLatLngList.get(latLngListIndex).toString()
//                + ", distance = " + getDistance(mOriginLocationMarker.getPosition(), mLatLngList.get(latLngListIndex))
//                + ", duration = " + String.valueOf(duration));

//        createLog(toRotation, fromRotation);

        final Interpolator interpolator = new LinearInterpolator();


        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

//                Log.d("ROTATE", "elapsed = " + String.valueOf(elapsed)
//                        + ", t = " + String.valueOf(t)
//                        + ", rot = " + String.valueOf(rot)
//                        + ", markerRot = " + String.valueOf(markerRot));

                if (t < 1.0)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
//                    Log.d("ROTATE", "POST AGAIN");
                }
                else
                {
                    moveMarker(latLngListIndex);
//                    Log.d("ROTATE", "MOVE MARKER");
                }
            }
        });
    }

    private void moveMarker(final Integer latLngListIndex)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
//        Projection proj = mGoogleMap.getProjection();
//        Point startPoint = proj.toScreenLocation(mOriginLocationMarker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final LatLng startLatLng = mOriginLocationMarker.getPosition();
        final LatLng destinationLatLng = new LatLng(mLatLngList.get(latLngListIndex).latitude, mLatLngList.get(latLngListIndex).longitude);
        final long movementSpeedDelay = 50;
        final long duration = (getDistance(startLatLng, destinationLatLng).longValue()) * movementSpeedDelay;
        if (duration == 0)
        {
            startMoveCourierMarker(latLngListIndex + 1);
            return;
        }
//        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * destinationLatLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * destinationLatLng.latitude + (1 - t)
                        * startLatLng.latitude;
//                Log.d("MOVE", "t = " + String.valueOf(t)
//                        + ", lat = " + String.valueOf(lat)
//                        + ", lng = " + String.valueOf(lng)
//                        + ", destinationPosition = " + mLatLngList.get(latLngListIndex).toString()
//                        + ", distance = " + getDistance(mOriginLocationMarker.getPosition(), mLatLngList.get(latLngListIndex))
//                        + ", duration = " + String.valueOf(duration));
                LatLng currentLatLng = new LatLng(lat, lng);
                mOriginLocationMarker.setPosition(currentLatLng);
                getMapFragment().moveCamera(mOriginLocationMarker.getPosition());
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOriginLocationMarker.getPosition(), 21));

                if (t < 1.0)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
                else
                {
                    startMoveCourierMarker(latLngListIndex + 1);
                }
            }
        });
    }

    public Float getDistance(LatLng originLatLng, LatLng destinationLatLng)
    {
        android.location.Location originLocation = getLocation(originLatLng);
        android.location.Location destinationLocation = getLocation(destinationLatLng);
        return originLocation.distanceTo(destinationLocation);
    }

    private android.location.Location getLocation(LatLng latLng)
    {
        android.location.Location l = new android.location.Location("");
        l.setLatitude(latLng.latitude);
        l.setLongitude(latLng.longitude);
        return  l;
    }
}
