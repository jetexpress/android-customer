package coid.customer.pickupondemand.jet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.IBaseOverflowMenuListener;
import coid.customer.pickupondemand.jet.model.Pickup;

public class PickupAdapter extends ArrayAdapter<Pickup>
{
    private LayoutInflater mLayoutInflater;
    private IBaseOverflowMenuListener mOverflowMenuListener;

    public PickupAdapter(Context context)
    {
        this(context, null);
    }

    public PickupAdapter(Context context, IBaseOverflowMenuListener overflowMenuListener)
    {
        this(context, new ArrayList<Pickup>(), overflowMenuListener);
    }

    public PickupAdapter(Context context, List<Pickup> pickupList, IBaseOverflowMenuListener overflowMenuListener)
    {
        super(context, R.layout.list_item_current_pickup, pickupList);
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
            convertView = mLayoutInflater.inflate(R.layout.list_item_current_pickup, parent, false);
            holder.ll_content_container = convertView.findViewById(R.id.ll_content_container);
            holder.tv_pickup_number = (TextView) convertView.findViewById(R.id.tv_pickup_number);
            holder.tv_pickup_date = (TextView) convertView.findViewById(R.id.tv_pickup_date);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.img_menu = (ImageView) convertView.findViewById(R.id.img_menu);
            holder.progress_bar_status = (ProgressBar) convertView.findViewById(R.id.progress_bar_status);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Pickup pickup = getItem(position);
        if (pickup != null)
        {
            holder.tv_pickup_number.setText(pickup.getCode());
            holder.tv_pickup_date.setText(pickup.getFormattedCreatedDate());
            holder.tv_price.setText(pickup.getFormattedTotalFee());
            holder.tv_status.setText(pickup.getReadableStatus());
            if (mOverflowMenuListener != null)
            {
                holder.img_menu.setVisibility(View.VISIBLE);
                holder.img_menu.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mOverflowMenuListener.onMenuClicked(view, position);
                    }
                });
            }
            else
                holder.img_menu.setVisibility(View.GONE);

            holder.progress_bar_status.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setOverflowMenuListener(IBaseOverflowMenuListener baseOverflowMenuListener)
    {
        mOverflowMenuListener = baseOverflowMenuListener;
    }

    public void updateData(List<Pickup> pickupList, Boolean isReplace)
    {
        if (isReplace) clear();
        addAll(pickupList);
        notifyDataSetChanged();
    }

    public Pickup getItemByCode(String pickupCode)
    {
        for (int i = 0; i < getCount(); i++)
        {
            Pickup pickup = getItem(i);
            if (pickup != null)
                if (pickup.getCode().equals(pickupCode))
                    return pickup;
        }

        return null;
    }

    private class ViewHolder
    {
        LinearLayout ll_content_container;
        TextView tv_pickup_number,tv_pickup_date, tv_price, tv_status;
        ImageView img_menu;
        ProgressBar progress_bar_status;
    }

}
