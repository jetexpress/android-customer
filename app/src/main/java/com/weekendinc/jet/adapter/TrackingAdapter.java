package com.weekendinc.jet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.weekendinc.jet.R;

import java.util.ArrayList;
import java.util.List;

public class TrackingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int ITEM_TYPE = 1;
    private static final int FOOTER_TYPE = 2;

    private List<String> list;
    private Context mContext;
    private View.OnClickListener listener;

    public TrackingAdapter(Context context, View.OnClickListener listener) {
        this.list = new ArrayList<>();
        list.add("");
        this.mContext = context;
        this.listener = listener;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_header, parent, false);
            return new HeaderViewHolder(v);
        }
        else if (viewType == ITEM_TYPE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tracking, parent, false);
            return new ItemViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_footer, parent, false);
            return new FooterViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE)
        {
            final ItemViewHolder vh = (ItemViewHolder) holder;
            if (position == 1)
                vh.img_clear.setVisibility(View.GONE);
            else
                vh.img_clear.setVisibility(View.VISIBLE);

            vh.setTrackingWatcher(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    list.set(vh.getAdapterPosition() - 1, s.toString().trim());
                }
            });
            vh.removeTextWatcher();
            vh.et_tracking.setText(list.get(position - 1));
            vh.addTextWatcher();
            vh.img_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClick(v);
                    list.remove(vh.getAdapterPosition() - 1);
                    notifyDataSetChanged();
                }
            });
        }
        else if (getItemViewType(position) == FOOTER_TYPE)
        {
            final FooterViewHolder vh = (FooterViewHolder) holder;
            vh.btn_add_waybill.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (list.size() >= 10)
                        return;
                    list.add("");
                    notifyDataSetChanged();

                    if (listener != null)
                        listener.onClick(v);
                }
            });
            vh.btn_track.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                        listener.onClick(v);
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        } else if (list != null && list.size() > 0 && position > 0 && position <= list.size()) {
            return ITEM_TYPE;
        } else {
            return FOOTER_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 2 : list.size() + 2;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View headerView) {
            super(headerView);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        EditText et_tracking;
        ImageView img_clear;
        TextWatcher trackingWatcher;


        ItemViewHolder(View itemView) {
            super(itemView);
            et_tracking = (EditText) itemView.findViewById(R.id.et_tracking);
            et_tracking.setImeOptions(EditorInfo.IME_ACTION_DONE);
            img_clear = (ImageView) itemView.findViewById(R.id.img_clear);
        }

        void setTrackingWatcher(TextWatcher textWatcher)
        {
            if (trackingWatcher == null)
                trackingWatcher = textWatcher;
        }

        void addTextWatcher()
        {
            if (trackingWatcher != null)
                et_tracking.addTextChangedListener(trackingWatcher);
        }

        void removeTextWatcher()
        {
            if (trackingWatcher != null)
                et_tracking.removeTextChangedListener(trackingWatcher);
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder
    {
        Button btn_add_waybill, btn_track;

        FooterViewHolder(View footerView)
        {
            super(footerView);
            btn_add_waybill = footerView.findViewById(R.id.btn_add_waybill);
            btn_track = footerView.findViewById(R.id.btn_track);
        }
    }

}
