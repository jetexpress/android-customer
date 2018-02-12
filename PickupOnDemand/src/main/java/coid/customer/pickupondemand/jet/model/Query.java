package coid.customer.pickupondemand.jet.model;

public class Query
{
    private boolean asc;
    private long count;
    private String keyword;
    private String orderBy;
    private String sort;
    private long page;
    private long size;
    private long totalPage;
    private String date;

    public boolean isAsc()
    {
        return asc;
    }

    public void setAsc(boolean asc)
    {
        this.asc = asc;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public long getPage()
    {
        return page;
    }

    public void setPage(long page)
    {
        this.page = page;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public long getTotalPage()
    {
        return totalPage;
    }

    public void setTotalPage(long totalPage)
    {
        this.totalPage = totalPage;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}
