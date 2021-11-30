package com.example.proto;

import android.app.ActionBar;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class SearchFragment extends Fragment { // 사용X

    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getFragmentManager() );

    Fragment accountFragment;
    Fragment locationFragment;
    Fragment postingFragment;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_search,container,false);

        accountFragment =new HaccountFragment();
        locationFragment=new HlocationFragment();
        postingFragment=new HpostingFragment();

        viewPager=(ViewPager)v.findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        // 프래그먼트
        viewPagerAdapter.AddFragment(new HaccountFragment(),"ACCOUNT");
        viewPagerAdapter.AddFragment(new HlocationFragment(),"LOCATION");
        viewPagerAdapter.AddFragment(new HpostingFragment(),"POSTING");

        tabs=(TabLayout) v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                int position=tab.getPosition();
//
//                Fragment selected=null;
//                if(position==0){
//                    selected= accountFragment;
//                }else if(position==1){
//                    selected=locationFragment;
//                }else if(position==2){
//                    selected = postingFragment;
//                }
                switch(tab.getPosition()){
                    case 0:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 1:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    default:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return v;
    }
}