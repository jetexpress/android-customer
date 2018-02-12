package com.weekendinc.jet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.weekendinc.jet.R;
import com.weekendinc.jet.WaybillDetailActivity;
import com.weekendinc.jet.adapter.WayBillListAdapter;
import com.weekendinc.jet.model.PagingQuery;
import com.weekendinc.jet.model.TrackingModel;
import com.weekendinc.jet.model.pojo.PinWaybill;
import com.weekendinc.jet.model.pojo.UnpinWaybill;
import com.weekendinc.jet.model.pojo.Waybill;
import com.weekendinc.jet.utils.SessionManager;
import com.weekendinc.jet.utils.SubscriptionUtils;
import com.weekendinc.jet.view.event.WayBillHolderItemListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.config.AppConfig;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public abstract class MyShipmentTabFragment extends BaseHasBasicLayoutFragment
{
    final private CompositeSubscription mSubscription = new CompositeSubscription();

    private LinearLayoutManager mLayoutManager;
    private WayBillListAdapter mAdapter;
    private PagingQuery mQuery;

    private LinearLayout ll_content_container;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView rcy_view;
    private ProgressBar progress_bar_paging;

    SessionManager mSessionManager;
    String mAccessToken, mTokenType, mRefreshToken;

    private final WayBillHolderItemListener wayBillHolderItemListener = new WayBillHolderItemListener() {
        @Override
        public void onClickContainer(int position, WayBillListAdapter.ViewHolder holder) {
            Waybill waybill = mAdapter.getWaybillList().get(position);

            if (waybill == null || waybill.getConnotes() == null || waybill.getConnotes().size() <= 0)
                return;

            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), WaybillDetailActivity.class);

            bundle.putParcelable(WaybillDetailActivity.WAYBILL_EXTRA, waybill);
            bundle.putParcelable(WaybillDetailActivity.CONNOTE_EXTRA, waybill.getConnotes().get(0));
            intent.putExtras(bundle);

            startActivity(intent);
        }

        @Override
        public void onSwitchClicked(int position, WayBillListAdapter.ViewHolder holder, boolean isChecked)
        {
            String awbNumber = mAdapter.getWaybillList().get(position).getAwbNumber();
            if (isChecked) {
                pinWaybill(holder, awbNumber);
            } else {
                unpinWaybill(holder, awbNumber);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getActivity());
        HashMap<String, String> loginDetails = mSessionManager.getLoginDetails();
        mAccessToken = loginDetails.get(SessionManager.ACCESS_TOKEN);
        mTokenType = loginDetails.get(SessionManager.TOKEN_TYPE);
        mRefreshToken = loginDetails.get(SessionManager.REFRESH_TOKEN);

        mAdapter = new WayBillListAdapter(new ArrayList<Waybill>(), R.layout.card_waybill, getActivity(), wayBillHolderItemListener);
        mAdapter.setPinEnabled(!isTrackFinished());
        mQuery = new PagingQuery();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_shipment_tab, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
        requestWaybillList();
    }

    @Override
    public void onDestroy()
    {
        mSubscription.clear();
        super.onDestroy();
    }

    @Override
    protected View getBaseContentLayout()
    {
        return ll_content_container;
    }

    @Override
    protected void onRetry()
    {
        mQuery = new PagingQuery();
        mSubscription.clear();
        requestWaybillList();
    }

    private void setView()
    {
        ll_content_container = (LinearLayout) findViewById(R.id.ll_content_container);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        progress_bar_paging = (ProgressBar) findViewById(R.id.progress_bar_paging);
        rcy_view = (RecyclerView) findViewById(R.id.rcy_view);

        /** LayoutManager needs to be initialized before set to RecyclerView to avoid IllegalArgumentException : LinearLayoutManager is already attached to a RecyclerView */
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);

        rcy_view.setLayoutManager(mLayoutManager);
        rcy_view.setAdapter(mAdapter);
    }

    private void setEvent()
    {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                mQuery = new PagingQuery();
                mSubscription.clear();
                requestWaybillList();
            }
        });
        rcy_view.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();

                    if (firstVisibleItem > 0 &&
                            visibleItemCount > 0 &&
                            totalItemCount > 0 &&
                            firstVisibleItem + visibleItemCount >= totalItemCount &&
                            mQuery.isIdle() &&
                            !mQuery.isLastPage())
                    {
                        mQuery.setIdle(false);
                        requestWaybillList();
                    }
                }
            }
        });
    }

    private void pinWaybill(final WayBillListAdapter.ViewHolder holder, String awbNumber) {
        holder.showPinnedProgressBar();
        mSubscription.add(
                SubscriptionUtils.subscribing(

                        TrackingModel.getInstance().pinWaybill(mTokenType + " " + mAccessToken, awbNumber),
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                holder.setSwitchRevert();
                                holder.showPinnedSwitch();
                                Toast.makeText(getContext(), R.string.waybill_failed_to_update_pin, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Action1<PinWaybill>() {
                            @Override
                            public void call(PinWaybill pinWaybill) {
                                holder.showPinnedSwitch();
                            }
                        }
                )
        );
    }

    private void unpinWaybill(final WayBillListAdapter.ViewHolder holder, String awbNumber) {
        holder.showPinnedProgressBar();
        mSubscription.add(
                SubscriptionUtils.subscribing(
                        TrackingModel.getInstance().unpinWaybill(mTokenType + " " + mAccessToken, awbNumber),
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                holder.setSwitchRevert();
                                holder.showPinnedSwitch();
                                Toast.makeText(getContext(), R.string.waybill_failed_to_update_pin, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Action1<UnpinWaybill>() {
                            @Override
                            public void call(UnpinWaybill unpinWaybill) {
                                holder.showPinnedSwitch();
                            }
                        }
                )
        );
    }

    private void requestWaybillList()
    {
        if (mQuery.isLastPage())
            return;

        if (mQuery.isFirstPage())
            showProgressBar();
        else
            showProgressBarPaging();

        mSubscription.add(
                SubscriptionUtils.subscribing(
                        TrackingModel.getInstance().getTrackingList(mTokenType + " " + mAccessToken, isTrackFinished(), AppConfig.DEFAULT_PAGING_SIZE, mQuery.getPage()),
                        doOnError,
                        doOnSuccess
                )
        );
    }

    private void showProgressBarPaging()
    {
        progress_bar_paging.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarPaging()
    {
        progress_bar_paging.setVisibility(View.GONE);
    }

    private Action1<Throwable> doOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            throwable.printStackTrace();
            mQuery.setIdle(true);
            swipe_refresh_layout.setRefreshing(false);
            hideProgressBarPaging();
            if (mQuery.isFirstPage())
                showRetry(R.string.request_timed_out);
        }
    };

    private Action1<List<Waybill>> doOnSuccess = new Action1<List<Waybill>>() {
        @Override
        public void call(List<Waybill> waybills) {
            mQuery.setIdle(true);
            swipe_refresh_layout.setRefreshing(false);
            hideProgressBarPaging();

            if (waybills.size() > 0)
            {
                mAdapter.updateData(waybills, mQuery.isFirstPage());
                if (waybills.size() < AppConfig.DEFAULT_PAGING_SIZE)
                    mQuery.setLastPage();
                else
                    mQuery.nextPage();
                showContent();
            }
            else
            {
                mQuery.setLastPage();
                if (mQuery.isFirstPage())
                    showNoData();
            }
        }
    };

    protected abstract boolean isTrackFinished();
}
