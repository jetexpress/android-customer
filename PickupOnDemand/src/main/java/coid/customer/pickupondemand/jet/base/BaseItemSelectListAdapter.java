package coid.customer.pickupondemand.jet.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import coid.customer.pickupondemand.jet.R;


public class BaseItemSelectListAdapter<T extends IBaseItemSelectModel>
        extends ArrayAdapter<T>
{
    private LayoutInflater mLayoutInflater;

    public BaseItemSelectListAdapter(Context context, List<T> list)
    {
        super(context, R.layout.base_list_item_select_layout, list);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.base_list_item_select_layout, parent, false);
            holder = new ViewHolder();
            holder.tv_item_description = (TextView) convertView.findViewById(R.id.tv_item_description);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        T model = getItem(position);

        if (model != null)
            holder.tv_item_description.setText(model.getItemSelectDescription());

        return convertView;
    }

    private class ViewHolder
    {
        TextView tv_item_description;
    }

    public void updateData(List<T> dataList)
    {
        clear();
        addAll(dataList);
        notifyDataSetChanged();
    }
}
