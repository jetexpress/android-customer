package coid.customer.pickupondemand.jet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.net.ConnectivityManagerCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.network.ConnectivityStateChangeReceiver;
import coid.customer.pickupondemand.jet.utility.NumberFormatter;
import retrofit2.Response;

public class Utility
{
    private static Context getContext()
    {
        return JETApplication.getContext();
    }

    public static class Executor
    {
        private static ThreadPoolExecutor mPool = JETApplication.getThreadPoolExecutor();

        public static void execute(Runnable task){
            if(!mPool.isShutdown() && mPool.getActiveCount() != mPool.getMaximumPoolSize()){
                mPool.execute(task);
            } else {
                new Thread(task).start();
            }
        }

        public static void submit(Runnable task){
            if(!mPool.isShutdown() && mPool.getActiveCount() != mPool.getMaximumPoolSize()){
                mPool.submit(task);
            } else {
                new Thread(task).start();
            }
        }
    }

    public static class Message
    {
        public static String responseFailedMessage = "";
        public static String get(Integer stringResourceId)
        {
            return getContext().getString(stringResourceId);
        }

        public static String getResponseMessage(Integer stringResourceId, Response response)
        {
            return get(stringResourceId) + ", " + response.message();
        }

        public static String setResponseFailedMessage(Response response)
        {
            try
            {
                responseFailedMessage = response.errorBody().string();
            }
            catch (Exception ex)
            {
                responseFailedMessage = "";
            }

            return responseFailedMessage;
        }

        public static String getResponseFailedMessage(Integer stringResourceId, Response response)
        {
            try
            {
                JSONObject obj = new JSONObject(responseFailedMessage);
                String errorMessage = obj.getString(ApiConfig.RESPONSE_MESSAGE_KEY_NAME);
                return errorMessage != null && !errorMessage.isEmpty() ? errorMessage : getResponseMessage(stringResourceId, response);
            }
            catch (Exception ex)
            {
                return getResponseMessage(stringResourceId, response);
            }
        }

        public static String getNetworkFailureMessage(Integer stringResourceId, Throwable t)
        {
            String message = getContext().getString(stringResourceId);
            return message + ", " + t.getMessage();
        }

        public static void printStackTrace(Throwable t)
        {
            if (t.getMessage() != null)
                t.printStackTrace();
        }
    }

    public static class DateFormat
    {
        public static String getDateStringFromMillis(long milliSeconds, String dateFormat, boolean isUTC)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            if (isUTC) sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return sdf.format(calendar.getTime());
        }

