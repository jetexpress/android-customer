package com.weekendinc.jet.fragments.listener;

/**
 * Created by Fadhlan on 2/16/15.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private Context context;
    private int previousTotal = 0; // The total number of items in the dataset after the last load

    public boolean isLoading() {
        return loading;
    }

    private boolean loading = true; // True if we are still waiting for the last set of data to
    // load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current
    // scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    private String tagPicasso = "";

    public EndlessRecyclerOnScrollListener(Context context,
                                           LinearLayoutManager linearLayoutManager,
                                           String tagPicasso) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.context = context;
        this.tagPicasso = tagPicasso;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount - visibleThreshold > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {

            current_page++;
            onLoadMore(current_page);

            loading = true;
        }
    }
    public void reset() {
        current_page=1;
        previousTotal=0;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE
                || newState == RecyclerView.SCROLL_STATE_DRAGGING)
            Picasso.with(context).resumeTag(tagPicasso);
        else {
            Picasso.with(context).pauseTag(tagPicasso);
        }
    }


    public abstract void onLoadMore(int current_page);
}