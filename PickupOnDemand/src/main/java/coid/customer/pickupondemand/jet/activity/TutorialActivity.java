package coid.customer.pickupondemand.jet.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.fragment.PickupTutorialSlideFragment;

public class TutorialActivity extends AppIntro2
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addSlide(PickupTutorialSlideFragment.newInstance(R.layout.fragment_pickup_tutorial_slide_01, R.color.colorPrimary, R.id.rl_container));
        addSlide(PickupTutorialSlideFragment.newInstance(R.layout.fragment_pickup_tutorial_slide_02, R.color.colorPrimary, R.id.rl_container));
        addSlide(PickupTutorialSlideFragment.newInstance(R.layout.fragment_pickup_tutorial_slide_03, R.color.colorPrimary, R.id.rl_container));
        addSlide(PickupTutorialSlideFragment.newInstance(R.layout.fragment_pickup_tutorial_slide_04, R.color.colorPrimary, R.id.rl_container));
        addSlide(PickupTutorialSlideFragment.newInstance(R.layout.fragment_pickup_tutorial_slide_05, R.color.colorPrimary, R.id.rl_container));
        setColorTransitionsEnabled(true);
        updateFirstTimeOpenedStatus();
    }

    @Override
    protected void onDestroy()
    {
        updateFirstTimeOpenedStatus();
        super.onDestroy();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment)
    {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment)
    {
        super.onDonePressed(currentFragment);
        finish();
    }

    private void updateFirstTimeOpenedStatus()
    {
        SharedPreferences pref = getSharedPreferences(AppConfig.JET_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(AppConfig.FIRST_TIME_OPENED_PARAM_KEY, false);
        editor.apply();
    }
}