package coid.customer.pickupondemand.jet.request;

import android.content.Context;
import android.support.annotation.Nullable;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.QueryResult;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class UnratedPickupListRequest extends BaseNetworkRequest<QueryResult<Pickup>>
{
    public UnratedPickupListRequest(@Nullable Context context)
    {
        super(context);
    }

    @Override
    public Call<QueryResult<Pickup>> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().getUnratedPickup();
    }
}
