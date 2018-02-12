package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Config;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class ConfigRequest extends BaseNetworkRequest<Config>
{
    public ConfigRequest(Context context)
    {
        super(context);
    }

    @Override
    public Call<Config> getCall()
    {
        return RetrofitProvider.getResourcesService().getConfig();
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        executeAsync();
    }
}