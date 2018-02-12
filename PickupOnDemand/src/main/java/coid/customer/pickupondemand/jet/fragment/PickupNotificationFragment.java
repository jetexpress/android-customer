package coid.customer.pickupondemand.jet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.adapter.NotificationAdapter;
import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.base.IBaseOverflowMenuListener;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.custom.DeleteConfirmationDialog;
import coid.customer.pickupondemand.jet.custom.PickupNotificationPagingLoader;
import coid.customer.pickupondemand.jet.custom.PickupNotificationPopupMenu;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.Pickup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupNotificationFragment extends BaseHasBasicLayoutFragment implements IBaseOverflowMenuListener
{
    public static final String NOTIFICATION_FROM_LIST_PARAM = "notificationFromList";

    private NotificationAdapter mNotificationAdapter;
    private LinearLayout ll_content_container;
    private SwipeRefreshLayout swipe_refresh_layout;
    private ListView list_view_notification;
    private ProgressBar progress_bar_paging;
    private PickupNotificationPagingLoader mPickupNotificationPagingLoader;

    public PickupNotificationFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mNotificationAdapter = new NotificationAdapter(getContext(), this);
        mPickupNotificationPagingLoader = getPagingLoader();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pickup_fragment_notification, container, false);
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
        setTitle(JETApplication.getContext().getString(R.string.pod_notification));
        Utility.Executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                NotificationPayload.setAllRead();
            }
        });
        if (mPickupNotificationPagingLoader != null && !mPickupNotificationPagingLoader.hasExecuted())
        {
            mPickupNotificationPagingLoader.execute();
            if (getLoaderManager().getLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID) != null)
                getLoaderManager().restartLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID, null, mPickupNotificationPagingLoader.getLoader());
            else
                getLoaderManager().initLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID, null, mPickupNotificationPagingLoader.getLoader());
        }
    }

    @Override
    public void onDestroy()
    {
        Utility.Executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                NotificationPayload.setAllOldAndRead();
            }
        });
        getActivity().invalidateOptionsMenu();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem notificationMenuItem = menu.findItem(R.id.action_notification);
        MenuItem clearAllMenuItem = menu.findItem(R.id.action_clear_all);
        if (notificationMenuItem != null)
            notificationMenuItem.setVisible(false);
        if (clearAllMenuItem != null)
            clearAllMenuItem.setVisible(true);
    }

    @Override
    protected View getBaseContentLayout()
    {
        return ll_content_container;
    }

    @Override
    protected void onRetry()
    {
        if (mPickupNotificationPagingLoader != null)
            mPickupNotificationPagingLoader.execute();
    }

    @Override
    public void onMenuClicked(View v, int position)
    {
        final NotificationPayload notificationPayload = mNotificationAdapter.getItem(position);
        if (notificationPayload == null)
            return;

        showNotificationPopupMenu(notificationPayload, v, position);
    }

    private void setView()
    {
        ll_content_container = (LinearLayout) findViewById(R.id.ll_content_container);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list_view_notification = (ListView) findViewById(R.id.list_view_notification);
        progress_bar_paging = (ProgressBar) findViewById(R.id.progress_bar_paging);
        list_view_notification.setAdapter(mNotificationAdapter);
    }

    private void setEvent()
    {
        list_view_notification.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (firstVisibleItem > 0 &&
                        visibleItemCount > 0 &&
                        totalItemCount > 0 &&
                        firstVisibleItem + visibleItemCount >= totalItemCount &&
                        mPickupNotificationPagingLoader.isIdle() &&
                        !mPickupNotificationPagingLoader.isLastPage())
                {
                    if (mNotificationAdapter.getCount() > 0)
                        mPickupNotificationPagingLoader.execute(mNotificationAdapter.getLastItemId());
                    else
                        mPickupNotificationPagingLoader.execute();
                }
            }
        });

        list_view_notification.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final NotificationPayload notificationPayload = mNotificationAdapter.getItem(position);
                if (notificationPayload == null)
                    return;

                View anchorView = view.findViewById(R.id.tv_code);
                if (anchorView == null)
                    return;

                showNotificationPopupMenu(notificationPayload, anchorView, position);
            }
        });

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                mPickupNotificationPagingLoader = getPagingLoader();
                mPickupNotificationPagingLoader.execute();
            }
        });
    }

    private PickupNotificationPagingLoader getPagingLoader()
    {
        return new PickupNotificationPagingLoader(getContext())
        {
            @Override
            public void onStart()
            {
                if (!hasExecuted())
                    showProgressBar();
                else
                    showPagingProgressBar();
            }

            @Override
            public void onSuccess(List<NotificationPayload> data)
            {
                mNotificationAdapter.updateData(data, getPage() == 1);
                showContent(data.size() > 0);
                hidePagingProgressBar();
                swipe_refresh_layout.setRefreshing(false);
            }

            @Override
            public void onNotificationReceived(NotificationPayload notificationPayload)
            {
                mNotificationAdapter.insert(notificationPayload, 0);
            }

            @Override
            public void onAllNotificationCleared()
            {
                mNotificationAdapter.clear();
                mNotificationAdapter.notifyDataSetChanged();
            }
        };
    }

    private void showNotificationPopupMenu(final NotificationPayload notificationPayload, View anchorView, final int position)
    {
        PickupNotificationPopupMenu pickupNotificationPopupMenu = new PickupNotificationPopupMenu(getContext(), anchorView, notificationPayload)
        {
            @Override
            public void onView()
            {
                Intent intent = new Intent(getContext(), PickupOnDemandActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(NOTIFICATION_FROM_LIST_PARAM, notificationPayload.toJson());
                startActivityForResult(intent, AppConfig.PICKUP_ACTIVITY_REQUEST_CODE);
            }

            @Override
            public void onDelete()
            {
                NotificationPayload.delete(NotificationPayload.class, notificationPayload.getId());
                NotificationPayload deletedNotificationPayload = mNotificationAdapter.getItem(position);
                if (deletedNotificationPayload != null)
                {
                    mNotificationAdapter.remove(deletedNotificationPayload);
                    mNotificationAdapter.notifyDataSetChanged();
                }
            }
        };

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(mContext, (MenuBuilder) pickupNotificationPopupMenu.getMenu(), anchorView);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    private void showPagingProgressBar()
    {
        progress_bar_paging.setVisibility(View.VISIBLE);
    }

    private void hidePagingProgressBar()
    {
        progress_bar_paging.setVisibility(View.GONE);
    }
}