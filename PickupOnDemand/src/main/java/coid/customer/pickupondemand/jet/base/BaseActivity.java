package coid.customer.pickupondemand.jet.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Method;

import coid.customer.pickupondemand.jet.Navigator;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.custom.ConfirmationDialog;
import coid.customer.pickupondemand.jet.network.ConnectivityStateChangeReceiver;
import coid.customer.pickupondemand.jet.utility.Reflection;

public abstract class BaseActivity  extends AppCompatActivity
{
    public static final String LOCATION_PERMISSION_PREF_NAME = "locationPermission";

    public static final int REQUEST_PERMISSION_SETTING_REQUEST_CODE = 1270;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1271;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1272;

    public Context mContext;
    private Navigator mNavigator;
    private ProgressDialog mProgressDialog;
    private ConnectivityStateChangeReceiver mConnectivityStateChangeReceiver;
    private ILocationPermissionListener mLocationPermissionListener;
    private ICameraPermissionListener mCameraPermissionListener;
    private SharedPreferences mLocationPermissionStatus;
    private Boolean mIsSentToSettings = false;
    private Boolean mIsLocationPermissionGranted;
    private Boolean mIsCameraPermissionGranted;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mNavigator = new Navigator(mContext, getFragmentContainerId());
        setConnectivityStateChangeReceiver();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        handlePermissionCallback();
    }

    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(getFragmentContainerId());
        if (fragment instanceof BaseFragment)
            ((BaseFragment) fragment).onBackPressed();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        hideLoadingDialog();
        if (mConnectivityStateChangeReceiver != null)
            unregisterReceiver(mConnectivityStateChangeReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERMISSION_SETTING_REQUEST_CODE)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (mLocationPermissionListener != null)
                    mLocationPermissionListener.onLocationPermissionGranted();
                buildGoogleApiClient();
                checkLocationStatus();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            {
                if (mCameraPermissionListener != null)
                    mCameraPermissionListener.onCameraPermissionGranted();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE :
                mIsLocationPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    if (mLocationPermissionListener != null)
//                        mLocationPermissionListener.onLocationPermissionGranted();
//                }
//                else
//                {
//                    if (mLocationPermissionListener != null)
//                        mLocationPermissionListener.onLocationPermissionDenied();
//                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE :
                mIsCameraPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    if (mCameraPermissionListener != null)
//                        mCameraPermissionListener.onCameraPermissionGranted();
//                }
//                else
//                {
//                    if (mCameraPermissionListener != null)
//                        mCameraPermissionListener.onCameraPermissionDenied();
//                }
                break;
            default:
                break;
        }
    }

    private void handlePermissionCallback()
    {
        if (mIsLocationPermissionGranted != null && mLocationPermissionListener != null)
        {
            if (mIsLocationPermissionGranted)
            {
                mLocationPermissionListener.onLocationPermissionGranted();
                buildGoogleApiClient();
                checkLocationStatus();
            }
            else
                mLocationPermissionListener.onLocationPermissionDenied();

            mIsLocationPermissionGranted = null;
            mLocationPermissionListener = null;
        }

        if (mIsCameraPermissionGranted != null && mCameraPermissionListener != null)
        {
            if (mIsCameraPermissionGranted)
                mCameraPermissionListener.onCameraPermissionGranted();
            else
                mCameraPermissionListener.onCameraPermissionDenied();

            mIsCameraPermissionGranted = null;
            mCameraPermissionListener = null;
        }
    }

    public Navigator getNavigator()
    {
        return mNavigator;
    }

    public void showLoadingDialog()
    {
        mProgressDialog = new ProgressDialog(mContext, R.style.ProgressDialog);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void showLoadingDialog(String message)
    {
        if (message == null) message = getString(R.string.pod_loading);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideLoadingDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showToast(String toastMessage)
    {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private void setConnectivityStateChangeReceiver()
    {
        mConnectivityStateChangeReceiver = new ConnectivityStateChangeReceiver();
        IntentFilter networkStateChangeIntentFilter = new IntentFilter();

        networkStateChangeIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectivityStateChangeReceiver, networkStateChangeIntentFilter);
    }

    private synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public GoogleApiClient getGoogleApiClient()
    {
        return mGoogleApiClient;
    }

    private void showNoGPSDialog() {
        String title = getString(R.string.pod_gps_activate_title);
        String message = getString(R.string.pod_gps_activate_confirmation_message);
        final ConfirmationDialog confirmationDialog = new ConfirmationDialog(this, title, message)
        {
            @Override
            public void onOKClicked()
            {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            @Override
            public void onCancelClicked()
            {
                finish();
            }
        };
        confirmationDialog.setCancelable(false);
        confirmationDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                Button buttonPositive = confirmationDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setTextColor(Color.BLACK);
                Button buttonNegative = confirmationDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setTextColor(Color.BLACK);
            }
        });
        confirmationDialog.show();
    }

    private void checkLocationStatus()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showNoGPSDialog();
        }
    }

    public void checkLocationPermission()
    {
        if (isXiaomiStupidPermission())
        {
            if (!checkOp(1)) //AppOpsManager.OP_FINE_LOCATION = 1
            {
                showLocationPermissionDialogToSettings();
                return;
            }
        }

//        if (!hasSelfPermissionForXiaomi(this, Manifest.permission.ACCESS_FINE_LOCATION))
//        {
//            showLocationPermissionDialogToSettings();
//            return;
//        }

        mLocationPermissionStatus = getSharedPreferences(LOCATION_PERMISSION_PREF_NAME, MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                showLocationPermissionDialogToRequest();
            }
            else if (mLocationPermissionStatus.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION, false))
            {
                showLocationPermissionDialogToSettings();
            }
            else
            {
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

            SharedPreferences.Editor editor = mLocationPermissionStatus.edit();
            editor.putBoolean(Manifest.permission.ACCESS_FINE_LOCATION,true);
            editor.apply();
        }
        else
        {
            mIsLocationPermissionGranted = true;
            handlePermissionCallback();
        }
    }

    @SuppressLint("NewApi")
    private boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
        if (permissionToOp == null) {
            // in case of normal permissions(e.g. INTERNET)
            return true;
        }
        int noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp, Process.myUid(), context.getPackageName());
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isXiaomiStupidPermission()
    {
        final int version = Build.VERSION.SDK_INT;
        return version >= 19 && version <= 22 && "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private boolean isXiaomiCameraAllowed(@NonNull Context context)
    {
        final int version = Build.VERSION.SDK_INT;
        return version >= 19 && version <= 22 && "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER) && checkOp(26); //AppOpsManager.OP_CAMERA = 26
    }

    private boolean checkOp(Context context, int op)
    {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try
        {
            return (AppOpsManager.MODE_ALLOWED == (Integer) Reflection.invokeMethod(manager, "checkOp", op, Binder.getCallingUid(), getPackageName()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkOp(int op)
    {
        try
        {
            AppOpsManager manager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            Method dispatchMethod = clazz.getMethod("checkOp", new Class[] { int.class, int.class, String.class });
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            int mode = (Integer) dispatchMethod.invoke(manager, new Object[] { op, Binder.getCallingUid(), getApplicationContext().getPackageName() });

            return AppOpsManager.MODE_ALLOWED == mode || mode < AppOpsManager.MODE_ALLOWED || mode > AppOpsManager.MODE_DEFAULT;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    public void checkCameraPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            {
                showCameraPermissionDialogToRequest();
            }
            else
            {
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            mIsCameraPermissionGranted = true;
            handlePermissionCallback();
        }
    }

    private void showLocationPermissionDialogToRequest()
    {
        String title = Utility.Message.get(R.string.pod_permission_location_title);
        String message = Utility.Message.get(R.string.pod_permission_location_message_info);

        final ConfirmationDialog confirmationDialog = new ConfirmationDialog(BaseActivity.this, title, message)
        {
            @Override
            public void onOKClicked()
            {
                dismiss();
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

            @Override
            public void onCancelClicked()
            {
                super.onCancelClicked();
                mIsLocationPermissionGranted = false;
                handlePermissionCallback();
            }
        };
        confirmationDialog.setCancelable(false);
        confirmationDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                Button buttonPositive = confirmationDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setTextColor(Color.BLACK);
                Button buttonNegative = confirmationDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setTextColor(Color.BLACK);
            }
        });
        confirmationDialog.show();
    }

    private void showLocationPermissionDialogToSettings()
    {
        String title = Utility.Message.get(R.string.pod_permission_location_title);
        String message = Utility.Message.get(R.string.pod_permission_location_message_info);

        final ConfirmationDialog confirmationDialog = new ConfirmationDialog(BaseActivity.this, title, message)
        {
            @Override
            public void onOKClicked()
            {
                mIsSentToSettings = true;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING_REQUEST_CODE);
            }

            @Override
            public void onCancelClicked()
            {
                super.onCancelClicked();
                mIsLocationPermissionGranted = false;
                handlePermissionCallback();
            }
        };
        confirmationDialog.setCancelable(false);
        confirmationDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                Button buttonPositive = confirmationDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setTextColor(Color.BLACK);
                Button buttonNegative = confirmationDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setTextColor(Color.BLACK);
            }
        });
        confirmationDialog.show();
    }

    private void showCameraPermissionDialogToRequest()
    {
        String title = Utility.Message.get(R.string.pod_permission_camera_title);
        String message = Utility.Message.get(R.string.pod_permission_camera_message_info);
        final ConfirmationDialog confirmationDialog = new ConfirmationDialog(BaseActivity.this, title, message)
        {
            @Override
            public void onOKClicked()
            {
                dismiss();
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }

            @Override
            public void onCancelClicked()
            {
                super.onCancelClicked();
                mIsCameraPermissionGranted = false;
                handlePermissionCallback();
            }
        };
        confirmationDialog.setCancelable(false);
        confirmationDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                Button buttonPositive = confirmationDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setTextColor(Color.BLACK);
                Button buttonNegative = confirmationDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setTextColor(Color.BLACK);
            }
        });
        confirmationDialog.show();
    }

    public ILocationPermissionListener getLocationPermissionListener()
    {
        return mLocationPermissionListener;
    }

    public void setLocationPermissionListener(ILocationPermissionListener locationPermissionListener)
    {
        mLocationPermissionListener = locationPermissionListener;
    }

    public void clearLocationPermissionListener()
    {
        mLocationPermissionListener = null;
    }

    public ICameraPermissionListener getCameraPermissionListener()
    {
        return mCameraPermissionListener;
    }

    public void setCameraPermissionListener(ICameraPermissionListener cameraPermissionListener)
    {
        mCameraPermissionListener = cameraPermissionListener;
    }

    public void clearCameraPermissionListener()
    {
        mCameraPermissionListener = null;
    }

    public abstract ActionBarDrawerToggle getDrawerToggle();
    public abstract Integer getFragmentContainerId();
    public abstract Fragment getDefaultFragment();

    public interface ILocationPermissionListener
    {
        void onLocationPermissionGranted();
        void onLocationPermissionDenied();
    }

    public interface ICameraPermissionListener
    {
        void onCameraPermissionGranted();
        void onCameraPermissionDenied();
    }
}