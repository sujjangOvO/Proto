package com.example.proto;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SearchFragment extends Fragment {

    private TabLayout tabs;
    private ViewPager viewPager;
    private TabpageAdapter adapter;

    Fragment accountFragment;
    Fragment foodFragment;
    Fragment locationFragment;
    Fragment storeFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_search,container,false);

        accountFragment =new HaccountFragment();
        foodFragment=new HfoodFragment();
        locationFragment=new HlocationFragment();
        storeFragment=new HstoreFragment();

        viewPager=(ViewPager)v.findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabpageAdapter(getChildFragmentManager()));

        tabs=(TabLayout) v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position=tab.getPosition();

                Fragment selected=null;
                if(position==0){
                    selected= accountFragment;
                }else if(position==1){
                    selected=locationFragment;
                }else if(position==2){
                    selected=foodFragment;
                }else if(position==3) {
                    selected = storeFragment;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}