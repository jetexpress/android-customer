package coid.customer.pickupondemand.jet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.utility.NumberFormatter;

public class PickupItemHistoryAdapter extends ArrayAdapter<PickupItem>
{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private IPickupItemHistoryAdapterListener mPickupItemHistoryAdapterListener;

    public PickupItemHistoryAdapter(Context context)
    {
        this(context, new ArrayList<PickupItem>());
    }

    public PickupItemHistoryAdapter(Context context, List<PickupItem> pickupItemList)
    {
        super(context, R.layout.list_item_pickup_item_history, pickupItemList);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_item_pickup_item_history, parent, false);
            holder.ll_content_container = convertView.findViewById(R.id.ll_content_container);
            holder.tv_pickup_item_code = (TextView) convertView.findViewById(R.id.tv_pickup_item_code);
            holder.tv_waybill_number = (TextView) convertView.findViewById(R.id.tv_waybill_number);
            holder.tv_receiver_name = (TextView) convertView.findViewById(R.id.tv_receiver_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final PickupItem pickupItem = getItem(position);
        if (pickupItem != null)
        {
            holder.tv_pickup_item_code.setText(pickupItem.getCode());
            holder.tv_waybill_number.setText(pickupItem.getFormattedWaybillNumber());
            holder.tv_receiver_name.setText(pickupItem.getConsigneeName());
            holder.tv_address.setText(pickupItem.getConsigneeAddressDetail());
            holder.tv_price.setText(pickupItem.getFormattedTotalFeeString());
            if (pickupItem.isWaybillCreated())
                holder.ll_content_container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundSuccess));

            holder.tv_pickup_item_code.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mPickupItemHistoryAdapterListener != null)
                        mPickupItemHistoryAdapterListener.onPickupItemCodeClicked(v, pickupItem);
                }
            });
            holder.tv_pickup_item_code.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (mPickupItemHistoryAdapterListener != null)
                    {
                        mPickupItemHistoryAdapterListener.onPickupItemCodeLongClicked(v, pickupItem);
                        return true;
                    }
                    return false;
                }
            });
            holder.tv_waybill_number.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mPickupItemHistoryAdapterListener != null)
                        mPickupItemHistoryAdapterListener.onWaybillNumberClicked(v, pickupItem);
                }
            });
            holder.tv_waybill_number.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (mPickupItemHistoryAdapterListener != null)
                    {
                        mPickupItemHistoryAdapterListener.onWaybillNumberLongClicked(v, pickupItem);
                        return true;
                    }
                    return false;
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

    public void setPickupItemHistoryAdapterListener(IPickupItemHistoryAdapterListener pickupItemHistoryAdapterListener)
    {
        mPickupItemHistoryAdapterListener = pickupItemHistoryAdapterListener;
    }

    private class ViewHolder
    {
        LinearLayout ll_content_container;
        TextView tv_pickup_item_code, tv_waybill_number, tv_receiver_name, tv_address, tv_price;
    }

    public interface IPickupItemHistoryAdapterListener
    {
        void onPickupItemCodeClicked(View v, PickupItem pickupItem);
        void onPickupItemCodeLongClicked(View v, PickupItem pickupItem);
        void onWaybillNumberClicked(View v, PickupItem pickupItem);
        void onWaybillNumberLongClicked(View v, PickupItem pickupItem);
    }
}
