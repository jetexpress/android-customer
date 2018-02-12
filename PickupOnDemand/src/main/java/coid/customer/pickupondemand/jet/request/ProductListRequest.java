package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Product;
import coid.customer.pickupondemand.jet.model.QueryData;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class ProductListRequest extends BaseNetworkRequest<QueryData<Product>>
{
    public ProductListRequest(Context context)
    {
        super(context);
    }

    @Override
    public Call<QueryData<Product>> getCall()
    {
        return RetrofitProvider.getResourcesService().getProductList();
    }

    @Override
    protected void onTimeOutOnUIThread()
    {
        executeAsync();
    }
}
