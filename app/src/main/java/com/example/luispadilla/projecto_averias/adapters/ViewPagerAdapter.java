package com.example.luispadilla.projecto_averias.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luispadilla.projecto_averias.ui.FaultListFragment;
import com.example.luispadilla.projecto_averias.ui.FaultMapFragment;

import java.util.Arrays;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Fragment faultListFragment;
    Fragment faultMapFragment;
    List<Fragment> appFragments;
    List<String> titles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentsInit();
    }

    public void fragmentsInit(){
        titles = Arrays.asList("Fault Map", "Fault List");
        faultListFragment = new FaultListFragment();
        faultMapFragment = new FaultMapFragment();
        appFragments = Arrays.asList(faultMapFragment, faultListFragment);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment current = appFragments.get(0); // default fragment;
        switch(position){
            case 0:
                current = appFragments.get(0);
                break;
            case 1:
                current = appFragments.get(1);
                break;
        }
        return current;
    }

    @Override
    public int getCount() {
        return appFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = titles.get(0);
        switch(position){
            case 0:
                title = titles.get(0);
                break;
            case 1:
                title = titles.get(1);
                break;
        }
        return title;
    }
}
