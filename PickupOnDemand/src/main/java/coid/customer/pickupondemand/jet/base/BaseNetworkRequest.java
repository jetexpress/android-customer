package coid.customer.pickupondemand.jet.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;

import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.request.RefreshTokenRequest;
import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseNetworkRequest<T>
{
    private Context mContext;
    private AppCompatActivity mActivity;
    private BaseActivity mBaseActivity;

    private Boolean mIsExecuting;
    private Boolean mIsSuccess;

    private ProgressDialog mProgressDialog;

    public BaseNetworkRequest(@Nullable Context context)
    {
        mContext = context;
        if (mContext != null && mContext instanceof AppCompatActivity)
        {
            mActivity = (AppCompatActivity) mContext;
            if (mActivity instanceof BaseActivity)
                mBaseActivity = (BaseActivity) mActivity;
        }

        mIsExecuting = false;
        mIsSuccess = false;
    }

    public void execute()
    {
        mIsExecuting = true;

        if (getCall() == null)
        {
            if (isBaseUIValid())
                showToast("Empty Call");
            return;
        }

//            if (isBaseUIValid() && !Utility.NetworkConnectivity.isNetworkAvailable())
//            {
//                onTimeOutOnUIThread();
//                return;
//            }

        onStart();
        executeCall();
    }

    public void executeAsync()
    {
        Utility.Executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                execute();
            }
        });
    }

    private void executeCall()
    {
        try
        {
            final Response<T> response = getCall().execute();

            if (response.body() instanceof Void)
            {
                if (response.errorBody() == null && response.code() == HttpURLConnection.HTTP_OK)
                {
                    Log.d("NETWORK_127", this.getCall().request().url().toString() + ", SUCCESS");
                    mIsSuccess = true;
                    onSuccess(response);
                }
                else
                {
                    Utility.Message.setResponseFailedMessage(response); /** HAX00R */
                    onResponseFailed(response);
                    Log.d("NETWORK_127", response.raw().request().url().toString() + ", RESPONSE FAILED : " + Utility.Message.responseFailedMessage);
                }
            }
            else
            {
                if (response.body() != null)
                {
                    Log.d("NETWORK_127", this.getCall().request().url().toString() + ", SUCCESS");
                    mIsSuccess = true;
                    onSuccess(response);
                }
                else
                {
                    if (response.errorBody() == null && response.code() == HttpURLConnection.HTTP_OK)
                    {
                        Log.d("NETWORK_127", this.getCall().request().url().toString() + ", SUCCESS");
                        mIsSuccess = true;
                        onSuccess(response);
                    }
                    else
                    {
                        Utility.Message.setResponseFailedMessage(response); /** HAX00R */
                        onResponseFailed(response);
                        Log.d("NETWORK_127", response.raw().request().url().toString() + ", RESPONSE FAILED : " + Utility.Message.responseFailedMessage);
                    }
                }
            }
        }
        catch (final Exception ex)
        {
            if (isTimeout(ex))
            {
                Log.d("NETWORK_127", this.getCall().request().url().toString() + ", TIMEOUT");
                onTimeOut();
            }
            else
            {
                Log.d("NETWORK_127", this.getCall().request().url().toString() + ", FAILED");
                onFailed(ex);
            }
        }
        finally
        {
            mIsExecuting = false;
        }
    }

    private Boolean isTimeout(Throwable t)
    {
        return (t instanceof UnknownHostException ||
                t instanceof TimeoutException ||
                t instanceof SocketException ||
                t instanceof SocketTimeoutException);
    }

    public Boolean isSuccess()
    {
        return mIsSuccess;
    }

    public Boolean isExecuting()
    {
        return mIsExecuting;
    }

    public Boolean isIdle()
    {
        return !mIsExecuting;
    }

    private Boolean isUIValid()
    {
        return mActivity != null;
    }
    private Boolean isBaseUIValid()
    {
        return mBaseActivity != null && getBaseFragment() != null && getBaseFragment().isAdded();
    }
    protected Context getContext()
    {
        return mContext;
    }

    protected String getString(Integer resId)
    {
        return mContext.getString(resId);
    }

    protected BaseActivity getBaseActivity()
    {
        return mBaseActivity;
    }

    protected BaseFragment getBaseFragment()
    {
        if (mBaseActivity != null)
        {
            FragmentManager fm = mBaseActivity.getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(mBaseActivity.getFragmentContainerId());
            if (fragment != null && fragment instanceof BaseFragment)
                return (BaseFragment) fragment;
        }
        return null;
    }

    protected void showLoadingDialog()
    {
        if (isBaseUIValid())
            mBaseActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mBaseActivity.showLoadingDialog();
                }
            });
        else if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mProgressDialog = new ProgressDialog(mContext, R.style.ProgressDialog);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                }
            });
    }

    protected void hideLoadingDialog()
    {
        clearLoadingDialog();
    }

    private void clearLoadingDialog()
    {
        if (isBaseUIValid())
            mBaseActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mBaseActivity.hideLoadingDialog();
                }
            });
        else if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                    {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            });
    }

    protected void showToast(final String message)
    {
        if (isBaseUIValid())
            mBaseActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mBaseActivity.showToast(message);
                }
            });
        else if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            });
    }

    private void onStart()
    {
        onStartOnNetworkThread();
        if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (isUIValid())
                        onStartOnUIThread();
                }
            });
    }
    private void onSuccess(final Response<T> response)
    {
        onSuccessOnNetworkThread(response);
        if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (isUIValid())
                        onSuccessOnUIThread(response);
                }
            });
    }
    private void onResponseFailed(final Response<T> response)
    {
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
        {
            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(mContext, this);
            refreshTokenRequest.executeAsync();
        }
        else
        {
            onResponseFailedOnNetworkThread(response);
            if (isUIValid())
                mActivity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (isUIValid())
                            onResponseFailedOnUIThread(response);
                    }
                });
        }
    }

    private void onFailed(final Exception ex)
    {
        onFailedOnNetworkThread(ex);
        if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (isUIValid())
                        onFailedOnUIThread(ex);
                }
            });
    }
    private void onTimeOut()
    {
        onTimeOutOnNetworkThread();
        if (isUIValid())
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (isUIValid())
                        onTimeOutOnUIThread();
                }
            });
    }

    public void cancel()
    {
        clearLoadingDialog();
        if (getCall() != null && !getCall().isCanceled())
            getCall().cancel();
    }

    public void clear()
    {
        cancel();
        mContext = null;
        mActivity = null;
        mBaseActivity = null;
        mProgressDialog = null;
    }

    protected void onStartOnNetworkThread(){}
    protected void onSuccessOnNetworkThread(Response<T> response){}
    protected void onResponseFailedOnNetworkThread(Response<T> response){}
    protected void onFailedOnNetworkThread(Exception ex){}
    protected void onTimeOutOnNetworkThread(){}

    protected void onStartOnUIThread()
    {
        showLoadingDialog();
    }
    protected void onSuccessOnUIThread(Response<T> response)
    {
        hideLoadingDialog();
    }
    protected void onResponseFailedOnUIThread(Response<T> response)
    {
        hideLoadingDialog();
    }
    protected void onFailedOnUIThread(Exception ex)
    {
        hideLoadingDialog();
    }
    protected void onTimeOutOnUIThread()
    {
        hideLoadingDialog();
    }

    public abstract Call<T> getCall();
}