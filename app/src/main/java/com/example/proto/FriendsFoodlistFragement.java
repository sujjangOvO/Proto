package com.example.proto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FriendsFoodlistFragement extends Fragment {

   private ArrayList<FriendsFoodItem> arrayList=new ArrayList<>();
   private RecyclerView recyclerView;
   private FriendsFoodItemAdapter friendsFoodItemAdapter;

    private static final String[] ftype=new String[]{
            "양식","일식","중식","한식","기타"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void prepareData(){
        for(int i=0;i<4;i++){
            arrayList.add(new FriendsFoodItem(R.drawable.ic_launcher_background,"제목입니다.","주소입니다."));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_friends_foodlist,container,false);

        recyclerView=(RecyclerView) v.findViewById(R.id.rv2);
        recyclerView.setHasFixedSize(true);
        friendsFoodItemAdapter=new FriendsFoodItemAdapter(arrayList);

        //Spinner spinner=(Spinner)v.findViewById(R.id.division);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,ftype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        /*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 음식 종류를 선택했을 때, division 수행 코드 필요
                Toast.makeText(getActivity(),Integer.toString(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); */
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendsFoodItemAdapter);

        return v;
    }
}
