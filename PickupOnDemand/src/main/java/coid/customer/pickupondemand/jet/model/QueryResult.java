package coid.customer.pickupondemand.jet.model;

import java.util.List;

public class QueryResult<T>
{
    private Query query;
    private List<T> result;

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public List<T> getResult()
    {
        return result;
    }

    public void setResult(List<T> result)
    {
        this.result = result;
    }
}
