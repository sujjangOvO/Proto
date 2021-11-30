package com.example.proto;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proto.Item;
import com.example.proto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookmarkDetail extends AppCompatActivity {
    private String title, addr , writer, user, type, name, body, score, waiting;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;

    TextView tv_title,main_content, storeName, storeAddr, storeType, storeScore, storeWaiting;
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_detail);
        setTitle("글 자세히보기");

        tv_title = (TextView) findViewById(R.id.title);
        main_content = (TextView) findViewById(R.id.main_content);
        storeName = (TextView) findViewById(R.id.storeName);
        storeAddr = (TextView) findViewById(R.id.storeAddr);
        storeType = (TextView) findViewById(R.id.storeType);
        storeScore = (TextView) findViewById(R.id.storeScore);
        storeWaiting = (TextView) findViewById(R.id.storeWaiting);
        image = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        addr = intent.getStringExtra("addr");
        user = intent.getStringExtra("user");
        writer = intent.getStringExtra("writer");

        type=intent.getStringExtra("type");
        name=intent.getStringExtra("name");
        body=intent.getStringExtra("body");
        score=intent.getStringExtra("score");
        waiting=intent.getStringExtra("waiting");
        // user_id=intent.getStringExtra("user_id");


        databaseReference = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();


        tv_title.setText(title);
        storeAddr.setText(addr);
        main_content.setText(body);
        storeName.setText(name);
        storeType.setText(type);
        storeScore.setText(score);
        storeWaiting.setText(waiting);


        // firebase storage에서 사진(사진 이름은 user id + title ) 불러오기
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference =  firebaseStorage.getReference();
        StorageReference pathReference = storageReference.child("photo");
        if(pathReference == null){
            Toast.makeText(BookmarkDetail.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            String storagePath = writer+title;
            Log.i("ListDetail ",storagePath);
            StorageReference imgReference = storageReference.child("photo/"+storagePath+".png");
            imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(BookmarkDetail.this).load(uri).into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BookmarkDetail.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}