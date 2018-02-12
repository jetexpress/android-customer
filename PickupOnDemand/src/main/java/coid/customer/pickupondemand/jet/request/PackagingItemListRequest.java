package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.PackagingItem;
import coid.customer.pickupondemand.jet.model.QueryData;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public class PackagingItemListRequest extends BaseNetworkRequest<QueryData<PackagingItem>>
{
    public PackagingItemListRequest(Context context)
    {
        super(context);
    }

    @Override
    public Call<QueryData<PackagingItem>> getCall()
    {
        return RetrofitProvider.getResourcesService().getPackagingItemList();
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        executeAsync();
    }
}

