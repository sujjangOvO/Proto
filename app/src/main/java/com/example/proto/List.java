package com.example.proto;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    private ArrayList<Item> arrayList;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private FloatingActionButton btn_add;

    private static final String[] ftype=new String[]{
            "양식","일식","중식","한식","기타"
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        arrayList = new ArrayList<>();
        prepareData();

        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        itemAdapter=new ItemAdapter(arrayList);
        /* xml파일 받고 살리기(11.24)
        btn_add=(FloatingActionButton)findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Posting.class));
            }
        }); */

        Spinner spinner=(Spinner)findViewById(R.id.division);
        ArrayAdapter <String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,ftype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 음식 종류를 선택했을 때, division 수행 코드 필요
                Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);

    }

    private void prepareData() {
        for (int i = 0; i < 4; i++) {
//            arrayList.add(new Item(R.drawable.ic_launcher_background,"제목입니다.","주소입니다.","이름입니다."));
        }
    }

}