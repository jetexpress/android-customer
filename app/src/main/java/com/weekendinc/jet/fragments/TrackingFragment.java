package com.weekendinc.jet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.weekendinc.jet.R;
import com.weekendinc.jet.TrackingResultActivity;
import com.weekendinc.jet.adapter.TrackingAdapter;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.base.BaseFragment;

public class TrackingFragment extends BaseFragment implements View.OnClickListener
{
    private TrackingAdapter mAdapter;
    private RecyclerView rcy_view;
    private LinearLayoutManager mLayoutManager;

    public TrackingFragment() {
        // Required empty public constructor
    }

    public static TrackingFragment newInstance() {
        return new TrackingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** LayoutManager needs to be initialized before set to RecyclerView to avoid IllegalArgumentException : LinearLayoutManager is already attached to a RecyclerView */
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);

        rcy_view = (RecyclerView) view.findViewById(R.id.rcy_view);
        rcy_view.setLayoutManager(mLayoutManager);
        mAdapter = new TrackingAdapter(getActivity(), this);
        rcy_view.setAdapter(mAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle(R.string.title_section_track);
    }

    @Override
    public void onArrowBackPressed()
    {
        super.onArrowBackPressed();
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_track)
        {
            Intent intent = new Intent(getActivity(), TrackingResultActivity.class);
            intent.putExtra(TrackingResultActivity.AWB_NUMBER_ARRAY_STRING_EXTRA, getAllWaybills());
            startActivity(intent);

        }
        else if (v.getId() == R.id.btn_add_waybill)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    rcy_view.scrollToPosition(mAdapter.getList().size());
                }
            }, 300);
        }
    }

    public String[] getAllWaybills() {
        String[] awbNumberStringArray = new String[mAdapter.getList().size()];
        awbNumberStringArray = mAdapter.getList().toArray(awbNumberStringArray);
        return awbNumberStringArray;
    }

//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if((event.getAction() == KeyEvent.ACTION_DOWN) || (keyCode == KeyEvent.KEYCODE_ENTER)){
//            onResume();
//        }
//    }
}
