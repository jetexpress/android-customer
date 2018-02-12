package com.weekendinc.jet.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekendinc.jet.R;
import com.weekendinc.jet.adapter.FragmentAdapter;

import coid.customer.pickupondemand.jet.base.BaseFragment;

public class MyShipmentFragment extends BaseFragment
{

    private static String DEFAULT_TAB_ARGS_PARAM = "defaultTabParam";
    public static int TRACKING_TAB = 0;
    public static int DELIVERED_TAB = 1;
    private int mDefaultTab;

    public MyShipmentFragment() {

    }

    public static MyShipmentFragment newInstance(int defaultTab) {
        MyShipmentFragment fragment = new MyShipmentFragment();
        Bundle args = new Bundle();
        args.putInt(DEFAULT_TAB_ARGS_PARAM, defaultTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(DEFAULT_TAB_ARGS_PARAM))
            mDefaultTab = getArguments().getInt(DEFAULT_TAB_ARGS_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_shipment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        OngoingShipmentFragment ongoingShipmentFragment = new OngoingShipmentFragment();
        DeliveredListFragment deliveredListFragment = new DeliveredListFragment();

        adapter.addFragment(ongoingShipmentFragment, getResources().getString(R.string.tracking));
        adapter.addFragment(deliveredListFragment, getResources().getString(R.string.delivered));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mDefaultTab, true);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(R.string.title_section_shipment);
    }
}
