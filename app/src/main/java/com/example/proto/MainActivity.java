package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button search;
    Button list;
    Button my_page;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private List<String> items = new ArrayList<>();



    private ArrayList<String> itemWestern= new ArrayList<>();
    private ArrayList<String> itemKorea= new ArrayList<>();
    private ArrayList<String> itemChina= new ArrayList<>();
    private ArrayList<String> itemJapan= new ArrayList<>();
    private ArrayList<String> itemSom= new ArrayList<>();

    private List<String> item_type=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search=(Button) findViewById(R.id.search);
        list=(Button) findViewById(R.id.list);
        my_page=(Button) findViewById(R.id.my_page);


        // 로그인 아이디 받기
        String id=getIntent().getStringExtra("id");


        databaseReference.child("userAccount").child(id).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    String str = fileSnapshot.child("name").getValue(String.class);
                    items.add(str);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        // 글 목록 버튼
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,listActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        // 글 검색 버튼
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, postSearchActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        // 마이페이지 버튼
        my_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, My_page.class);
                intent.putExtra("id",id);
                intent.putStringArrayListExtra("items", (ArrayList<String>) items);
                startActivity(intent);
            }
        });



    }
}