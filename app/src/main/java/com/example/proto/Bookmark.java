package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bookmark extends AppCompatActivity {

    private List<Item> bookmarkList=new ArrayList<Item>();
    private List<String> bookList=new ArrayList<String>();
    private String user_id;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private ListView book_lv;
    Button btn_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setTitle("북마크 목록");
        user_id=getIntent().getStringExtra("id");


        bookmarkList= (List<Item>) getIntent().getSerializableExtra("bookmarkList");
        bookList=getIntent().getStringArrayListExtra("bookList");
//        for(Item it:bookmarkList){
//            Toast.makeText(getApplicationContext(),it.getName(),Toast.LENGTH_SHORT).show();
//        }

        book_lv=findViewById(R.id.book_rv);
        ItemAdapter itemAdapter=new ItemAdapter();
        for(Item it:bookmarkList)
            itemAdapter.addItem(it);
        book_lv.setAdapter(itemAdapter);


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
                    Intent intent=new Intent(Bookmark.this,BookmarkDetail.class);
                    intent.putExtra("user_id",user_id);
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