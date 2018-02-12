package com.weekendinc.jet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weekendinc.jet.R;
import com.weekendinc.jet.model.pojo.Connote;
import com.weekendinc.jet.view.event.WayBillListHolderItemListener;

import java.util.List;

public class WayBillItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Connote> connotes;
    private int rowLayout;
    private Context mContext;
    private WayBillListHolderItemListener listener;

    public WayBillItemListAdapter(List<Connote> connotes, int rowLayout, Context context, WayBillListHolderItemListener listener) {
        this.connotes = connotes;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.listener = listener;
    }

    public List<Connote> getConnotes() {
        return connotes;
    }

    public void setConnotes(List<Connote> connotes) {
        this.connotes = connotes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return  new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Connote connote = connotes.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvItemDescription.setText(connote.getItemDescription());
        viewHolder.tvName.setText(connote.getConsigneeName());
        viewHolder.tvLocation.setText(connote.getDestinationLocationName());
    }

    @Override
    public int getItemCount() {
        return connotes == null ? 0 : connotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private WayBillListHolderItemListener listener;
        public ViewGroup container;
        public TextView tvItemDescription, tvName, tvLocation;


        public ViewHolder(View view, WayBillListHolderItemListener listener) {
            super(view);
            this.listener = listener;
            tvItemDescription = (TextView) view.findViewById(R.id.tv_item_description);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvLocation = (TextView) view.findViewById(R.id.tv_location);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            switch (v.getId()) {
                default:
                    if (listener != null) {
                        listener.onClickContainer(position, this);
                    }
                    break;
            }
        }
    }
}
