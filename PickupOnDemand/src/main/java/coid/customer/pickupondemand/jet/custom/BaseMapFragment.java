package coid.customer.pickupondemand.jet.custom;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.model.map.Direction;
import coid.customer.pickupondemand.jet.model.map.Step;
import coid.customer.pickupondemand.jet.request.GoogleMapDirectionRequest;

public class BaseMapFragment
        extends SupportMapFragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener
{
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private Location mOriginLocation;
    private LatLng mStartingLatLng;
    private Marker mOriginLocationMarker;
    private IMapListener mIMapListener;
    private Polyline mCurrentPolyline;
    private Context mContext;
    private GoogleMapDirectionRequest mGoogleMapDirectionRequest;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mContext = getContext();
    }

    @Override
    public void onStop()
    {
        if (mGoogleMapDirectionRequest != null)
            mGoogleMapDirectionRequest.cancel();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        if (mGoogleMapDirectionRequest != null)
        {
            mGoogleMapDirectionRequest.cancel();
            mGoogleMapDirectionRequest.clear();
            mGoogleMapDirectionRequest = null;
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        try
        {
            mOriginLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        catch (SecurityException ex)
        {
            Log.d("LOCATION", "NOT FOUND");
        }
        getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.d("MAP", "SUSPENDED");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d("MAP", "FAILED");
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraIdleListener(this);

        try
        {
            mOriginLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            if (mOriginLocation == null)
//                mOriginLocation = getDefaultLocation();
        }
        catch (SecurityException ex)
        {
            Log.d("LOCATION", "NOT FOUND");
        }

        if (mStartingLatLng != null)
        {
            moveCamera(mStartingLatLng);
            zoomCamera(15f);
        }
        else
        {
            if (mOriginLocation != null)
            {
                moveCamera(getOriginLatLng());
                zoomCamera(15f);
            }
        }

        if (mOriginLocation != null)
            updateOriginLocationMarker(getOriginLatLng());

        if (mIMapListener != null)
            mIMapListener.onMapReady(googleMap);
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        if (mIMapListener != null)
            mIMapListener.onMapClicked(latLng);
    }

    @Override
    public void onCameraMove()
    {
        if (mIMapListener != null)
            mIMapListener.onCameraMove();
    }

    @Override
    public void onCameraIdle()
    {
        if (mIMapListener != null)
            mIMapListener.onCameraIdle();
    }

    private synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //TODO:
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                //(just doing it here for now, note that with this code, no explanation is shown)
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            if (mGoogleApiClient == null)
            {
                buildGoogleApiClient();
            }
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                    }
                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getDirection(LatLng destinationLatLng)
    {
        if (mOriginLocation != null)
        {
            String origin = String.valueOf(mOriginLocation.getLatitude()) + "," + String.valueOf(mOriginLocation.getLongitude());
            String destination = String.valueOf(destinationLatLng.latitude) + "," + String.valueOf(destinationLatLng.longitude);
            Log.d("URL", "https://maps.googleapis.com/maps/api/directions/json?origin= " + origin + "&destination=" + destination);
            mGoogleMapDirectionRequest = new GoogleMapDirectionRequest(mContext, origin, destination);
            mGoogleMapDirectionRequest.executeAsync();
        }
    }

    public void drawDirectionLine(Direction direction)
    {
        if (direction == null
                || direction.getRoutes() == null
                || direction.getRoutes().size() <= 0
                || direction.getRoutes().get(0).getLegs() == null
                || direction.getRoutes().get(0).getLegs().size() <= 0
                || direction.getRoutes().get(0).getLegs().get(0).getSteps() == null
                || direction.getRoutes().get(0).getLegs().get(0).getSteps().size() <= 0)
            return;

        if (mCurrentPolyline != null)
            mCurrentPolyline.remove();

        PolylineOptions rectLine = new PolylineOptions().width(10).color(
                Color.BLUE);

        Log.d("JET_127", direction.toString());

        for (Step step : direction.getRoutes().get(0).getLegs().get(0).getSteps())
        {
            LatLng latLng = new LatLng(step.getStartLocation().getLat(), step.getStartLocation().getLng());
            rectLine.add(latLng);

            ArrayList<LatLng> arr = step.getPolyline().getDecodedPolyline();
            for (int j = 0; j < arr.size(); j++) {
                latLng = new LatLng(arr.get(j).latitude, arr.get(j).longitude);
                rectLine.add(latLng);
            }

            latLng = new LatLng(step.getEndLocation().getLat(), step.getEndLocation().getLng());
            rectLine.add(latLng);
        }

        mCurrentPolyline = mGoogleMap.addPolyline(rectLine);
    }

    public List<LatLng> getDirectionLatLngList(Direction direction)
    {
        if (direction == null
                || direction.getRoutes() == null
                || direction.getRoutes().size() <= 0
                || direction.getRoutes().get(0).getLegs() == null
                || direction.getRoutes().get(0).getLegs().size() <= 0
                || direction.getRoutes().get(0).getLegs().get(0).getSteps() == null
                || direction.getRoutes().get(0).getLegs().get(0).getSteps().size() <= 0)
            return null;

        Log.d("JET_127", direction.toString());

        List<LatLng> latLngList = new ArrayList<>();

        for (Step step : direction.getRoutes().get(0).getLegs().get(0).getSteps())
        {
            ArrayList<LatLng> arr = step.getPolyline().getDecodedPolyline();
            for (int j = 0; j < arr.size(); j++) {
                LatLng latLng = new LatLng(arr.get(j).latitude, arr.get(j).longitude);
                latLngList.add(latLng);
            }
        }

        return latLngList;
    }

    public void moveCameraToOriginPosition(Float zoomDepth)
    {
        if (getOriginLatLng() != null)
        {
            LatLng latLng = getOriginLatLng();
            moveCamera(latLng, zoomDepth);
        }
    }

    public void moveCamera(LatLng latLng)
    {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void zoomCamera(Float zoomDepth)
    {
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(zoomDepth));
    }

    public void moveCamera(LatLng latLng, Float zoomDepth)
    {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomDepth));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomDepth));
    }

    private Location getDefaultLocation()
    {
        LatLng latLng = new LatLng(-6.204394, 106.780494);
        return getLocation(latLng);
    }

    private Location getLocation(LatLng latLng)
    {
        Location l = new Location("");
        l.setLatitude(latLng.latitude);
        l.setLongitude(latLng.longitude);
        return  l;
    }

    public Marker addMarker(LatLng latLng, String title, BitmapDescriptor markerIcon)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.icon(markerIcon);
        return mGoogleMap.addMarker(markerOptions);
    }

    public LatLng getOriginLatLng()
    {
        if (mOriginLocation != null)
            return new LatLng(mOriginLocation.getLatitude(), mOriginLocation.getLongitude());
        else
            return null;
    }

    public GoogleMap getMap()
    {
        return mGoogleMap;
    }

    public void updateOriginLocationMarker(LatLng latLng)
    {
        removeOriginMarker();
        mOriginLocationMarker = addMarker(latLng, "Current Position", BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
    }

    public void removeOriginMarker()
    {
        if (mOriginLocationMarker != null)
            mOriginLocationMarker.remove();
    }

    public LatLng getCenterLatLng()
    {
        return mGoogleMap.getCameraPosition().target;
    }

    public interface IMapListener
    {
        void onMapReady(GoogleMap googleMap);
        void onMapClicked(LatLng latLng);
        void onCameraMove();
        void onCameraIdle();
    }

    public void setMapListener(IMapListener iMapListener)
    {
        mIMapListener = iMapListener;
    }
    public void setStartingLatLng(LatLng startingLatLng)
    {
        mStartingLatLng = startingLatLng;
    }
}
