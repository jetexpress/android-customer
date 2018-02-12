package com.weekendinc.jet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weekendinc.jet.model.TrackingModel;
import com.weekendinc.jet.model.pojo.Connote;
import com.weekendinc.jet.model.pojo.ConnoteTrack;
import com.weekendinc.jet.model.pojo.Waybill;
import com.weekendinc.jet.utils.SubscriptionUtils;

import java.util.Collections;
import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class WaybillDetailActivity extends AppCompatActivity
{

    public static final String WAYBILL_EXTRA = "WaybillExtraParam";
    public static final String CONNOTE_EXTRA = "ConnoteExtraParam";

    final private CompositeSubscription mSubscription = new CompositeSubscription();
    private Waybill mWaybill;
    private Connote mConnote;

    private TextView tv_timed_out;
    private Button btn_retry;
    private ProgressBar progress_bar_waybill;
    private ScrollView sv_content_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        setView();
        setEvent();
        requestWaybillDetail();
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

    private void setData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(WAYBILL_EXTRA))
            mWaybill = bundle.getParcelable(WAYBILL_EXTRA);
        if (bundle != null && bundle.containsKey(CONNOTE_EXTRA))
            mConnote = bundle.getParcelable(CONNOTE_EXTRA);
    }

    private void setView()
    {
        setContentView(R.layout.activity_waybill_detail);
        tv_timed_out = findViewById(R.id.tv_timed_out);
        btn_retry = findViewById(R.id.btn_retry);
        progress_bar_waybill = findViewById(R.id.progress_bar_waybill);
        sv_content_container = findViewById(R.id.sv_content_container);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Utility.Message.get(R.string.waybill_detail));
        }
    }

    private void setEvent()
    {
        btn_retry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                requestWaybillDetail();
            }
        });
    }

    private void requestWaybillDetail()
    {
        showProgressBar();
        mSubscription.add(
                SubscriptionUtils.subscribing(
                        TrackingModel.getInstance().getWayBillTracks(mWaybill.getAwbNumber(), mConnote.getConnoteCode()),
                        doOnError,
                        doOnSuccess
                )
        );
    }

    private Action1<Throwable> doOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            throwable.printStackTrace();
            showRetry();
        }
    };

    private Action1<List<List<ConnoteTrack>>> doOnSuccess = new Action1<List<List<ConnoteTrack>>>() {
        @Override
        public void call(List<List<ConnoteTrack>> lists)
        {
            showTrackingData(lists);
            showContent();
        }
    };

    /** 2017-06-08 > quick fix trackDate grouping for Locale DateTime */
    private void showTrackingData(List<List<ConnoteTrack>> connoteTrackListGroupedByDay)
    {
        LinearLayout trackingDetail = (LinearLayout) findViewById(R.id.ll_tracking_detail);

        ((TextView) findViewById(R.id.tv_item_description)).setText(mConnote.getItemDescription());
        ((TextView) findViewById(R.id.tv_name)).setText(mWaybill.getConsigneeName());
        ((TextView) findViewById(R.id.tv_location)).setText(mWaybill.getDisplayDestinationCity());
        ((TextView) findViewById(R.id.tv_weight)).setText(String.format("%s kg", mWaybill.getTotalWeight()));
        ((TextView) findViewById(R.id.tv_volume)).setText(String.format("%s cm³", mWaybill.getTotalVolume()));
        ((TextView) findViewById(R.id.tv_number_of_item)).setText(String.valueOf(mConnote.getTotalPart()));
        ((TextView) findViewById(R.id.tv_address)).setText(mWaybill.getDisplayOriginCity());
        ((TextView) findViewById(R.id.tv_destination)).setText(mConnote.getDestinationLocationName());

        ConnoteTrack currentConnoteTrack = null;
        Collections.reverse(connoteTrackListGroupedByDay);

        for (List<ConnoteTrack> connoteTrackListOneDay : connoteTrackListGroupedByDay)
        {
            Collections.reverse(connoteTrackListOneDay);
            for (ConnoteTrack connoteTrack : connoteTrackListOneDay)
            {
                String day = connoteTrack.getTrackDayOfMonthString();
                String month = connoteTrack.getTrackMonthString();
                String time = connoteTrack.getTrackTimeString();

                View newDay = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tracking_day_item, trackingDetail, false);
                ((TextView) newDay.findViewById(R.id.tv_date)).setText(day);
                ((TextView) newDay.findViewById(R.id.tv_month)).setText(month);
                ((TextView) newDay.findViewById(R.id.tv_city)).setText(connoteTrack.getLocation());
                ((TextView) newDay.findViewById(R.id.tv_status)).setText(connoteTrack.getDisplayedStatus());
                ((TextView) newDay.findViewById(R.id.tv_time)).setText(time);

                if (currentConnoteTrack != null && currentConnoteTrack.isTrackSameDay(connoteTrack))
                {
                    newDay.findViewById(R.id.tv_date).setVisibility(View.INVISIBLE);
                    newDay.findViewById(R.id.tv_month).setVisibility(View.INVISIBLE);
                }
                else
                {
                    if (currentConnoteTrack == null)
                    {
                        ((TextView) newDay.findViewById(R.id.tv_date)).setTextColor(Color.RED);
                        ((TextView) newDay.findViewById(R.id.tv_month)).setTextColor(Color.RED);
                    }
                    currentConnoteTrack = connoteTrack;
                }

                trackingDetail.addView(newDay);
            }
        }
    }

    private void showContent()
    {
        sv_content_container.setVisibility(View.VISIBLE);
        tv_timed_out.setVisibility(View.GONE);
        btn_retry.setVisibility(View.GONE);
        progress_bar_waybill.setVisibility(View.GONE);
    }

    private void showRetry()
    {
        sv_content_container.setVisibility(View.GONE);
        tv_timed_out.setVisibility(View.VISIBLE);
        btn_retry.setVisibility(View.VISIBLE);
        progress_bar_waybill.setVisibility(View.GONE);
    }

    private void showProgressBar()
    {
        sv_content_container.setVisibility(View.GONE);
        tv_timed_out.setVisibility(View.GONE);
        btn_retry.setVisibility(View.GONE);
        progress_bar_waybill.setVisibility(View.VISIBLE);
    }
}
