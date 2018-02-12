package coid.customer.pickupondemand.jet.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.BaseDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourierOrderLoadingDialogFragment extends BaseDialogFragment
{
    private TextView tv_back_to_pickup_list;

    public CourierOrderLoadingDialogFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.CourierOrderLoadingDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_courier_order_loading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setEvent();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog d = getDialog();
        if (d != null && d.getWindow() != null)
        {
            d.getWindow().setGravity(Gravity.CENTER);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
        }
    }

    private void setView()
    {
        tv_back_to_pickup_list = (TextView) findViewById(R.id.tv_back_to_pickup_list);
    }

    private void setEvent()
    {
        tv_back_to_pickup_list.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
                getNavigator().popResumeToDefaultFragment();
            }
        });
    }
}