        public static Long getMillisFromDateString(String dateString, String dateFormat, boolean isUTC)
        {
            /*yyyy-MM-dd'T'HH:mm:ss'Z'*/
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            if (isUTC) sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            try
            {
                calendar.setTime(sdf.parse(dateString));
            }
            catch (ParseException ex)
            {
                return null;
            }
            return calendar.getTimeInMillis();
        }
    }

    public static class NumberFormat
    {
        public static String doubleToString(Double value, Integer decimalPlace)
        {
            String decimalPlaceStringFormat = String.valueOf(decimalPlace);
            return String.format(Locale.getDefault(), "%,." + decimalPlaceStringFormat + "f", value);
        }

        public static Double stringToDouble(String valueString)
        {
            return valueString.isEmpty() ? 0 : Double.valueOf(valueString.replaceAll(",", "").replaceAll("\\.",""));
        }
    }

    public static class NetworkConnectivity
    {
        public static Integer TYPE_NETWORK_DISCONNECTED = -1;
        public static Integer TYPE_MOBILE_CONNECTED = 0;
        public static Integer TYPE_WIFI_CONNECTED = 1;

        public static Integer getConnectivityStatus(Intent intent)
        {
            ConnectivityManager cm = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(cm, intent);
            if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected())
                    return TYPE_WIFI_CONNECTED;
                else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnected())
                    return TYPE_MOBILE_CONNECTED;
            }
            return TYPE_NETWORK_DISCONNECTED;
        }

        public static Integer getConnectivityStatus()
        {
            ConnectivityManager cm = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected())
                    return TYPE_WIFI_CONNECTED;
                else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnected())
                    return TYPE_MOBILE_CONNECTED;
            }
            return TYPE_NETWORK_DISCONNECTED;
        }

        public static Boolean isNetworkAvailable()
        {
            if (ConnectivityStateChangeReceiver.getConnectivityState() != null)
                return !ConnectivityStateChangeReceiver.getConnectivityState().equals(TYPE_NETWORK_DISCONNECTED);
            else
                return getConnectivityStatus().equals(TYPE_NETWORK_DISCONNECTED);
        }

        public static Boolean isWifiConnected()
        {
            if (ConnectivityStateChangeReceiver.getConnectivityState() != null)
                return ConnectivityStateChangeReceiver.getConnectivityState().equals(TYPE_WIFI_CONNECTED);
            else
                return getConnectivityStatus().equals(TYPE_WIFI_CONNECTED);
        }

        public static Boolean isMobileConnected()
        {
            if (ConnectivityStateChangeReceiver.getConnectivityState() != null)
                return ConnectivityStateChangeReceiver.getConnectivityState().equals(TYPE_MOBILE_CONNECTED);
            else
                return getConnectivityStatus().equals(TYPE_MOBILE_CONNECTED);
        }
    }

    public static class Location
    {
        public static android.location.Location getCurrentLocationByFused(GoogleApiClient googleApiClient)
        {
            if (googleApiClient == null)
                return null;

            try
            {
                return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }
            catch (SecurityException ex)
            {
                return null;
            }
        }
    }

    public static class UI
    {
        public static void hideKeyboard(View view)
        {
            if (view == null || view.getWindowToken() == null)
                return;

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class Validation
    {
        public static Boolean isEmailFormatValid(String email)
        {
            final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            return Pattern.matches(EMAIL_REGEX, email);
        }

        public static ValidateEmptyTextWatcher getValidateEmptyTextWatcher(TextInputLayout inputLayout, TextInputEditText editText)
        {
            return new ValidateEmptyTextWatcher(inputLayout, editText);
        }

        public static ValidateNominalTextWatcher getValidateNominalTextWatcher(TextInputLayout inputLayout, TextInputEditText editText, Double minValue)
        {
            return new ValidateNominalTextWatcher(inputLayout, editText, minValue);
        }

        public static DecimalTextWatcher getDecimalTextWatcher(EditText editText)
        {
            return new DecimalTextWatcher(editText);
        }

        public static boolean isEditTextEmpty(TextInputLayout inputLayout, TextInputEditText editText)
        {
            if (editText.getText().toString().trim().isEmpty())
            {
                String requiredMessage = Message.get(R.string.pod_required);
                inputLayout.setError(requiredMessage);
                editText.requestFocus();
                return true;
            }
            else
            {
                inputLayout.setErrorEnabled(false);
            }

            return false;
        }

        public static boolean isNominalInvalid(TextInputLayout inputLayout, TextInputEditText editText, Double minValue)
        {
            if (minValue == null)
                minValue = 0D;

            Double value = NumberFormatter.stringToDouble(editText.getText().toString().trim());
            if (value == null || value <= minValue)
            {
                String requiredMessage = Message.get(R.string.pod_invalid_nominal);
                inputLayout.setError(requiredMessage);
                editText.requestFocus();
                return true;
            }
            else
            {
                inputLayout.setErrorEnabled(false);
            }

            return false;
        }
    }

    public static class Animation
    {
        private static final int animDuration = 200;

        // To animate view slide out from right to left
        public static void slideOutToLeft(View view, Integer toXDelta){
            TranslateAnimation animate = new TranslateAnimation(0,-toXDelta,0,0);
            animate.setDuration(animDuration);
            view.startAnimation(animate);
            view.setVisibility(View.GONE);
        }

        // To animate view slide out from left to right
        public static void slideOutToRight(View view, Integer toXDelta){
            TranslateAnimation animate = new TranslateAnimation(0,toXDelta,0,0);
            animate.setDuration(animDuration);
            view.startAnimation(animate);
            view.setVisibility(View.GONE);
        }

        // To animate view slide in from left to right
        public static void slideInFromLeft(View view, Integer fromXDelta){
            TranslateAnimation animate = new TranslateAnimation(-fromXDelta,0,0,0);
            animate.setDuration(animDuration);
            view.startAnimation(animate);
            view.setVisibility(View.VISIBLE);
        }

        // To animate view slide in from right to left
        public static void slideInFromRight(View view, Integer fromXDelta){
            TranslateAnimation animate = new TranslateAnimation(fromXDelta,0,0,0);
            animate.setDuration(animDuration);
            view.startAnimation(animate);
            view.setVisibility(View.VISIBLE);
        }

        public static void slideInFromBottom(View view){
            TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),0);
            animate.setDuration(animDuration);
            view.startAnimation(animate);
            view.setVisibility(View.VISIBLE);
        }

        public static void slideOutToBottom(View view){
            TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
            animate.setDuration(animDuration);
            view.startAnimation(animate);
            view.setVisibility(View.GONE);
        }
    }

    public static class Image
    {
        public static byte[] bitmapToByteArray(Bitmap bitmap)
        {
            try
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                return outputStream.toByteArray();
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public static String byteArrayToBase64String(byte[] byteArrayImage)
        {
            try
            {
                return Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public static String bitmapToBase64String(Bitmap bitmap)
        {
            try
            {
                return byteArrayToBase64String(bitmapToByteArray(bitmap));
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public static byte[] base64StringToByteArray(String base64String)
        {
            try
            {
                return Base64.decode(base64String, Base64.DEFAULT);
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public static Bitmap byteArrayToBitmap(byte[] byteArrayImage)
        {
            try
            {
                return BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length);
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public static Bitmap base64StringToBitmap(String base64String)
        {
            try
            {
                return byteArrayToBitmap(base64StringToByteArray(base64String));
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }

    public static class DecimalTextWatcher implements TextWatcher
    {

        private EditText mEditText;

        public DecimalTextWatcher(EditText editText){
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mEditText.removeTextChangedListener(this);
            String value = mEditText.getText().toString();
            if (!value.isEmpty())
            {
                Double beforeText = Double.valueOf(value.replaceAll(",", "").replaceAll("\\.", ""));
                mEditText.setText(String.format("%,.0f", beforeText));
                mEditText.setSelection(mEditText.getText().toString().length());
            }
            mEditText.addTextChangedListener(this);
        }
    }

    private static class ValidateEmptyTextWatcher implements TextWatcher
    {
        private TextInputLayout mInputLayout;
        private TextInputEditText mEditText;

        private ValidateEmptyTextWatcher(TextInputLayout inputLayout, TextInputEditText editText)
        {
            this.mInputLayout = inputLayout;
            this.mEditText = editText;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable)
        {
            Validation.isEditTextEmpty(mInputLayout, mEditText);
        }
    }

    private static class ValidateNominalTextWatcher implements TextWatcher
    {
        private TextInputLayout mInputLayout;
        private TextInputEditText mEditText;
        private Double mMinValue;

        private ValidateNominalTextWatcher(TextInputLayout inputLayout, TextInputEditText editText, Double minValue)
        {
            this.mInputLayout = inputLayout;
            this.mEditText = editText;
            this.mMinValue = minValue != null ? minValue : 0D;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable)
        {
            Validation.isNominalInvalid(mInputLayout, mEditText, mMinValue);
        }
    }

    public static class OneSignal
    {
        private static final String TAG_EMAIL = "email";
        private static final String TAG_ROLE = "role";
        private static final String TAG_AVAILABLE = "available";
        private static final String TAG_BRANCH_CODE = "branchCode";
        private static final String TAG_KIOSK_CODE = "kioskCode";
        private static final String TAG_KIOSK_TYPE = "kiosk_type";

        public static JSONObject getTag()
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                UserProfile userProfile = DBQuery.getSingle(UserProfile.class);
                if (userProfile == null)
                    return null;

                jsonObject.put(TAG_EMAIL, userProfile.getEmail());
                jsonObject.put(TAG_ROLE, AppConfig.ONE_SIGNAL_ROLE_TAG);
            }
            catch (Exception ex)
            {
                return null;
            }

            return jsonObject;
        }

        public static void sendTags()
        {
            if (getTag() == null)
            {
                Log.d("JET_127", "TAG NULL");
                return;
            }

            com.onesignal.OneSignal.getTags(new com.onesignal.OneSignal.GetTagsHandler()
            {
                @Override
                public void tagsAvailable(JSONObject tags)
                {
                    if (tags != null)
                    {
                        com.onesignal.OneSignal.deleteTag(TAG_EMAIL);
                        com.onesignal.OneSignal.deleteTag(TAG_ROLE);
                        com.onesignal.OneSignal.deleteTag(TAG_AVAILABLE);
                        com.onesignal.OneSignal.deleteTag(TAG_BRANCH_CODE);
                        com.onesignal.OneSignal.deleteTag(TAG_KIOSK_CODE);
                        com.onesignal.OneSignal.deleteTag(TAG_KIOSK_TYPE);
                        Log.d("JET_127", "DELETE TAG");
                    }
                    else
                        Log.d("JET_127", "TAG NOT AVAILABLE");

                    JSONObject tag = getTag();
                    com.onesignal.OneSignal.sendTags(tag);
                    Log.d("JET_127", "SEND TAG : " + getTag());
                }
            });
        }

        public static void deleteTags()
        {
            com.onesignal.OneSignal.getTags(new com.onesignal.OneSignal.GetTagsHandler()
            {
                @Override
                public void tagsAvailable(JSONObject tags)
                {
                    if (tags != null)
                    {
                        com.onesignal.OneSignal.deleteTag(TAG_EMAIL);
                        com.onesignal.OneSignal.deleteTag(TAG_ROLE);
                        com.onesignal.OneSignal.deleteTag(TAG_AVAILABLE);
                        com.onesignal.OneSignal.deleteTag(TAG_BRANCH_CODE);
                        com.onesignal.OneSignal.deleteTag(TAG_KIOSK_CODE);
                        com.onesignal.OneSignal.deleteTag(TAG_KIOSK_TYPE);
                        Log.d("JET_127", "DELETE TAG");
                    }
                    else
                        Log.d("JET_127", "TAG NOT AVAILABLE");
                }
            });
        }
    }
}