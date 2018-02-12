package coid.customer.pickupondemand.jet.fragment;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.activity.PickupOnDemandActivity;
import coid.customer.pickupondemand.jet.adapter.PickupItemAdapter;
import coid.customer.pickupondemand.jet.base.BaseHasBasicLayoutFragment;
import coid.customer.pickupondemand.jet.base.IBaseOverflowMenuListener;
import coid.customer.pickupondemand.jet.custom.DeleteConfirmationDialog;
import coid.customer.pickupondemand.jet.custom.EstimatedPriceView;
import coid.customer.pickupondemand.jet.custom.PickupItemPopupMenu;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.model.PickupQRCode;
import coid.customer.pickupondemand.jet.request.PickupDraftCreateRequest;
import coid.customer.pickupondemand.jet.request.PickupGetByCodeRequest;
import coid.customer.pickupondemand.jet.request.PickupItemCreateRequest;
import coid.customer.pickupondemand.jet.request.PickupItemDeleteRequest;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickupDetailFragment extends BaseHasBasicLayoutFragment implements
        IBaseOverflowMenuListener, PickupItemFragment.PickupItemFragmentListener
{
    private static final String PICKUP_ARGS_PARAM = "PickupParam";
    private static final int ADD_REQUEST_CODE = 101;
    private static final int EDIT_REQUEST_CODE = 102;

    private Pickup mPickup;
    private GoogleApiClient mGoogleApiClient;

    private TextView tv_add_pickup_item, tv_quick_pickup, tv_add_pickup_item_using_qr_code;
    //    private ImageView img_refresh_estimated_price;
    private LinearLayout ll_bottom_container, ll_quick_pickup_info, ll_empty_pickup_item_info_container;
    private TextView tv_quick_pickup_item_count, tv_actual_pickup_item_count, tv_created_waybills_count, tv_quick_pickup_status;
    private RelativeLayout rl_content_container, rl_continue;
    private SwipeRefreshLayout swipe_refresh_layout;
    private ListView list_view_pickup_item;
    private EstimatedPriceView mEstimatedPriceBottomView;

    private PickupItemAdapter mPickupItemAdapter;
    private PickupDraftCreateRequest mPickupDraftCreateRequest;
    private PickupGetByCodeRequest mPickupGetByCodeRequest;
    private PickupItemDeleteRequest mPickupItemDeleteRequest;
    private PickupItemCreateRequest mFirstPickupItemCreateRequest;
    private PickupItemCreateRequest mPickupItemCreateRequest;

    private PickupDetailFragmentListener mPickupDetailFragmentListener;

    public PickupDetailFragment()
    {
        // Required empty public constructor
    }

    public static PickupDetailFragment newInstance(Pickup pickup)
    {
        PickupDetailFragment fragment = new PickupDetailFragment();
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
        {
            mPickup = getArguments().getParcelable(PICKUP_ARGS_PARAM);
            mPickupGetByCodeRequest = getPickupRequest();
        }
        mPickupItemAdapter = new PickupItemAdapter(mContext, this);
        if (getTargetFragment() instanceof PickupDetailFragmentListener)
            mPickupDetailFragmentListener = (PickupDetailFragmentListener) getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setState();
        setValue();
        setEvent();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String title = getString(R.string.pod_pickup_create);
        if (mPickup != null)
            title = mPickup.getCode();
        setTitle(title);
        if (mPickupGetByCodeRequest != null && !mPickupGetByCodeRequest.isSuccess())
            mPickupGetByCodeRequest.executeAsync();
    }

    @Override
    public void onStop()
    {
        if (mPickupDraftCreateRequest != null)
            mPickupDraftCreateRequest.cancel();
        if (mPickupGetByCodeRequest != null)
            mPickupGetByCodeRequest.cancel();
        if (mFirstPickupItemCreateRequest != null)
            mFirstPickupItemCreateRequest.cancel();
        if (mPickupItemCreateRequest != null)
            mPickupItemCreateRequest.cancel();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        if (mPickupDraftCreateRequest != null)
        {
            mPickupDraftCreateRequest.cancel();
            mPickupDraftCreateRequest.clear();
            mPickupDraftCreateRequest = null;
        }

        if (mPickupGetByCodeRequest != null)
        {
            mPickupGetByCodeRequest.cancel();
            mPickupGetByCodeRequest.clear();
            mPickupGetByCodeRequest = null;
        }

        if (mFirstPickupItemCreateRequest != null)
        {
            mFirstPickupItemCreateRequest.clear();
            mFirstPickupItemCreateRequest = null;
        }

        if (mPickupItemCreateRequest != null)
        {
            mPickupItemCreateRequest.clear();
            mPickupItemCreateRequest = null;
        }
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null)
        {
            PickupQRCode pickupQRCode = PickupQRCode.createFromDataString(result.getContents());
            PickupItem pickupItem = PickupItem.createFromQRCode(pickupQRCode);
            if (mPickup != null)
            {
                if (!mPickup.getPickupItemList().contains(pickupItem))
                    requestCreatePickupItem(pickupItem);
                else
                    showToast(getString(R.string.pod_pickup_item_duplicate_qr_code_message));
            }
            else
                requestCreatePickupDraft(pickupItem);
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected View getBaseContentLayout()
    {
        return rl_content_container;
    }

    @Override
    public void onMenuClicked(View v, int position)
    {
        final PickupItem pickupItem = mPickupItemAdapter.getItem(position);
        if (pickupItem == null)
            return;

        showPickupItemPopupMenu(pickupItem, v);
    }

    @Override
    protected void onRetry()
    {
        if (mPickupGetByCodeRequest != null)
            mPickupGetByCodeRequest.executeAsync();
    }

    @Override
    public void onPickupCreated(Pickup pickup)
    {
        if (pickup == null)
        {
            Log.d("PICKUP_DETAIL_127", "PICKUP NULL");
            return;
        }

        mPickup = pickup;
        if (mPickupDetailFragmentListener != null)
            mPickupDetailFragmentListener.onPickupCreated(mPickup);
    }

    @Override
    public void onPickupItemCreated(PickupItem pickupItem)
    {
        mPickupItemAdapter.add(pickupItem);
        mPickupItemAdapter.notifyDataSetChanged();
        mEstimatedPriceBottomView.setEstimatedPrice(mPickupItemAdapter.getFormattedTotalPrice());
        mPickup.getPickupItemList().add(pickupItem);
        mPickup.setTotalFee(mPickupItemAdapter.getTotalPrice());
        if (mPickupDetailFragmentListener != null)
            mPickupDetailFragmentListener.onPickupItemChanged();
        setState();
    }

    @Override
    public void onPickupItemUpdated(PickupItem pickupItem)
    {
        mPickupItemAdapter.notifyDataSetChanged();
        mEstimatedPriceBottomView.setEstimatedPrice(mPickupItemAdapter.getFormattedTotalPrice());
        mPickup.setTotalFee(mPickupItemAdapter.getTotalPrice());
        if (mPickupDetailFragmentListener != null)
            mPickupDetailFragmentListener.onPickupItemChanged();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof PickupOnDemandActivity)
            mGoogleApiClient = ((PickupOnDemandActivity) context).getGoogleApiClient();
    }

    private void setView()
    {
        ll_bottom_container = (LinearLayout) findViewById(R.id.ll_bottom_container);
        mEstimatedPriceBottomView = new EstimatedPriceView(mContext);
        mEstimatedPriceBottomView.showPriceOnly();
        ll_bottom_container.addView(mEstimatedPriceBottomView, 0);

        ll_quick_pickup_info = (LinearLayout) findViewById(R.id.ll_quick_pickup_info);
        ll_empty_pickup_item_info_container = (LinearLayout) findViewById(R.id.ll_empty_pickup_item_info_container);
        tv_quick_pickup_item_count = (TextView) findViewById(R.id.tv_quick_pickup_item_count);
        tv_actual_pickup_item_count = (TextView) findViewById(R.id.tv_actual_pickup_item_count);
        tv_created_waybills_count = (TextView) findViewById(R.id.tv_created_waybills_count);
        tv_quick_pickup_status = (TextView) findViewById(R.id.tv_quick_pickup_status);

        rl_content_container = (RelativeLayout) findViewById(R.id.rl_content_container);
        tv_add_pickup_item = (TextView) findViewById(R.id.tv_add_pickup_item);
        tv_quick_pickup = (TextView) findViewById(R.id.tv_quick_pickup);
        tv_add_pickup_item_using_qr_code = (TextView) findViewById(R.id.tv_add_pickup_item_using_qr_code);
        rl_continue = (RelativeLayout) findViewById(R.id.rl_continue);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list_view_pickup_item = (ListView) findViewById(R.id.list_view_pickup_item);
        list_view_pickup_item.setAdapter(mPickupItemAdapter);
    }

    private void setState()
    {
        if (mPickup != null)
        {
            if (!mPickup.isDraft())
            {
                tv_quick_pickup.setVisibility(View.GONE);
                tv_add_pickup_item_using_qr_code.setVisibility(View.GONE);
                tv_add_pickup_item.setVisibility(View.GONE);
                rl_continue.setVisibility(View.GONE);

                if (mPickup.getQuickPickupItemCount() != null && mPickup.getQuickPickupItemCount() > 0)
                {
                    ll_quick_pickup_info.setVisibility(View.VISIBLE);
                    if (mPickup.getPickupItemList() != null && mPickup.getPickupItemList().size() > 0)
                    {
                        ll_empty_pickup_item_info_container.setVisibility(View.GONE);
                        list_view_pickup_item.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        ll_empty_pickup_item_info_container.setVisibility(View.VISIBLE);
                        list_view_pickup_item.setVisibility(View.GONE);
                    }
                }
                else
                    ll_quick_pickup_info.setVisibility(View.GONE);
            }
            else
            {
                if (mPickup.getTotalItem() != null && mPickup.getTotalItem() > 0)
                    tv_quick_pickup.setVisibility(View.INVISIBLE);
                else
                {
                    if (mPickupItemAdapter.getCount() > 0)
                        tv_quick_pickup.setVisibility(View.INVISIBLE);
                    else
                        tv_quick_pickup.setVisibility(View.VISIBLE);
                }
            }
            mEstimatedPriceBottomView.setEstimatedPrice(mPickup.getTotalFee());
//            tv_estimated_price.setText(mPickup.getFormattedTotalFee());
        }
        else
        {
            tv_quick_pickup.setVisibility(View.VISIBLE);
            tv_add_pickup_item_using_qr_code.setVisibility(View.VISIBLE);
            tv_add_pickup_item.setVisibility(View.VISIBLE);
            rl_continue.setVisibility(View.VISIBLE);
            mEstimatedPriceBottomView.setEstimatedPrice(Pickup.getFormattedEmptyFee());
//            tv_estimated_price.setText(Pickup.getFormattedEmptyFee());
        }
//        img_refresh_estimated_price.setVisibility(View.GONE);
    }

    private void setValue()
    {
        if (mPickup == null || mPickup.isDraft() || mPickup.getQuickPickupItemCount() == null || mPickup.getQuickPickupItemCount() <= 0)
            return;

        tv_quick_pickup_item_count.setText(mPickup.getQuickPickupItemCountString());
        tv_actual_pickup_item_count.setText(mPickup.getActualPickupItemCountString());
        tv_created_waybills_count.setText(mPickup.getWaybillCreatedCountString());
        tv_quick_pickup_status.setText(mPickup.getReadableStatus());
    }

    private void setEvent()
    {
        tv_quick_pickup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPickup != null)
                    getNavigator().showFragment(PickupImageFragment.newInstance(mPickup));
                else
                    requestCreatePickupDraft(null);
            }
        });
        tv_add_pickup_item_using_qr_code.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(PickupDetailFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt(getString(R.string.pod_pickup_item_qr_code_info));
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
        tv_add_pickup_item.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PickupItemFragment pickupItemFragment;
                if (mPickup != null)
                    pickupItemFragment = PickupItemFragment.newAddInstance(mPickup);
                else
                    pickupItemFragment = new PickupItemFragment();
                pickupItemFragment.setTargetFragment(PickupDetailFragment.this, ADD_REQUEST_CODE);
                getNavigator().showFragment(pickupItemFragment);
            }
        });
        rl_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mPickupItemAdapter.getCount() <= 0)
                    showToast(getString(R.string.pod_pickup_item_not_available));
                else
                {
                    getNavigator().showFragment(PaymentMethodFragment.newInstance(mPickup));
                }
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
                mPickupGetByCodeRequest = getPickupRequest();
                mPickupGetByCodeRequest.executeAsync();
            }
        });
        list_view_pickup_item.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final PickupItem pickupItem = mPickupItemAdapter.getItem(position);
                if (pickupItem == null)
                    return;

                View anchorView = view.findViewById(R.id.tv_receiver_name);
                if (anchorView == null)
                    return;

                showPickupItemPopupMenu(pickupItem, anchorView);
            }
        });
    }

    private void requestCreatePickupDraft(final PickupItem firstPickupItem)
    {
        Double lat;
        Double lng;

        Location location = Utility.Location.getCurrentLocationByFused(mGoogleApiClient);
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
        else
        {
            lat = null;
            lng = null;
        }

        mPickupDraftCreateRequest = new PickupDraftCreateRequest(mContext, lat, lng)
        {
            @Override
            protected void onSuccessOnUIThread(Response<Pickup> response)
            {
                mPickup = response.body();
                if (mPickupDetailFragmentListener != null)
                    mPickupDetailFragmentListener.onPickupCreated(response.body());

                if (firstPickupItem != null)
                    requestCreateFirstPickupItem(firstPickupItem);
                else
                {
                    super.onSuccessOnUIThread(response);
                    getNavigator().showFragment(PickupImageFragment.newInstance(mPickup));
                }
            }
        };
        mPickupDraftCreateRequest.executeAsync();
    }

    private void requestCreateFirstPickupItem(PickupItem firstPickupItem)
    {
        mFirstPickupItemCreateRequest = new PickupItemCreateRequest(mContext, mPickup.getCode(), firstPickupItem)
        {
            @Override
            protected void onStartOnUIThread(){}
            @Override
            protected void onSuccessOnUIThread(Response<PickupItem> response)
            {
                super.onSuccessOnUIThread(response);
                onPickupItemCreated(response.body());
            }
        };
        mFirstPickupItemCreateRequest.executeAsync();
    }

    private void requestCreatePickupItem(PickupItem pickupItem)
    {
        mPickupItemCreateRequest = new PickupItemCreateRequest(mContext, mPickup.getCode(), pickupItem)
        {
            @Override
            protected void onSuccessOnUIThread(Response<PickupItem> response)
            {
                super.onSuccessOnUIThread(response);
                onPickupItemCreated(response.body());
            }
        };
        mPickupItemCreateRequest.executeAsync();
    }

    private PickupGetByCodeRequest getPickupRequest()
    {
        return new PickupGetByCodeRequest(mContext, mPickup.getCode())
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
                mPickupItemAdapter.updateData(mPickup.getPickupItemList());
                mPickup.setTotalFee(mPickupItemAdapter.getTotalPrice());
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
    }

    private PickupItemDeleteRequest getDeletePickupRequest(final PickupItem pickupItem)
    {
        return new PickupItemDeleteRequest(mContext, mPickup.getCode(), pickupItem.getCode())
        {
            @Override
            protected void onSuccessOnUIThread(Response<PickupItem> response)
            {
                super.onSuccessOnUIThread(response);
                mPickupItemAdapter.remove(pickupItem);
                mPickupItemAdapter.notifyDataSetChanged();
                mPickup.getPickupItemList().remove(pickupItem);
                mEstimatedPriceBottomView.setEstimatedPrice(mPickupItemAdapter.getFormattedTotalPrice());
//                tv_estimated_price.setText(mPickupItemAdapter.getFormattedTotalPrice());
                mPickup.setTotalFee(mPickupItemAdapter.getTotalPrice());
                if (mPickupDetailFragmentListener != null)
                    mPickupDetailFragmentListener.onPickupItemChanged();
                setState();
            }
        };
    }

    private void showPickupItemPopupMenu(final PickupItem pickupItem, View v)
    {
        PickupItemPopupMenu pickupItemPopupMenu = new PickupItemPopupMenu(mContext, v, pickupItem, mPickup.isDraft())
        {
            @Override
            public void onEdit()
            {
                PickupItemFragment pickupItemFragment = PickupItemFragment.newEditInstance(mPickup, pickupItem);
                pickupItemFragment.setTargetFragment(PickupDetailFragment.this, EDIT_REQUEST_CODE);
                getNavigator().showFragment(pickupItemFragment);
            }

            @Override
            public void onView()
            {
                getNavigator().showFragment(PickupItemFragment.newViewInstance(pickupItem));
            }

            @Override
            public void onDelete()
            {
                String title = getString(R.string.pod_pickup_item_delete);
                String message = getString(R.string.pod_pickup_item_delete_confirmation);
                DeleteConfirmationDialog deleteConfirmationDialog = new DeleteConfirmationDialog(mContext, title, message)
                {
                    @Override
                    public void onOKClicked()
                    {
                        mPickupItemDeleteRequest = getDeletePickupRequest(pickupItem);
                        mPickupItemDeleteRequest.executeAsync();
                    }
                };
                deleteConfirmationDialog.show();
            }
        };

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(mContext, (MenuBuilder) pickupItemPopupMenu.getMenu(), v);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    public interface PickupDetailFragmentListener
    {
        void onPickupCreated(Pickup pickup);
        void onPickupItemChanged();
    }
}
