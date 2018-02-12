package coid.customer.pickupondemand.jet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.model.PaymentMethod;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethod>
{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Integer mCurrentSelectedPosition;

    public PaymentMethodAdapter(Context context, List<PaymentMethod> paymentMethodList)
    {
        super(context, R.layout.list_item_payment_method, paymentMethodList);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mCurrentSelectedPosition = -1;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.list_item_payment_method, parent, false);
            holder = new ViewHolder();
            holder.ll_payment_method_container = (LinearLayout) convertView.findViewById(R.id.ll_payment_method_container);
            holder.img_payment_method = (ImageView) convertView.findViewById(R.id.img_payment_method);
            holder.tv_payment_method_name = (TextView) convertView.findViewById(R.id.tv_payment_method_name);
            holder.tv_payment_method_description = (TextView) convertView.findViewById(R.id.tv_payment_method_description);
            holder.radio_button_payment_method = (RadioButton) convertView.findViewById(R.id.radio_button_payment_method);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        PaymentMethod paymentMethod = getItem(position);
        if (paymentMethod != null)
        {
            holder.img_payment_method.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_wallet));
            holder.tv_payment_method_name.setText(paymentMethod.getName());
            holder.tv_payment_method_description.setText(paymentMethod.getDescription());

            holder.ll_payment_method_container.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mCurrentSelectedPosition = position;
                    notifyDataSetChanged();
                }
            });
            holder.radio_button_payment_method.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mCurrentSelectedPosition = position;
                    notifyDataSetChanged();
                }
            });
            if (position == mCurrentSelectedPosition)
                holder.radio_button_payment_method.setChecked(true);
            else
                holder.radio_button_payment_method.setChecked(false);
        }

        return convertView;
    }

    public void updateData(List<PaymentMethod> paymentMethodList)
    {
        clear();
        addAll(paymentMethodList);
        notifyDataSetChanged();
    }

    public Boolean isChecked()
    {
        return mCurrentSelectedPosition > -1;
    }

    public PaymentMethod getCheckedPaymentMethod()
    {
        if (isChecked())
            return getItem(mCurrentSelectedPosition);
        return null;
    }

    private class ViewHolder
    {
        LinearLayout ll_payment_method_container;
        ImageView img_payment_method;
        TextView tv_payment_method_name, tv_payment_method_description;
        RadioButton radio_button_payment_method;
    }
}
