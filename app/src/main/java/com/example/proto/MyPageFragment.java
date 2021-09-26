package com.example.proto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private ArrayList<FriendsItem> arrayList = new ArrayList<>();
    private ListView listView;
    private FriendsAdapter adapter;


    public void friends(){
        StringBuilder rev = new StringBuilder();

        for(int i=0; i<10; i++){
            String str = "친구";
            arrayList.add(new FriendsItem(str+(i+1)));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false);

    }
}