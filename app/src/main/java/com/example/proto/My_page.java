package com.example.proto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class My_page extends AppCompatActivity {

    String str_id;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    TextView name;
    Button add; // 친구추가 버튼
    Button btn_post;
    private ActivityResultLauncher<Intent> resultLauncher;


    private ListView listView;
    private FriendsAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.set_option,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1: // 개인정보수정
                startActivity(new Intent(getApplicationContext(),my_setting.class));
                break;
            case R.id.menu2: // 로그아웃
                startActivity(new Intent(getApplicationContext(),Logout.class));
                break;
            case R.id.menu3: // 회원탈퇴
                startActivity(new Intent(getApplicationContext(),Daccount.class));
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        str_id = getIntent().getStringExtra("id");
        TextView search = findViewById(R.id.search);

        setTitle("마이페이지");

       //친구목록(리스트뷰) 불러오기
        listView = findViewById(R.id.listView);



        adapter = new FriendsAdapter(str_id);
        ArrayList<String> items=new ArrayList<>();
        items=getIntent().getStringArrayListExtra("items");
        final List<String> result = items.stream().distinct().collect(Collectors.toList());
        for (String str : result) {
            adapter.addItem(str);
        }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        //이름 텍스트 설정
        name = findViewById(R.id.name);
        name.setText(str_id);

        //검색버튼 눌렀을 때
        add = findViewById(R.id.add_friend);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("userAccount").child(search.getText().toString()).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value=snapshot.getValue(String.class);
                        if(value!=null){ //계정 존재
                            FriendsItem fr=new FriendsItem(search.getText().toString());
                            databaseReference.child("userAccount").child(str_id).child("friends").child(search.getText().toString()).setValue(fr);
                            Toast.makeText(My_page.this,"친구가 등록되었습니다",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            result.add(search.getText().toString());
                            adapter = new FriendsAdapter(str_id);
                            for (String str : result) {
                                adapter.addItem(str);
                            }
                            listView.setAdapter(adapter);

                        }else{
                            Toast.makeText(My_page.this,"친구 아이디를 확인하세요",Toast.LENGTH_SHORT).show();
                            //계정x
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                for (String str : items) {
//                    Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
//                }

            }
        });

        btn_post = (Button) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My_page.this,Posting.class);
                intent.putExtra("id",str_id);
                startActivity(intent);
            }
        });

    }
}