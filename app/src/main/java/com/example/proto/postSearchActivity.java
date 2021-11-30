package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class postSearchActivity extends AppCompatActivity {

    Button btn_search;
    TextInputEditText ptitle;
    String str_id, str_title;
    private String snapshotKey;
    private ListView lView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Item> itemlist = new ArrayList<>();
    private ItemAdapter adapter;
    String writer, name, title, body, addr, score, waiting ,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);
        setTitle("게시글 검색");

        databaseReference= FirebaseDatabase.getInstance().getReference();
        database=FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        str_id = intent.getStringExtra("id");

        ptitle=(TextInputEditText)findViewById(R.id.ptitle);
        lView=findViewById(R.id.rv);


        // 검색버튼
        btn_search=findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemlist.clear();
                if (ptitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
//                Toast.makeText(getApplicationContext(),"검색어는"+ptitle.getText().toString(),Toast.LENGTH_SHORT).show();
                    databaseReference.child("postList").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Item item = dataSnapshot.getValue(Item.class);
                                String getTitle = item.getTitle();
                                if (getTitle.contains(ptitle.getText().toString())) {
                                    itemlist.add(item);
                                    title= getTitle;
                                    writer = item.getUser();
                                    waiting = item.getWaiting();
                                    name = item.getName();
                                    body = item.getBody();
                                    addr = item.getAddress();
                                    score = item.getScore();
                                    type = item.getType();
                                }
                                ItemAdapter adapter = new ItemAdapter();
                                for (Item it : itemlist) {
                                    adapter.addItem(it);
                                    adapter.notifyDataSetChanged();
                                }
                                lView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        // 리스트뷰 리스너
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) adapter.getItem(position);
                Intent intent=new Intent(postSearchActivity.this,ListDetail.class);
                intent.putExtra("user_id",str_id);
                intent.putExtra("name",item.getName());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("body",item.getBody());
                intent.putExtra("addr",item.getAddress());
                intent.putExtra("score",item.getScore());
                intent.putExtra("waiting",item.getWaiting());
                intent.putExtra("type",item.getType());
                intent.putExtra("writer",item.getUser());
                startActivity(intent);

            }
        });

    }

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

            //글목록 클릭이벤트
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(postSearchActivity.this,ListDetail.class);
                    intent.putExtra("user_id",str_id);
                    intent.putExtra("name",item.getName());
                    intent.putExtra("title",item.getTitle());
                    intent.putExtra("body",item.getBody());
                    intent.putExtra("addr",item.getAddress());
                    intent.putExtra("score",item.getScore());
                    intent.putExtra("waiting",item.getWaiting());
                    intent.putExtra("type",item.getType());
                    intent.putExtra("writer",item.getUser());
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}