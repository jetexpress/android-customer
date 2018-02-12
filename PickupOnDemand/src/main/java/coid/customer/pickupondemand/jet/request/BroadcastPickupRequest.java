package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.custom.ConfirmationDialog;
import coid.customer.pickupondemand.jet.model.BroadcastRecipient;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class BroadcastPickupRequest extends BaseNetworkRequest<BroadcastRecipient>
{
    private Pickup mPickup;
    private int mAttempt;
    private Handler mHandler;
    private long mAttemptDelay;

    public BroadcastPickupRequest(Context context, Pickup pickup, long attemptDelay)
    {
        super(context);
        mPickup = pickup;
        mAttemptDelay = attemptDelay;
        if (mPickup.isQuickPickup())
            mAttempt = 0;
        else
            mAttempt = 1;

        mHandler = new Handler(Looper.getMainLooper());
        Log.d("JET_127", "BROADCAST ATTEMPT : " + String.valueOf(mAttempt));
    }

    @Override
    public Call<BroadcastRecipient> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().broadcastPickup(mPickup.getCode(), mAttempt);
    }

    @Override
    protected void showLoadingDialog()
    {

    }

    @Override
    protected void onStartOnUIThread()
    {

    }

    @Override
    protected void onResponseFailedOnUIThread(Response<BroadcastRecipient> response)
    {
        super.onResponseFailedOnUIThread(response);
        showToast(Utility.Message.getResponseMessage(R.string.pod_broadcast_pickup_failed, response));
    }

    @Override
    protected void onFailedOnUIThread(Exception ex)
    {
        super.onFailedOnUIThread(ex);
        showToast(Utility.Message.getNetworkFailureMessage(R.string.pod_broadcast_pickup_failed, ex));
    }

    @Override
    protected void onSuccessOnUIThread(Response<BroadcastRecipient> response)
    {
        clearDelayedRequest();

        if (isLastAttempt())
        {
            Log.d("JET_127", "BROADCAST DONE, NTC");
            hideLoadingDialog();
            getBaseActivity().getNavigator().popResumeToDefaultFragment();
            String title = getString(R.string.pod_pickup_status_waiting_for_courier);
            String message = getString(R.string.pod_pickup_last_attempt_message);
            final ConfirmationDialog confirmationDialog = new ConfirmationDialog(getContext(), title, message)
            {
                @Override
                public void onOKClicked()
                {
                    dismiss();
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
                    buttonNegative.setEnabled(false);
                }
            });
            confirmationDialog.show();

            clearDelayedRequest();
        }
        else
        {
            mAttempt += 1;

            if (response.body().getRecipients() <= 0)
            {
                Log.d("JET_127", "BROADCAST ATTEMPT : " + String.valueOf(mAttempt));
                Log.d("JET_127", "BROADCAST IMMEDIATE");
                executeAsync();
            }
            else
            {
                Log.d("JET_127", "BROADCAST DELAYED WILL BE STARTED IN " + String.valueOf(mAttemptDelay) + " ms, RECIPIENTS : " + String.valueOf(response.body().getRecipients()));
                executeDelayed();
            }
        }
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        executeAsync();
    }

    public int getAttempt()
    {
        return mAttempt;
    }

    public void setAttempt(int attempt)
    {
        mAttempt = attempt;
    }

    public Boolean isFirstAttempt()
    {
        return mAttempt > 1;
    }

    public Boolean isLastAttempt()
    {
        return mAttempt == AppConfig.COURIER_ORDER_LAST_ATTEMPT;
    }

    public void clearDelayedRequest()
    {
        Log.d("JET_127", "CLEAR BROADCAST PICKUP");
        mHandler.removeCallbacksAndMessages(null);
    }

    public void executeDelayed()
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("JET_127", "BROADCAST ATTEMPT : " + String.valueOf(mAttempt));
                Log.d("JET_127", "BROADCAST DELAYED EXECUTED");
                executeAsync();
            }
        }, mAttemptDelay);
    }
}