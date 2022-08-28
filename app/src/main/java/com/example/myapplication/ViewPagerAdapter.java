package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.ChoreographiesFragment;
import com.example.myapplication.fragments.PresetsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChoreographiesFragment();
            case 1:
                return new PresetsFragment();
            default:
                return new ChoreographiesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
