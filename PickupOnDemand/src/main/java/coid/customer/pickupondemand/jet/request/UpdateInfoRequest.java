package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.UpdateInfo;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class UpdateInfoRequest extends BaseNetworkRequest<UpdateInfo>
{
    private int mVersion;

    public UpdateInfoRequest(Context context, int version)
    {
        super(context);
        mVersion = version;
    }

    @Override
    public Call<UpdateInfo> getCall()
    {
        return RetrofitProvider.getResourcesService().getUpdateInfo(mVersion);
    }

    @Override
    protected void showLoadingDialog()
    {

    }

    @Override
    protected void hideLoadingDialog()
    {

    }

    @Override
    protected void onTimeOutOnNetworkThread()
    {
        executeAsync();
    }
}
