package com.example.proto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class HpostingFragment extends Fragment {

    private ArrayList<HpostingItem> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private HpostingAdapter hpostingAdapter;

    public HpostingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void prepareData(){
        for(int i=0;i<10;i++){
            arrayList.add(new HpostingItem("Title","location","Contents Contents Contents\nContents Contents Contents",R.drawable.ic_launcher_foreground));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_hposting, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.rv_hposting);
        recyclerView.setHasFixedSize(true);
        hpostingAdapter=new HpostingAdapter(arrayList);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hpostingAdapter);

        return v;
    }
}