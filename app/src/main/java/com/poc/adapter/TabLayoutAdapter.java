package com.poc.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.poc.activity.MainActivity;
import com.poc.fragment.CallsFragment;
import com.poc.fragment.ContactsFragment;
import com.poc.fragment.MessagesFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private Activity activity;
    private int tabCount;

    public TabLayoutAdapter(Activity activity, FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.activity = activity;
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ContactsFragment();
            case 1:
                return new CallsFragment();
            case 2:
                return new MessagesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
