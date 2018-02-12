package coid.customer.pickupondemand.jet.base;

import android.content.Context;

import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.Query;
import coid.customer.pickupondemand.jet.model.QueryResult;
import coid.customer.pickupondemand.jet.network.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Response;

public abstract class BasePagingRequest<T> extends BaseNetworkRequest<QueryResult<T>>
{
    private Query mQuery;
    private Long mPage = 1L;

    public BasePagingRequest(Context context)
    {
        super(context);
    }

    @Override
    protected void onSuccessOnUIThread(Response<QueryResult<T>> response)
    {
        mQuery = response.body().getQuery();
    }

    public Query getQuery()
    {
        return mQuery;
    }

    public void setQuery(Query query)
    {
        mQuery = query;
    }

    public Long getPage()
    {
        return mPage;
    }

    public void setPage(Long page)
    {
        mPage = page;
    }

    public Boolean hasExecuted()
    {
        return mQuery != null;
    }

    public Boolean isLastPage()
    {
        return mQuery != null && mQuery.getPage() >= mQuery.getTotalPage();
    }
}