package coid.customer.pickupondemand.jet.fragment;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.BaseItemSelectDialogFragment;
import coid.customer.pickupondemand.jet.model.Product;

public class ProductItemSelectDialogFragment extends BaseItemSelectDialogFragment<Product>
{
    private static final String PRODUCT_ARRAY_LIST_ARGS_PARAM = "ProductArrayListParam";
    private List<Product> mProductList;

    public ProductItemSelectDialogFragment()
    {

    }

    public static ProductItemSelectDialogFragment newInstance(List<Product> productList)
    {
        ProductItemSelectDialogFragment dialog = new ProductItemSelectDialogFragment();
        Bundle args = new Bundle();
        ArrayList<Product> productArrayList = new ArrayList<>();
        productArrayList.addAll(productList);
        args.putParcelableArrayList(PRODUCT_ARRAY_LIST_ARGS_PARAM, productArrayList);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mProductList = getArguments().getParcelableArrayList(PRODUCT_ARRAY_LIST_ARGS_PARAM);
        }
    }

    @Override
    protected String getTitle()
    {
        Context context = JETApplication.getContext();
        return context.getString(R.string.pod_pickup_item_product);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateData(mProductList);
    }
}
