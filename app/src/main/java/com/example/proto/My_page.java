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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
<<<<<<< HEAD
    Button post;
=======
    Button btn_post;
>>>>>>> 3d3bb76 (11/25 commit 이제 여기서부터 시작하면 됨니다)
    private ActivityResultLauncher<Intent> resultLauncher;


    private ListView listView;
    private FriendsAdapter adapter;
    String del_id=null;

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

<<<<<<< HEAD
=======

>>>>>>> 3d3bb76 (11/25 commit 이제 여기서부터 시작하면 됨니다)

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

        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                results -> {
                    if(results.getResultCode() == RESULT_OK) {
                        Intent intent = results.getData();
                        del_id=intent.getStringExtra("del_id");
//                        Toast.makeText(My_page.this,del_id,Toast.LENGTH_SHORT).show();
                        //            친구삭제처리
                        result.remove(del_id);
                        adapter = new FriendsAdapter(str_id);
                        for (String str : result) {
                            adapter.addItem(str);
                        }
                        listView.setAdapter(adapter);
                        del_id=null;
                    }
                }
        );

        //리스트뷰 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendsItem item = (FriendsItem) adapter.getItem(position);
//                Toast.makeText(getApplicationContext(), "선택 :"+item.getName(), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(My_page.this,FriendProfile.class);
                intent.putExtra("friend_id",item.getName());
                intent.putExtra("user_id",str_id);
                mStartForResult.launch(intent);
            }
        });

        //맛집추가액티비티
        post=findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My_page.this,Write.class);
                intent.putExtra("id",str_id);
                startActivity(intent);
            }
        });

    }

    class FriendsAdapter extends BaseAdapter {

        ArrayList<FriendsItem> items = new ArrayList<FriendsItem>();

        String user_id;

        public FriendsAdapter(String user_id){
            this.user_id=user_id;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(String str){
            FriendsItem item=new FriendsItem(str);
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 뷰 객체 재사용
            FriendListView view = null;
            if (convertView == null) {
                view = new FriendListView(getApplicationContext());
            } else {
                view = (FriendListView) convertView;
            }

            FriendsItem item = items.get(position);

            view.setName(item.getName());

            return view;
        }
    }


}