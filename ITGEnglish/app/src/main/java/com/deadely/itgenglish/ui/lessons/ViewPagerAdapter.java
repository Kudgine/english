package com.deadely.itgenglish.ui.lessons;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.deadely.itgenglish.ui.mapsfragments.MapCityFragment;
import com.deadely.itgenglish.ui.mapsfragments.MapCurortFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Activity activity;

    public ViewPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MapCityFragment();
        }
        return new MapCurortFragment();
    }

}
