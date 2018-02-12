package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.CourierLocation;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class CourierLastLocationRequest extends BaseNetworkRequest<CourierLocation>
{
    private String mCourierUserId;
    private Handler mHandler;
    private Runnable mIntervalRequestRunnable;
    private Long mIntervalMillis;
    private Boolean mIsHalted;

    public CourierLastLocationRequest(Context context, String courierUserId)
    {
        super(context);
        mCourierUserId = courierUserId;
        mIsHalted = false;
    }

    @Override
    public Call<CourierLocation> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().getCourierLastLocation(mCourierUserId);
    }

    @Override
    protected void onResponseFailedOnUIThread(Response<CourierLocation> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseFailedMessage(R.string.pod_get_courier_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_get_courier_failed, ex));
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        super.onTimeOutOnUIThread();
        showToast(getString(R.string.pod_request_timed_out));
    }

    @Override
    public void clear()
    {
        stopInterval();
        super.clear();
    }

    public void executeInterval(Long intervalMillis)
    {
        Log.d("JET_127", "LOCATION LOG, START");

        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());

        if (mIntervalRequestRunnable == null)
            mIntervalRequestRunnable = getIntervalRequestRunnable();

        mIntervalMillis = intervalMillis;
        mHandler.post(mIntervalRequestRunnable);
    }

    public void stopInterval()
    {
        Log.d("JET_127", "LOCATION LOG, STOP");
        cancel();
        if (mHandler != null)
        {
            if (mIntervalRequestRunnable != null)
                mHandler.removeCallbacks(mIntervalRequestRunnable);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mIntervalRequestRunnable = null;
    }

    private Runnable getIntervalRequestRunnable()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                if (!mIsHalted)
                {
                    Log.d("JET_127", "LOCATION LOG, GET COURIER LOCATION");
                    executeAsync();
                }
                else
                    Log.d("JET_127", "LOCATION LOG, GET LOCATION HALTED, MARKER IS STILL MOVING");

                Log.d("JET_127", "LOCATION LOG, GET NEXT COURIER LOCATION IN " + String.valueOf(mIntervalMillis) + " MILLISECONDS");
                mHandler.postDelayed(this, mIntervalMillis);
            }
        };
    }

    public void setHalt(Boolean isHalted)
    {
        mIsHalted = isHalted;
    }
}