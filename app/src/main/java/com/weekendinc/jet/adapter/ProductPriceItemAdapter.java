package com.weekendinc.jet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weekendinc.jet.R;
import com.weekendinc.jet.model.pojo.PricingOption;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductPriceItemAdapter extends ArrayAdapter<PricingOption>
{
    public ProductPriceItemAdapter(Context context, List<PricingOption> pricingOptionList)
    {
        super(context, R.layout.custom_product_type_item_layout, pricingOptionList);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_product_type_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        PricingOption pricingOption = getItem(position);
        if (pricingOption != null)
        {
            String price = "Rp " +  formatIndonesianCurrency(pricingOption.getCalculationResult());

            holder.tv_product_name.setText(pricingOption.getProductName());
            holder.tv_product_price.setText(price);
            if(pricingOption.getProductCode().equals("CRG"))
            {

                holder.tv_product_info.setText(R.string.price_check_info_cargo);
            }else{
                holder.tv_product_info.setText("");
            }
        }

        return convertView;
    }

    public static String formatIndonesianCurrency(Object number){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat)nf;
        decimalFormat.applyPattern("###,###");
        return decimalFormat.format(number);
    }

    public void updateData(List<PricingOption> pricingOptionList)
    {
        clear();
        addAll(pricingOptionList);
        notifyDataSetChanged();
    }

    private static class ViewHolder
    {
        TextView tv_product_name;
        TextView tv_product_price;
        TextView tv_product_info;

        ViewHolder(View v)
        {
            tv_product_name = (TextView) v.findViewById(R.id.tv_product_name);
            tv_product_price = (TextView) v.findViewById(R.id.tv_product_price);
            tv_product_info = (TextView) v.findViewById(R.id.tv_product_info);
        }
    }
}
