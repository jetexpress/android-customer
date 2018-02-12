package coid.customer.pickupondemand.jet.request;

import android.content.Context;

import coid.customer.pickupondemand.jet.base.BaseNetworkRequest;
import coid.customer.pickupondemand.jet.model.PaymentMethod;
import coid.customer.pickupondemand.jet.model.QueryData;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;

public class PaymentMethodListRequest extends BaseNetworkRequest<QueryData<PaymentMethod>>
{
    public PaymentMethodListRequest(Context context)
    {
        super(context);
    }

    @Override
    public Call<QueryData<PaymentMethod>> getCall()
    {
        return RetrofitProvider.getAuthorizedResourcesService().getPaymentMethodList();
    }
}
