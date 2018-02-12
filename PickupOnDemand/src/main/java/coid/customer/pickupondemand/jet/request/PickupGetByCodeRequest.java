package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class PickupGetByCodeRequest extends BaseNetworkRequest<Pickup>
{
    private String mPickupCode;

    public PickupGetByCodeRequest(Context context, String pickupCode)
    {
        super(context);
        mPickupCode = pickupCode;
    }

    @Override
    public Call<Pickup> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().getPickupByCode(mPickupCode);
    }
}
