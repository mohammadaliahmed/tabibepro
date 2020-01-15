package com.tabibe.app.adapter;

import com.tabibe.app.fragment.SiblingsInformationFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return SiblingsInformationFragment.newInstance();

//            case 1:
//                return MyInformationFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Mes proche et moi";

            case 1:
                return "Mon compte";

            default:
                return null;
        }

    }
}
