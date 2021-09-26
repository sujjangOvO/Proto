package com.example.proto;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TabpageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment>fragmentList=new ArrayList<>();
    private ArrayList<String>title=new ArrayList<>();

    public TabpageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentList.add(new HaccountFragment());
        fragmentList.add(new HfoodFragment());
        fragmentList.add(new HlocationFragment());
        fragmentList.add(new HstoreFragment());

        // 탭 이름
        title.add("account");
        title.add("food");
        title.add("location");
        title.add("store");
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position){
        return title.get(position);
    }
    // 화면의 실제 Fragment를 반환
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
