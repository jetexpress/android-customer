package coid.customer.pickupondemand.jet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.adapter.PickupAdapter;
import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.base.BasePagingRequest;
import coid.customer.pickupondemand.jet.base.IBaseOverflowMenuListener;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.QueryResult;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PickupOnDemandListFragment extends BaseHasBasicLayoutFragment
{
    public static final String TITLE_ARGS_PARAM = "titleParam";

    private ListView list_view_pickup;
    private SwipeRefreshLayout swipe_refresh_layout;
    private ProgressBar progress_bar_paging;
    private LinearLayout ll_content_container;
    private FloatingActionButton fab_new_pickup;

    private PickupAdapter mPickupAdapter;
    protected BasePagingRequest<Pickup> mPickupRequest;

    public PickupOnDemandListFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPickupAdapter = new PickupAdapter(mContext);
        mPickupRequest = getPickupRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_on_demand_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(getString(R.string.pod_pickup));
        if (mPickupRequest != null && !mPickupRequest.isSuccess())
            mPickupRequest.executeAsync();
    }

    @Override
    protected View getBaseContentLayout()
    {
        return ll_content_container;
    }

    @Override
    protected void onRetry()
    {
        mPickupRequest.executeAsync();
    }

    private void setView()
    {
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        progress_bar_paging = (ProgressBar) findViewById(R.id.progress_bar_paging);
        list_view_pickup = (ListView) findViewById(R.id.list_view_pickup);
        ll_content_container = (LinearLayout) findViewById(R.id.ll_content_container);
        fab_new_pickup = (FloatingActionButton) findViewById(R.id.fab_new_pickup);

        list_view_pickup.setAdapter(mPickupAdapter);
    }

    private void setEvent()
    {
        list_view_pickup.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState)
            {
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL)
                    fab_new_pickup.animate().alpha(0.0f).setDuration(500);
                else
                    fab_new_pickup.animate().alpha(1.0f).setDuration(1000);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (firstVisibleItem > 0 &&
                        visibleItemCount > 0 &&
                        totalItemCount > 0 &&
                        firstVisibleItem + visibleItemCount >= totalItemCount &&
                        mPickupRequest.isIdle() &&
                        !mPickupRequest.isLastPage())
                {
                    mPickupRequest.executeAsync();
                }
            }
        });
        list_view_pickup.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                onListItemClicked(parent, view, position, id);
            }
        });
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshList();
            }
        });

        fab_new_pickup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onFABNewPickupClicked();
            }
        });
    }

    public BasePagingRequest<Pickup> getPickupRequest()
    {
        return new BasePagingRequest<Pickup>(mContext)
        {
            @Override
            public Call<QueryResult<Pickup>> getCall()
            {
                setPage(getQuery() != null ? getQuery().getPage() + 1 : 1);
                return getPickupCall(getPage());
            }

            @Override
            protected void onStartOnUIThread()
            {
                if (!hasExecuted())
                    showProgressBar();
                else
                    showPagingProgressBar();
            }

            @Override
            protected void onSuccessOnUIThread(Response<QueryResult<Pickup>> response)
            {
                super.onSuccessOnUIThread(response);
                mPickupAdapter.updateData(response.body().getResult(), getQuery().getPage() == 1);
                showContent(response.body().getResult().size() > 0);
                hidePagingProgressBar();
                swipe_refresh_layout.setRefreshing(false);
            }

            @Override
            protected void hideLoadingDialog()
            {
                if (!hasExecuted())
                    showRetry(R.string.pod_request_timed_out);
            }
        };
    }

    public void refreshList()
    {
        mPickupRequest = getPickupRequest();
        mPickupRequest.executeAsync();
    }

    protected void showFABNewPickup()
    {
        fab_new_pickup.setVisibility(View.VISIBLE);
    }
    protected void hideFABNewPickup()
    {
        fab_new_pickup.setVisibility(View.GONE);
    }
    public PickupAdapter getPickupAdapter()
    {
        return mPickupAdapter;
    }
    protected void setPickupMenuListener(IBaseOverflowMenuListener menuListener)
    {
        mPickupAdapter.setOverflowMenuListener(menuListener);
    }
    public void showPagingProgressBar()
    {
        progress_bar_paging.setVisibility(View.VISIBLE);
    }
    public void hidePagingProgressBar()
    {
        progress_bar_paging.setVisibility(View.GONE);
    }
    protected void onFABNewPickupClicked(){}
    protected void onListItemClicked(AdapterView<?> parent, View view, int position, long id){}
    public abstract String getTitle();
    public abstract Call<QueryResult<Pickup>> getPickupCall(Long currentPage);
}
