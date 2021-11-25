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
import java.util.List;

public class HlocationFragment extends Fragment {

    private ArrayList<HlocationItem> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private HlocationAdapter hlocationAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void prepareData(){
        for(int i=0;i<10;i++){
            arrayList.add(new HlocationItem("Store name","Store location",R.drawable.location));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_hlocation,container,false);
        recyclerView=(RecyclerView) v.findViewById(R.id.rv_hlocation);
        recyclerView.setHasFixedSize(true);
        hlocationAdapter=new HlocationAdapter(arrayList);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hlocationAdapter);

        return v;
    }
}