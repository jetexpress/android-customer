package coid.customer.pickupondemand.jet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.JETApplication;
import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.BaseItemSelectDialogFragment;
import coid.customer.pickupondemand.jet.model.PackagingItem;

public class PackagingItemSelectDialogFragment extends BaseItemSelectDialogFragment<PackagingItem>
{
    private static final String PACKAGING_ITEM_ARRAY_LIST_ARGS_PARAM = "PackagingItemArrayListParam";
    private List<PackagingItem> mPackagingItemList;

    public PackagingItemSelectDialogFragment()
    {

    }

    public static PackagingItemSelectDialogFragment newInstance(List<PackagingItem> packagingItemList)
    {
        PackagingItemSelectDialogFragment dialog = new PackagingItemSelectDialogFragment();
        Bundle args = new Bundle();
        ArrayList<PackagingItem> packagingItemArrayList = new ArrayList<>();
        packagingItemArrayList.addAll(packagingItemList);
        args.putParcelableArrayList(PACKAGING_ITEM_ARRAY_LIST_ARGS_PARAM, packagingItemArrayList);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mPackagingItemList = getArguments().getParcelableArrayList(PACKAGING_ITEM_ARRAY_LIST_ARGS_PARAM);
        }
    }

    @Override
    protected String getTitle()
    {
        Context context = JETApplication.getContext();
        return context.getString(R.string.pod_pickup_item_packaging);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateData(mPackagingItemList);
    }
}
