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
import coid.customer.pickupondemand.jet.model.NotificationPayload;

public class NotificationAdapter extends ArrayAdapter<NotificationPayload>
{
    private IBaseOverflowMenuListener mOverflowMenuListener;

    public NotificationAdapter(Context context)
    {
        this(context, null);
    }

    public NotificationAdapter(Context context, IBaseOverflowMenuListener overflowMenuListener)
    {
        this(context, new ArrayList<NotificationPayload>(), overflowMenuListener);
    }

    public NotificationAdapter(Context context, List<NotificationPayload> notificationPayloadList, IBaseOverflowMenuListener overflowMenuListener)
    {
        super(context, R.layout.list_item_notification, notificationPayloadList);
        mOverflowMenuListener = overflowMenuListener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        NotificationPayload notificationPayload = getItem(position);
        if (notificationPayload != null)
        {
            holder.tv_code.setText(notificationPayload.getCode());
            holder.tv_status.setText(notificationPayload.getReadableStatus());

            if (notificationPayload.isNew())
                holder.tv_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_barcode, 0, R.drawable.ic_new, 0);
            else
                holder.tv_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_barcode, 0, 0, 0);

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
        }

        return convertView;
    }

    public long getLastItemId()
    {
        NotificationPayload lastNotificationPayloadItem = getItem(getCount() - 1);
        if (lastNotificationPayloadItem != null)
            return lastNotificationPayloadItem.getId();

        return 0;
    }

    public void updateData(List<NotificationPayload> notificationPayloadList, boolean isReplace)
    {
        if (isReplace) clear();
        addAll(notificationPayloadList);
        notifyDataSetChanged();
    }

    private static class ViewHolder
    {
        LinearLayout ll_content_container;
        TextView tv_code, tv_status;
        ImageView img_menu;

        ViewHolder(View v)
        {
            ll_content_container = v.findViewById(R.id.ll_content_container);
            tv_code = v.findViewById(R.id.tv_code);
            tv_status = v.findViewById(R.id.tv_status);
            img_menu = v.findViewById(R.id.img_menu);
        }
    }
}
