package com.example.luispadilla.projecto_averias.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    public static void addFragment (FragmentManager fm, Integer fragmentId, android.support.v4.app.Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(fragmentId, fragment);
        ft.commit();
    }

    public static void changeFragment (FragmentManager fm, Integer fragmentId, android.support.v4.app.Fragment fragment, Boolean toBackstack) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(fragmentId, fragment);
        if(toBackstack){
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public static void removeFragment (FragmentManager fm, android.support.v4.app.Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}
