package com.weekendinc.jet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.weekendinc.jet.R;
import com.weekendinc.jet.model.pojo.Connote;
import com.weekendinc.jet.model.pojo.Waybill;
import com.weekendinc.jet.view.event.WayBillHolderItemListener;

import java.util.List;

public class WayBillListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Waybill> mWaybillList;
    private int rowLayout;
    private Context mContext;
    private WayBillHolderItemListener wayBillHolderItemListener;
    private boolean mIsPinEnabled = true;
    private boolean mIsModeTracking = false;

    public WayBillListAdapter(List<Waybill> waybillList, int rowLayout, Context context, WayBillHolderItemListener wayBillHolderItemListener) {
        this.mWaybillList = waybillList;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.wayBillHolderItemListener = wayBillHolderItemListener;
    }

    public List<Waybill> getWaybillList() {
        return mWaybillList;
    }

    public void setWaybillList(List<Waybill> mWaybillList) {
        this.mWaybillList = mWaybillList;
    }

    public void setPinEnabled(boolean isPinEnabled)
    {
        mIsPinEnabled = isPinEnabled;
    }

    public void setTrackingModeEnabled(boolean isEnabled)
    {
        mIsModeTracking = isEnabled;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new ViewHolder(v, wayBillHolderItemListener);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tracking_failed, parent, false);
            return new FailedViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 0 = success & 1 = failed
        return mWaybillList.get(position).getId() != null ? 0 : 1;
}

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Waybill waybill = mWaybillList.get(position);

        if (getItemViewType(position) == 0) {
            ViewHolder viewHolder = (ViewHolder) holder;

            if (mIsModeTracking)
            {
                viewHolder.ll_header_container.setVisibility(View.VISIBLE);
                viewHolder.switch_pinned.setVisibility(View.GONE);
            }
            else
            {
                if (mIsPinEnabled)
                    viewHolder.switch_pinned.setVisibility(View.VISIBLE);
                else
                    viewHolder.switch_pinned.setVisibility(View.GONE);

                if (waybill.getIsFinished())
                    viewHolder.ll_header_container.setVisibility(View.GONE);
                else
                    viewHolder.ll_header_container.setVisibility(View.VISIBLE);
            }

            viewHolder.tv_waybill_number.setText(waybill.getAwbNumber());
            viewHolder.ll_item_list.removeAllViews(); /** 2017-06-08 > quick fix duplicate added view onScroll*/
            for (Connote connote : waybill.getConnotes()) {
                View connoteView = LayoutInflater.from(mContext).inflate(R.layout.custom_connote_item_layout, viewHolder.ll_item_list, false);
                TextView tv_item_description = (TextView) connoteView.findViewById(R.id.tv_item_description);
                tv_item_description.setText(connote.getItemDescription());
                viewHolder.ll_item_list.addView(connoteView);
            }
            viewHolder.tv_name.setText(waybill.getConsigneeName());
            viewHolder.tv_location.setText(waybill.getDisplayDestinationCity());
            viewHolder.tv_status.setText(waybill.getFirstConnoteStatus());
            viewHolder.tv_status_date.setText(waybill.getFirstConnoteDateString());
            viewHolder.switch_pinned.setChecked(waybill.getPinned());
            viewHolder.updatePinDrawable(waybill.getPinned());
        } else {
            FailedViewHolder failedViewHolder = (FailedViewHolder) holder;
            failedViewHolder.tvWaybillNumber.setText(waybill.getAwbNumber());
        }
    }

    @Override
    public int getItemCount() {
        return mWaybillList == null ? 0 : mWaybillList.size();
    }

    public void updateData(List<Waybill> waybillList, Boolean isReplace)
    {
        if (isReplace) mWaybillList.clear();
        mWaybillList.addAll(waybillList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header_container, ll_content_container, ll_item_list;
        TextView tv_waybill_number, tv_name, tv_location, tv_status, tv_status_date;
        Switch switch_pinned;
        ProgressBar progress_bar_pinned_switch;
        private WayBillHolderItemListener listener;

        public ViewHolder(View itemView, final WayBillHolderItemListener listener) {
            super(itemView);
            this.listener = listener;
            ll_header_container = (LinearLayout)itemView.findViewById(R.id.ll_header_container);
            ll_content_container = (LinearLayout) itemView.findViewById(R.id.ll_content_container);
            tv_waybill_number = (TextView) itemView.findViewById(R.id.tv_waybill_number);
            ll_item_list = (LinearLayout)itemView.findViewById(R.id.ll_item_list);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_status_date = (TextView) itemView.findViewById(R.id.tv_status_date);
            switch_pinned = (Switch) itemView.findViewById(R.id.switch_pinned);
            progress_bar_pinned_switch = (ProgressBar) itemView.findViewById(R.id.progress_bar_pinned_switch);

            View.OnClickListener contentClickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                        listener.onClickContainer(getLayoutPosition(), ViewHolder.this);
                }
            };
            tv_waybill_number.setOnClickListener(contentClickListener);
            ll_content_container.setOnClickListener(contentClickListener);

            switch_pinned.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    updatePinDrawable(switch_pinned.isChecked());
                    if (listener != null)
                        listener.onSwitchClicked(getLayoutPosition(), ViewHolder.this, switch_pinned.isChecked());
                }
            });
        }

        public void showPinnedProgressBar()
        {
            switch_pinned.setVisibility(View.GONE);
            progress_bar_pinned_switch.setVisibility(View.VISIBLE);
        }

        public void showPinnedSwitch()
        {
            switch_pinned.setVisibility(View.VISIBLE);
            progress_bar_pinned_switch.setVisibility(View.GONE);
        }

        public void setSwitchChecked(boolean isChecked)
        {
            switch_pinned.setChecked(isChecked);
            updatePinDrawable(isChecked);
        }

        public void setSwitchRevert()
        {
            switch_pinned.setChecked(!switch_pinned.isChecked());
            updatePinDrawable(switch_pinned.isChecked());
        }

        public void updatePinDrawable(boolean isChecked)
        {
            if (isChecked)
                tv_waybill_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hash_grey,0,R.drawable.ic_pin_red, 0);
            else
                tv_waybill_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hash_grey,0,R.drawable.ic_pin_light_grey, 0);
        }
    }

    public static class FailedViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWaybillNumber;

        public FailedViewHolder(View itemView) {
            super(itemView);
            tvWaybillNumber = (TextView) itemView.findViewById(R.id.tv_waybill_number);
        }
    }
}
