package com.example.proto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class listActivity extends AppCompatActivity {

    private ArrayList<Item> itemWestern= new ArrayList<>();
    private ArrayList<Item> itemKorea= new ArrayList<>();
    private ArrayList<Item> itemChina= new ArrayList<>();
    private ArrayList<Item> itemJapan= new ArrayList<>();
    private ArrayList<Item> itemSom= new ArrayList<>();

    private ItemAdapter itemAdapter;
    private FloatingActionButton btn_add;
    private String str_id;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    private ListView lView;
    private ItemAdapter adapter;
    private Uri URI;

    private String title,body,name,type,score, addr, user,waiting;

    private static final String[] ftype = new String[]{
            "양식", "일식", "중식", "한식", "기타"
    };

    public void setter_uri(Uri uri){
        URI = uri;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("글 목록");

        Intent intent = getIntent();
        str_id = intent.getStringExtra("id"); // 포스팅 눌렀을 때 넘겨줄 id

        databaseReference= FirebaseDatabase.getInstance().getReference();
        database=FirebaseDatabase.getInstance();

        lView=findViewById(R.id.rv);

        // 포스팅 + 버튼
        btn_add=(FloatingActionButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(listActivity.this,Posting.class);
                intent.putExtra("id",str_id);
                startActivity(intent);
            }
        });

//        itemKorea= intent.getStringArrayListExtra("itemKorea");
//        itemJapan= intent.getStringArrayListExtra("itemJapan");
//        itemChina= intent.getStringArrayListExtra("itemChina");
//        itemWestern= intent.getStringArrayListExtra("itemWestern");
//        itemSom= intent.getStringArrayListExtra("itemSom");

//        for(String item:itemJapan)
//            Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();


        databaseReference.child("postList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    //Log.i("Item",item.getAddress());
                    //Log.i("Item",item.getTitle());
                    //Log.i("Item",item.getUser());
                    String type=item.getType();
                    if(type.equals("기타")){
                        itemSom.add(item);
                    }else if(type.equals("한식")){
                        itemKorea.add(item);
                    }else if(type.equals("중식")){
                        itemChina.add(item);
                    }else if(type.equals("양식")){
                        itemWestern.add(item);
                        ItemAdapter adapter=new ItemAdapter();
                        for(Item it:itemWestern)
                            adapter.addItem(it);
                        lView.setAdapter(adapter);
                    }else if(type.equals("일식")){
                        itemJapan.add(item);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        final ItemAdapter adapter=new ItemAdapter();
//        set_list(itemJapan,adapter);

        Spinner spinner = (Spinner) findViewById(R.id.division);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(listActivity.this, android.R.layout.simple_spinner_item, ftype);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapt);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ItemAdapter adapter=new ItemAdapter();
                if (ftype[i].equals("일식")) {
                    for(Item it:itemJapan)
                        adapter.addItem(it);
                    lView.setAdapter(adapter);
                }else if(ftype[i].equals("양식")){
                    for(Item it:itemWestern)
                        adapter.addItem(it);
                    lView.setAdapter(adapter);
                }else if(ftype[i].equals("중식")){
                    for(Item it:itemChina)
                        adapter.addItem(it);
                    lView.setAdapter(adapter);
                }else if(ftype[i].equals("한식")){
                    for(Item it:itemKorea)
                        adapter.addItem(it);
                    lView.setAdapter(adapter);
                }else if(ftype[i].equals("기타")){
                    for(Item it:itemSom)
                        adapter.addItem(it);
                    lView.setAdapter(adapter);
                }


            }
            //
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) adapter.getItem(position);
//                Toast.makeText(getApplicationContext(), "선택 :"+item.getName(), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(listActivity.this,ListDetail.class);
                intent.putExtra("user_id",str_id);
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

            // 리스트에 사진 뜨는거 오류나서 주석
            /*
            String filePath = item.getUser()+item.getTitle();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference pathReference = storageReference.child("photo/"+filePath+".png");
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    setter_uri(uri);
                }
            }).
            addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            Glide.with(listActivity.this).load(URI).into(view.listImage); */

            //글목록 클릭이벤트
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(listActivity.this,ListDetail.class);
                    intent.putExtra("user_id",str_id);
                    intent.putExtra("name",item.getName());
                    intent.putExtra("title",item.getTitle());
                    intent.putExtra("body",item.getBody());
                    intent.putExtra("addr",item.getAddress());
                    intent.putExtra("score",item.getScore());
                    intent.putExtra("waiting",item.getWaiting());
                    intent.putExtra("type",item.getType());
                    intent.putExtra("writer",item.getUser()); // 글쓴사람 아이디를 넘겨줘야함!!
                    startActivity(intent);
                }
            });

            return view;
        }
    }



}


