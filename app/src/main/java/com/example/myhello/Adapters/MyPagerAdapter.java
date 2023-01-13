package com.example.myhello.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myhello.Fragments.SamplesFragment;
import com.example.myhello.Fragments.UpLoadImgFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public MyPagerAdapter(FragmentManager fm, int numOfTabs2) {
        super(fm);
        this.numOfTabs = numOfTabs2;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return new UpLoadImgFragment();
        }
        /*if (position != 1) {
            return null;
        }*/
        return new SamplesFragment();
    }

    public int getCount() {
        return this.numOfTabs;
    }
}
