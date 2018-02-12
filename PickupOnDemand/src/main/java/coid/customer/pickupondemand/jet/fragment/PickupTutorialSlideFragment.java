package coid.customer.pickupondemand.jet.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

public class PickupTutorialSlideFragment extends Fragment implements ISlideBackgroundColorHolder
{
    private static final String LAYOUT_RES_ID_ARGS_PARAM = "layoutResId";
    private static final String COLOR_RES_ID_ARGS_PARAM = "colorResId";
    private static final String CONTAINER_ID_ARGS_PARAM = "containerId";
    private int mLayoutResId;
    private int mColorResId;
    private int mLayoutContainerId;

    public static PickupTutorialSlideFragment newInstance(int layoutResId) {
        PickupTutorialSlideFragment fragment = new PickupTutorialSlideFragment();

        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES_ID_ARGS_PARAM, layoutResId);
        fragment.setArguments(args);

        return fragment;
    }

    public static PickupTutorialSlideFragment newInstance(int layoutResId, int colorResId, int layoutContainerId) {
        PickupTutorialSlideFragment fragment = new PickupTutorialSlideFragment();

        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES_ID_ARGS_PARAM, layoutResId);
        args.putInt(COLOR_RES_ID_ARGS_PARAM, colorResId);
        args.putInt(CONTAINER_ID_ARGS_PARAM, layoutContainerId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(LAYOUT_RES_ID_ARGS_PARAM))
                mLayoutResId = getArguments().getInt(LAYOUT_RES_ID_ARGS_PARAM);
            if (getArguments().containsKey(COLOR_RES_ID_ARGS_PARAM))
                mColorResId = getArguments().getInt(COLOR_RES_ID_ARGS_PARAM);
            if (getArguments().containsKey(CONTAINER_ID_ARGS_PARAM))
                mLayoutContainerId = getArguments().getInt(CONTAINER_ID_ARGS_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(mLayoutResId, container, false);
    }

    @Override
    public int getDefaultBackgroundColor()
    {
        if (mColorResId > 0)
            return ContextCompat.getColor(getContext(), mColorResId);
        else
            return mColorResId;
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor)
    {
        if (getView() != null)
        {
            View v = getView().findViewById(mLayoutContainerId);
            v.setBackgroundColor(ContextCompat.getColor(getContext(), mColorResId));
        }
    }
}
