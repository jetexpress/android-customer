package coid.customer.pickupondemand.jet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseActivity;
import coid.customer.pickupondemand.jet.base.BaseFragment;
import coid.customer.pickupondemand.jet.custom.BaseMapFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MapWithBottomViewFragment extends BaseFragment
        implements BaseMapFragment.IMapListener, BaseActivity.ILocationPermissionListener
{
    private FrameLayout fl_map_container;
    private LinearLayout ll_bottom_container, ll_choose_pickup_location_marker_container_clickable;
    private ProgressBar progress_bar_choose_location, progress_bar_area;
    private ImageView img_choose_location_not_found;
    private RelativeLayout rl_search_container, rl_choose_pickup_location_background;
    private TextView tv_area, tv_area_label;
    private RelativeLayout rl_choose_pickup_location_marker_container;
    private FloatingActionButton fab_my_location;

    private BaseMapFragment mMapFragment;
    private PlaceSelectionListener mPlaceSelectionListener;
    private Boolean mIsReadOnly;

    public MapWithBottomViewFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_map_with_bottom_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setBottomView();
//        setCenterMarkerEnabled(false);
    }

    @Nullable
    public View onCreateBottomView(LayoutInflater inflater, ViewGroup container)
    {
        return null;
    }

    public void onBottomViewCreated(View view)
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getBaseActivity().setLocationPermissionListener(this);
        getBaseActivity().checkLocationPermission();
        Utility.UI.hideKeyboard(mView);
        if (mMapFragment != null)
            mMapFragment.getMapAsync(mMapFragment);
    }

    @Override
    public void onDestroy()
    {
        getBaseActivity().clearLocationPermissionListener();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && mPlaceSelectionListener != null)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                mPlaceSelectionListener.onPlaceSelected(place);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(mContext, data);
                mPlaceSelectionListener.onError(status);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (getMapFragment().getOriginLatLng() == null)
            fab_my_location.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_my_location_off));
        setReadOnly(false);
    }

    @Override
    public void onLocationPermissionGranted()
    {
        if (isGooglePlayServicesAvailable())
        {
            FragmentManager fm = getChildFragmentManager();
            mMapFragment = (BaseMapFragment) fm.findFragmentByTag(BaseMapFragment.class.getSimpleName());
            if (mMapFragment == null) {
                mMapFragment = new BaseMapFragment();
                mMapFragment.setStartingLatLng(getStartingLatLng());
                mMapFragment.setMapListener(this);
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.fl_map_container, mMapFragment, BaseMapFragment.class.getSimpleName());
                ft.commit();
                fm.executePendingTransactions();
            }
            mMapFragment.checkLocationPermission();
        }
    }

    @Override
    public void onLocationPermissionDenied()
    {
        onBackPressed();
    }

    private void setView()
    {
        fl_map_container = (FrameLayout) findViewById(R.id.fl_map_container);
        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        rl_search_container = (RelativeLayout) findViewById(R.id.rl_search_container);
        img_choose_location_not_found = (ImageView) findViewById(R.id.img_choose_location_not_found);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_area_label = (TextView) findViewById(R.id.tv_area_label);
        rl_choose_pickup_location_marker_container = (RelativeLayout) findViewById(R.id.rl_choose_pickup_location_marker_container);
        rl_choose_pickup_location_background = (RelativeLayout) findViewById(R.id.rl_choose_pickup_location_background);
        ll_choose_pickup_location_marker_container_clickable = (LinearLayout) findViewById(R.id.ll_choose_pickup_location_marker_container_clickable);
        progress_bar_choose_location = (ProgressBar) findViewById(R.id.progress_bar_choose_location);
        progress_bar_area = (ProgressBar) findViewById(R.id.progress_bar_area);
        fab_my_location = (FloatingActionButton) findViewById(R.id.fab_my_location);
    }

    private void setBottomView()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View addedView = onCreateBottomView(layoutInflater, ll_bottom_container);
        if (addedView == null)
            return;

        ll_bottom_container.addView(addedView);
        onBottomViewCreated(addedView);
    }

    public void setBottomView(View view)
    {
        ll_bottom_container.addView(view);
    }

    private void setEvent()
    {
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("ID")
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(autocompleteFilter).build(getActivity());
                    startActivityForResult(intent, 1);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    showToast("Repairable");
                    // TODO: Handle the error.
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    showToast("Service not available");
                    // TODO: Handle the error.
                }
            }
        };

        rl_search_container.setOnClickListener(onClickListener);
        tv_area.setOnClickListener(onClickListener);

        fab_my_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getMapFragment().moveCameraToOriginPosition(15f);
            }
        });
    }

    public ViewGroup getBottomContainer()
    {
        return ll_bottom_container;
    }

    public void setReadOnly(Boolean isReadOnly)
    {
        if (mMapFragment == null || mMapFragment.getMap() == null)
            return;

        mMapFragment.getMap().getUiSettings().setAllGesturesEnabled(!isReadOnly);
        mIsReadOnly = isReadOnly;
        if(isReadOnly)
        {
            fab_my_location.setVisibility(View.GONE);
            rl_choose_pickup_location_background.setVisibility(View.GONE);
            rl_search_container.setOnClickListener(null);
            tv_area.setOnClickListener(null);
            fab_my_location.setOnClickListener(null);
        }
        else
        {
            fab_my_location.setVisibility(View.VISIBLE);
            rl_choose_pickup_location_background.setVisibility(View.VISIBLE);
            setEvent();
        }
    }

    public BaseMapFragment getMapFragment()
    {
        return mMapFragment;
    }

    public void setArea(String area)
    {
        tv_area.setText(area);
    }

    public void setAreaLabel(String areaLabel)
    {
        tv_area_label.setText(areaLabel);
    }

    public void setPlaceSelectionListener(PlaceSelectionListener placeSelectionListener)
    {
        this.mPlaceSelectionListener = placeSelectionListener;
    }

    public void setCenterMarkerEnabled(Boolean isEnabled)
    {
        if (isEnabled)
            rl_choose_pickup_location_marker_container.setVisibility(View.VISIBLE);
        else
            rl_choose_pickup_location_marker_container.setVisibility(View.GONE);
    }

    public void setCenterMarkerBackgroundVisible(Boolean isVisible)
    {
        if (isVisible)
            rl_choose_pickup_location_background.setVisibility(View.VISIBLE);
        else
            rl_choose_pickup_location_background.setVisibility(View.GONE);
    }

    public void setClickableCenterMarkerEnabled(Boolean isEnabled)
    {
        if (isEnabled)
        {
            ll_choose_pickup_location_marker_container_clickable.setVisibility(View.VISIBLE);
            progress_bar_choose_location.setVisibility(View.GONE);
            progress_bar_area.setVisibility(View.GONE);
            tv_area.setVisibility(View.VISIBLE);
            img_choose_location_not_found.setVisibility(View.GONE);
        }
        else
        {
            ll_choose_pickup_location_marker_container_clickable.setVisibility(View.INVISIBLE);
            progress_bar_choose_location.setVisibility(View.VISIBLE);
            progress_bar_area.setVisibility(View.VISIBLE);
            tv_area.setVisibility(View.GONE);
            img_choose_location_not_found.setVisibility(View.GONE);
        }
    }

    public void setCenterMarkerError()
    {
        ll_choose_pickup_location_marker_container_clickable.setVisibility(View.INVISIBLE);
        progress_bar_choose_location.setVisibility(View.GONE);
        progress_bar_area.setVisibility(View.GONE);
        tv_area.setVisibility(View.VISIBLE);
        img_choose_location_not_found.setVisibility(View.VISIBLE);
    }


    public void setCenterMarkerOnClickListener(View.OnClickListener onClickListener)
    {
        ll_choose_pickup_location_marker_container_clickable.setOnClickListener(onClickListener);
    }

    public void setSearchAreaEnabled(Boolean isEnabled)
    {
        if (isEnabled)
            rl_search_container.setVisibility(View.VISIBLE);
        else
            rl_search_container.setVisibility(View.GONE);
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(mContext);
        if(status != ConnectionResult.SUCCESS)
        {
            if(googleApiAvailability.isUserResolvableError(status))
            {
                googleApiAvailability.getErrorDialog(getActivity(), status, 2404).show();
            }
            return false;
        }
        return true;
    }

    public Boolean isReadOnly()
    {
        return mIsReadOnly;
    }

    protected abstract LatLng getStartingLatLng();
}
