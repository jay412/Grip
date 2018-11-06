package com.herokuapp.jordan_chau.grip.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.herokuapp.jordan_chau.grip.fragments.HistoryFragment;
import com.herokuapp.jordan_chau.grip.fragments.NewBillFragment;
import com.herokuapp.jordan_chau.grip.fragments.SettingsFragment;

public class BottomNavBarAdapter extends SmartFragmentStatePagerAdapter {
    //private final List<Fragment> fragments = new ArrayList<>();
    private static int NUM_ITEMS = 3;

    public BottomNavBarAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show HistoryFragment
                return new HistoryFragment();
            case 1: // Fragment # 1 - This will show NewBillFragment
                return new NewBillFragment();
            case 2: // Fragment # 2 - This will show SettingsFragment
                return new SettingsFragment();
            default:
                throw new UnsupportedOperationException("Navigation option not supported");
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
