package coid.customer.pickupondemand.jet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.IBaseOverflowMenuListener;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.utility.NumberFormatter;

public class PickupItemAdapter extends ArrayAdapter<PickupItem>
{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private IBaseOverflowMenuListener mOverflowMenuListener;

    public PickupItemAdapter(Context context, IBaseOverflowMenuListener overflowMenuListener)
    {
        this(context, new ArrayList<PickupItem>(), overflowMenuListener);
    }

    public PickupItemAdapter(Context context, List<PickupItem> pickupItemList, IBaseOverflowMenuListener overflowMenuListener)
    {
        super(context, R.layout.list_item_pickup_item, pickupItemList);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mOverflowMenuListener = overflowMenuListener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_item_pickup_item, parent, false);
            holder.ll_content_container = convertView.findViewById(R.id.ll_content_container);
            holder.tv_pickup_item_code = (TextView) convertView.findViewById(R.id.tv_pickup_item_code);
            holder.tv_unlock_code = (TextView) convertView.findViewById(R.id.tv_unlock_code);
            holder.tv_receiver_name = (TextView) convertView.findViewById(R.id.tv_receiver_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.img_menu = (ImageView) convertView.findViewById(R.id.img_menu);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        PickupItem pickupItem = getItem(position);
        if (pickupItem != null)
        {
            holder.tv_pickup_item_code.setText(pickupItem.getCode());
            holder.tv_unlock_code.setText(pickupItem.getUnlockCode());
            holder.tv_receiver_name.setText(pickupItem.getConsigneeName());
            holder.tv_address.setText(pickupItem.getConsigneeAddressDetail());
            holder.tv_price.setText(pickupItem.getFormattedTotalFeeString());

            holder.img_menu.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mOverflowMenuListener != null)
                        mOverflowMenuListener.onMenuClicked(view, position);
                }
            });
        }

        return convertView;
    }

    public void updateData(List<PickupItem> pickupItemList)
    {
        clear();
        addAll(pickupItemList);
        notifyDataSetChanged();
    }

    public Double getTotalPrice()
    {
        Double totalPrice = 0D;

        for (int i = 0; i < getCount(); i++)
        {
            PickupItem pickupItem = getItem(i);
            if (pickupItem != null)
                totalPrice += pickupItem.getTotalFee();
        }

        return totalPrice;
    }

    public String getFormattedTotalPrice()
    {
        if (getTotalPrice() <= 0)
            return "-";
        return mContext.getString(R.string.pod_currency) + " " + NumberFormatter.doubleToString(getTotalPrice(), 0);
    }

    private class ViewHolder
    {
        LinearLayout ll_content_container;
        TextView tv_pickup_item_code, tv_unlock_code, tv_receiver_name, tv_address, tv_price;
        ImageView img_menu;
    }
}
