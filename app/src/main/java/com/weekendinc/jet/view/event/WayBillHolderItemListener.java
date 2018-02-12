package com.weekendinc.jet.view.event;

import android.widget.CompoundButton;

import com.weekendinc.jet.adapter.WayBillListAdapter;

public interface WayBillHolderItemListener {
    public void onClickContainer(int position, WayBillListAdapter.ViewHolder holder);
    void onSwitchClicked(int position, WayBillListAdapter.ViewHolder holder, boolean isChecked);
}
