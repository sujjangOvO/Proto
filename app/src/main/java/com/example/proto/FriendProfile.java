package com.example.proto;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class FriendProfile extends AppCompatActivity {
    String friend_id;
    String str_id;
    TextView id, NickName;
    Button btn_del;

    private ArrayList<Item> itemlist = new ArrayList<>();
    private ItemAdapter adapter;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    private ListView lView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Intent intent=getIntent();
        friend_id=intent.getStringExtra("friend_id");
        str_id=intent.getStringExtra("user_id");
        id=(TextView) findViewById(R.id.name);
        btn_del=(Button)findViewById(R.id.delete);
        id.setText(friend_id);
        lView=findViewById(R.id.rv);

        // 친구 닉네임 가져오기
        NickName = (TextView) findViewById(R.id.NickName);
        databaseReference.child("userAccount").child(friend_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                String nickname = userAccount.getNickname();
                NickName.setText("닉네임 : "+nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // 친구삭제
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("userAccount").child(str_id).child("friends").child(friend_id).setValue(null);
                Intent intent1=getIntent();
                intent1.putExtra("del_id",friend_id);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });


        // 친구가 작성한 글 목록 adpater에 저장
        databaseReference.child("postList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    String user = item.getUser();
                    if (user.equals(friend_id)) {
                        itemlist.add(item);
                    }
                    ItemAdapter adapter = new ItemAdapter();
                    for (Item it : itemlist)
                        adapter.addItem(it);
                    lView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) adapter.getItem(position);
                Intent intent=new Intent(FriendProfile.this,BookmarkDetail.class);
                intent.putExtra("user_id",str_id);
                intent.putExtra("writer",item.getUser());
                intent.putExtra("name",item.getName());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("body",item.getBody());
                intent.putExtra("addr",item.getAddress());
                intent.putExtra("score",item.getScore());
                intent.putExtra("waiting",item.getWaiting());
                intent.putExtra("type",item.getType());

                startActivity(intent);

            }
        });
    } // onCrate end



    class ItemAdapter extends BaseAdapter {

        ArrayList<Item> items = new ArrayList<Item>();

        String user_id;

        public ItemAdapter(){

        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Item i){
            items.add(i);
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
            ItemListView view = null;
            if (convertView == null) {
                view = new ItemListView(getApplicationContext());
            } else {
                view = (ItemListView) convertView;
            }

            Item item = items.get(position);

            view.setUser(item.getUser());
            view.setTitle(item.getTitle());
            view.setAddr(item.getAddress());
            view.setImage();

            // 글목록 클릭이벤트
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(FriendProfile.this,BookmarkDetail.class);
                    intent.putExtra("user_id",str_id);
                    intent.putExtra("writer",item.getUser());
                    intent.putExtra("name",item.getName());
                    intent.putExtra("title",item.getTitle());
                    intent.putExtra("body",item.getBody());
                    intent.putExtra("addr",item.getAddress());
                    intent.putExtra("score",item.getScore());
                    intent.putExtra("waiting",item.getWaiting());
                    intent.putExtra("type",item.getType());

                    startActivity(intent);
                }
            });

            return view;
        }

    }
}