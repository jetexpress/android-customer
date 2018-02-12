package com.weekendinc.jet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weekendinc.jet.adapter.WayBillListAdapter;
import com.weekendinc.jet.model.TrackingModel;
import com.weekendinc.jet.model.pojo.Waybill;
import com.weekendinc.jet.utils.SubscriptionUtils;
import com.weekendinc.jet.view.event.WayBillHolderItemListener;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.model.Login;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class TrackingResultActivity extends AppCompatActivity {

    public static final String AWB_NUMBER_ARRAY_STRING_EXTRA = "AWBNumberArrayString";

    final private CompositeSubscription mSubscription = new CompositeSubscription();

    private LinearLayoutManager mLayoutManager;
    private WayBillListAdapter mAdapter;
    private String[] mAWBNumberArrayString;
    private RecyclerView rcy_view;
    private ProgressBar progress_bar_tracking;
    private Button btn_retry;
    private TextView tv_timed_out;

    private WayBillHolderItemListener wayBillHolderItemListener = new WayBillHolderItemListener() {
        @Override
        public void onClickContainer(int position, WayBillListAdapter.ViewHolder holder) {
            Waybill waybill = mAdapter.getWaybillList().get(position);

            if (waybill == null || waybill.getConnotes() == null || waybill.getConnotes().size() <= 0)
                return;

            Bundle bundle = new Bundle();
            Intent intent = new Intent(TrackingResultActivity.this, WaybillDetailActivity.class);

            bundle.putParcelable(WaybillDetailActivity.WAYBILL_EXTRA, waybill);
            bundle.putParcelable(WaybillDetailActivity.CONNOTE_EXTRA, waybill.getConnotes().get(0));
            intent.putExtras(bundle);

            startActivity(intent);

        }

        @Override
        public void onSwitchClicked(int position, WayBillListAdapter.ViewHolder holder, boolean isChecked)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        setView();
        setEvent();
        requestTrackingWaybillList();
    }

    private void setData()
    {
        if (getIntent() != null && getIntent().getExtras().containsKey(AWB_NUMBER_ARRAY_STRING_EXTRA))
        {
            mAWBNumberArrayString = getIntent().getStringArrayExtra(AWB_NUMBER_ARRAY_STRING_EXTRA);
        }
    }

    private void setView()
    {
        setContentView(R.layout.activity_tracking_result);
        rcy_view = findViewById(R.id.rcy_view);
        progress_bar_tracking = findViewById(R.id.progress_bar_tracking);
        btn_retry = findViewById(R.id.btn_retry);
        tv_timed_out = findViewById(R.id.tv_timed_out);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_tracking_detail);
        }

        /** LayoutManager needs to be initialized before set to RecyclerView to avoid IllegalArgumentException : LinearLayoutManager is already attached to a RecyclerView */
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);

        mAdapter = new WayBillListAdapter(new ArrayList<Waybill>(), R.layout.card_waybill, this, wayBillHolderItemListener);
        mAdapter.setTrackingModeEnabled(true);

        rcy_view.setLayoutManager(mLayoutManager);
        rcy_view.setAdapter(mAdapter);
    }

    private void setEvent()
    {
        btn_retry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                requestTrackingWaybillList();
            }
        });
    }

    private void requestTrackingWaybillList()
    {
        showProgressBar();
        mSubscription.add(
                SubscriptionUtils.subscribing(
                        TrackingModel.getInstance().getWayBills(Login.getHeaderAuthorization(), mAWBNumberArrayString),
                        doOnError,
                        doOnSuccess
                )
        );
    }

    private Action1<Throwable> doOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            showRetry();
        }
    };

    private Action1<List<Waybill>> doOnSuccess = new Action1<List<Waybill>>() {
        @Override
        public void call(List<Waybill> resultGetWayBills) {
            showResultIncludeDuplicate(resultGetWayBills);
        }
    };

    private void showResultExcludeDuplicate(List<Waybill> resultWaybillList)
    {
        int i = 0;
        for(String awbNumber: mAWBNumberArrayString)
        {
            boolean isExist = false;

            for (Waybill waybill:resultWaybillList) {
                if (waybill.getAwbNumber().equals(awbNumber)) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                resultWaybillList.add(i, new Waybill(awbNumber));
            }

            i++;
        }

        mAdapter.updateData(resultWaybillList, true);
        showContent();
    }

    private void showResultIncludeDuplicate(List<Waybill> resultWaybillList)
    {
        List<Waybill> newResult = new ArrayList<>();

        for(String awbNumber: mAWBNumberArrayString)
        {
            boolean isExist = false;

            for (Waybill waybill:resultWaybillList)
            {
                if (waybill.getAwbNumber().equals(awbNumber)) {
                    isExist = true;
                    newResult.add(waybill);
                    break;
                }
            }

            if (!isExist)
                newResult.add(new Waybill(awbNumber));
        }

        mAdapter.updateData(newResult, true);
        showContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showContent()
    {
        rcy_view.setVisibility(View.VISIBLE);
        tv_timed_out.setVisibility(View.GONE);
        btn_retry.setVisibility(View.GONE);
        progress_bar_tracking.setVisibility(View.GONE);
    }

    private void showRetry()
    {
        rcy_view.setVisibility(View.GONE);
        tv_timed_out.setVisibility(View.VISIBLE);
        btn_retry.setVisibility(View.VISIBLE);
        progress_bar_tracking.setVisibility(View.GONE);
    }

    private void showProgressBar()
    {
        rcy_view.setVisibility(View.GONE);
        tv_timed_out.setVisibility(View.GONE);
        btn_retry.setVisibility(View.GONE);
        progress_bar_tracking.setVisibility(View.VISIBLE);
    }
}
