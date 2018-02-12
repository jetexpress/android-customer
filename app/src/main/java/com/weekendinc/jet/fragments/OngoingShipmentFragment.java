package com.weekendinc.jet.fragments;

public class OngoingShipmentFragment extends MyShipmentTabFragment
{
    @Override
    protected boolean isTrackFinished()
    {
        return false;
    }
}