package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search=findViewById(R.id.search);
        list=findViewById(R.id.list);
        my_page=findViewById(R.id.my_page);


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
                startActivity(new Intent(MainActivity.this, List.class));
            }
        });

        // 글 검색 버튼
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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