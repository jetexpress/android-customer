package coid.customer.pickupondemand.jet.model;

import java.util.List;

public class QueryData<T>
{
    private Query query;
    private List<T> data;

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }
}
