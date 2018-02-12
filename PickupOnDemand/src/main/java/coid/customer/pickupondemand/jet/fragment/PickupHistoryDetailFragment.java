package coid.customer.pickupondemand.jet.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.adapter.PickupItemHistoryAdapter;
import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.request.PickupGetByCodeRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupHistoryDetailFragment extends BaseHasBasicLayoutFragment implements PickupItemHistoryAdapter.IPickupItemHistoryAdapterListener
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";

    private Pickup mPickup;
    private PickupItemHistoryAdapter mPickupItemHistoryAdapter;
    private PickupGetByCodeRequest mPickupGetByCodeRequest;

    private SwipeRefreshLayout swipe_refresh_layout;
    private RelativeLayout rl_content_container;
    private LinearLayout ll_bottom_container, ll_quick_pickup_info, ll_empty_pickup_item_info_container;
    private TextView tv_quick_pickup_item_count, tv_actual_pickup_item_count, tv_created_waybills_count;
    private ListView list_view_pickup_item;
    private EstimatedPriceView mEstimatedPriceBottomView;

    public PickupHistoryDetailFragment()
    {
        // Required empty public constructor
    }

    public static PickupHistoryDetailFragment newInstance(Pickup pickup)
    {
        PickupHistoryDetailFragment fragment = new PickupHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICKUP_ARGS_PARAM, pickup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);

        mPickupItemHistoryAdapter = new PickupItemHistoryAdapter(mContext);
        mPickupItemHistoryAdapter.setPickupItemHistoryAdapterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_history_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setValue();
        setEvent();
        setState();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String title = getString(R.string.pod_pickup_item_history_detail);
        if (mPickup != null)
            title = mPickup.getCode();
        setTitle(title);

        if (mPickupGetByCodeRequest != null && mPickupGetByCodeRequest.isSuccess())
            return;

        requestPickup();
    }

    @Override
    public void onDestroy()
    {
        if (mPickupGetByCodeRequest != null)
        {
            mPickupGetByCodeRequest.clear();
            mPickupGetByCodeRequest = null;
        }
        super.onDestroy();
    }

    @Override
    protected View getBaseContentLayout()
    {
        return rl_content_container;
    }

    @Override
    protected void onRetry()
    {
        requestPickup();
    }

    @Override
    public void onPickupItemCodeClicked(View v, PickupItem pickupItem)
    {
        viewPickupItemDetail(pickupItem);
    }

    @Override
    public void onPickupItemCodeLongClicked(View v, PickupItem pickupItem)
    {
        showCopyMenu(v, pickupItem.getCode());
    }

    @Override
    public void onWaybillNumberClicked(View v, PickupItem pickupItem)
    {
        viewPickupItemDetail(pickupItem);
    }

    @Override
    public void onWaybillNumberLongClicked(View v, PickupItem pickupItem)
    {
        showCopyMenu(v, pickupItem.getWaybillNumber());
    }

    private void setView()
    {
        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        mEstimatedPriceBottomView = new EstimatedPriceView(mContext);
        mEstimatedPriceBottomView.showPriceOnly();
        ll_bottom_container.addView(mEstimatedPriceBottomView, 0);

        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        rl_content_container = (RelativeLayout) findViewById(R.id.rl_content_container);
        ll_quick_pickup_info = (LinearLayout) findViewById(R.id.ll_quick_pickup_info);
        ll_empty_pickup_item_info_container = (LinearLayout) findViewById(R.id.ll_empty_pickup_item_info_container);
        tv_quick_pickup_item_count = (TextView) findViewById(R.id.tv_quick_pickup_item_count);
        tv_actual_pickup_item_count = (TextView) findViewById(R.id.tv_actual_pickup_item_count);
        tv_created_waybills_count = (TextView) findViewById(R.id.tv_created_waybills_count);
        list_view_pickup_item = (ListView) findViewById(R.id.list_view_pickup_item);
        list_view_pickup_item.setAdapter(mPickupItemHistoryAdapter);
    }

    private void setValue()
    {
        if (mPickup == null)
            return;

        tv_quick_pickup_item_count.setText(mPickup.getQuickPickupItemCountString());
        tv_actual_pickup_item_count.setText(mPickup.getActualPickupItemCountString());
        tv_created_waybills_count.setText(mPickup.getWaybillCreatedCountString());
    }

    private void setEvent()
    {
        list_view_pickup_item.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                PickupItem pickupItem = mPickupItemHistoryAdapter.getItem(position);
                if (pickupItem != null)
                    getNavigator().showFragment(PickupItemFragment.newViewInstance(pickupItem));
            }
        });
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (mPickup == null)
                {
                    swipe_refresh_layout.setRefreshing(false);
                    return;
                }
                requestPickup();
            }
        });
    }

    private void setState()
    {
        if (mPickup.getQuickPickupItemCount() != null && mPickup.getQuickPickupItemCount() > 0)
            ll_quick_pickup_info.setVisibility(View.VISIBLE);
        else
            ll_quick_pickup_info.setVisibility(View.GONE);

        if (mPickup.getPickupItemList() != null && mPickup.getPickupItemList().size() > 0)
        {
            ll_empty_pickup_item_info_container.setVisibility(View.GONE);
            list_view_pickup_item.setVisibility(View.VISIBLE);
        }
        else
        {
            if (mPickup.isCancelled())
                ll_empty_pickup_item_info_container.setVisibility(View.GONE);
            else
                ll_empty_pickup_item_info_container.setVisibility(View.VISIBLE);
            list_view_pickup_item.setVisibility(View.GONE);
        }
    }

    private void requestPickup()
    {
        mPickupGetByCodeRequest = new PickupGetByCodeRequest(mContext, mPickup.getCode())
        {
            @Override
            protected void onStartOnUIThread()
            {
                showProgressBar();
            }

            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                mPickup.update(response.body());
                mPickupItemHistoryAdapter.updateData(mPickup.getPickupItemList());
                mPickup.setTotalFee(mPickupItemHistoryAdapter.getTotalPrice());
                mEstimatedPriceBottomView.setEstimatedPrice(mPickup.getFormattedTotalFee());
//                tv_estimated_price.setText(mPickup.getFormattedTotalFee());
                swipe_refresh_layout.setRefreshing(false);
                setState();
                showContent();
            }

            @Override
            protected void hideLoadingDialog()
            {
                showRetry(R.string.pod_request_timed_out);
            }
        };
        mPickupGetByCodeRequest.executeAsync();
    }

    private void viewPickupItemDetail(PickupItem pickupItem)
    {
        if (pickupItem != null)
            getNavigator().showFragment(PickupItemFragment.newViewInstance(pickupItem));
    }

    private void showCopyMenu(View anchorView, final String willBeCopiedString)
    {
        PopupMenu copyPopupMenu = new PopupMenu(mContext, anchorView);
        copyPopupMenu.getMenuInflater().inflate(R.menu.copy_menu, copyPopupMenu.getMenu());
        copyPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                try
                {
                    ClipboardManager clipboard = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", willBeCopiedString);
                    clipboard.setPrimaryClip(clip);
                    return true;
                }
                catch (Exception ex)
                {
                    return false;
                }
            }
        });
        copyPopupMenu.show();
    }
}
