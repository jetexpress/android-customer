package com.weekendinc.jet.model;

public class PagingQuery
{
    private long page;
    private long lastPage;
    private boolean isIdle;

    public PagingQuery()
    {
        this.page = 1;
        this.lastPage = -1;
        this.isIdle = true;
    }

    public long getPage()
    {
        return page;
    }

    public void setPage(long page)
    {
        this.page = page;
    }

    public long getLastPage()
    {
        return lastPage;
    }

    public void setLastPage()
    {
        this.lastPage = this.page;
    }

    public boolean isIdle()
    {
        return isIdle;
    }

    public void setIdle(boolean idle)
    {
        isIdle = idle;
    }

    public void nextPage()
    {
        this.page++;
    }

    public boolean isFirstPage()
    {
        return this.page <= 1;
    }

    public boolean isLastPage()
    {
        return this.lastPage == this.page;
    }
}
