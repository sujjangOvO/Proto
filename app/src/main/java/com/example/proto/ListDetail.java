package com.example.proto;

import android.content.DialogInterface;
import com.bumptech.glide.Glide;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListDetail extends AppCompatActivity {
    private String title, addr, user,type,name,body,score,waiting, writer;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;


    TextView tv_title,main_content, storeName, storeAddr, storeType, storeScore, storeWaiting;
    ImageView imgView;
    String storagePath;
    Button btn_bookmark;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listdetail);
        setTitle("글 자세히보기");

        tv_title = (TextView) findViewById(R.id.title);
        main_content = (TextView) findViewById(R.id.main_content);
        storeName = (TextView) findViewById(R.id.storeName);
        storeAddr = (TextView) findViewById(R.id.storeAddr);
        storeType = (TextView) findViewById(R.id.storeType);
        storeScore = (TextView) findViewById(R.id.storeScore);
        storeWaiting = (TextView) findViewById(R.id.storeWaiting);
        imgView = (ImageView) findViewById(R.id.imgView);
        btn_bookmark = (Button) findViewById(R.id.btn_bookmark);


        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        addr = intent.getStringExtra("addr");
        user = intent.getStringExtra("user_id");
        type=intent.getStringExtra("type");
        name=intent.getStringExtra("name");
        body=intent.getStringExtra("body");
        score=intent.getStringExtra("score");
        waiting=intent.getStringExtra("waiting");
        writer = intent.getStringExtra("writer"); // 글쓴 사람 id를 받아와야함

        // firebase storage에서 사진(사진 이름은 user id + title ) 불러오기
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference =  firebaseStorage.getReference();
        StorageReference pathReference = storageReference.child("photo");
        if(pathReference == null){
            Toast.makeText(ListDetail.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            storagePath = writer+title;
            Log.i("ListDetail ",storagePath);
            StorageReference imgReference = storageReference.child("photo/"+storagePath+".png");
            imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ListDetail.this).load(uri).into(imgView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ListDetail.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }


        tv_title.setText(title);
        storeAddr.setText(addr);
        main_content.setText(body);
        storeName.setText(name);
        storeType.setText(type);
        storeScore.setText(score);
        storeWaiting.setText(waiting);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        // 북마크 버튼
        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item it = new Item(writer,name,title,body,addr,type,score,waiting);
                databaseReference.child("bookmark").child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int tmp=0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Item item = dataSnapshot.getValue(Item.class);
                            if(it.getTitle().equals(item.getTitle())&&it.getBody().equals(item.getBody())){
                                tmp=1;
                                break;
                            }
                        }
                        if(tmp==0){
                            databaseReference.child("bookmark").child(user).push().setValue(it);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(getApplicationContext(),"북마크 완료",Toast.LENGTH_SHORT).show();
                //if(databaseReference.child("bookmark").child(user).)
            }
        });



        // 옵션메뉴 리스너
        findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu=new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.menu1) {  //게시물 수정
                            if(user.equals(writer)){
                                Intent intent2 = new Intent(ListDetail.this, EditPosting.class);
                                intent2.putExtra("user_id", user);
                                intent2.putExtra("name", name);
                                intent2.putExtra("title", title);
                                intent2.putExtra("body", body);
                                intent2.putExtra("addr", addr);
                                intent2.putExtra("score", score);
                                intent2.putExtra("waiting", waiting);
                                intent2.putExtra("type", type);
                                intent2.putExtra("writer", writer);
                                startActivity(intent2);
                            }
                            else{
                                Toast.makeText(ListDetail.this, "글 작성자만 수정할 수 있습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else if(menuItem.getItemId()==R.id.menu2) { // 게시글 삭제
                            if(user.equals(writer)){
                                AlertDialog.Builder dlg = new AlertDialog.Builder(ListDetail.this);
                                dlg.setTitle("삭제 확인 메시지");
                                dlg.setMessage("게시글을 삭제하시겠습니까?");

                                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("postList").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    String snapshotKey = dataSnapshot.getKey();
                                                    Item item = dataSnapshot.getValue(Item.class);
                                                    String item_title = item.getTitle();
                                                    String item_user = item.getUser();


                                                    if (title.equals(item_title)) {
                                                        if (user.equals(item_user)) {
                                                            databaseReference.child("postList").child(snapshotKey).removeValue();
                                                        }
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        StorageReference reference = FirebaseStorage.getInstance().getReference();
                                        StorageReference desertRef = reference.child("photo/" + storagePath + ".png");
                                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                        Toast.makeText(ListDetail.this, "게시글을 성공적으로 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });

                                dlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        onBackPressed();
                                    }
                                });

                                dlg.show();
                            }
                            else{
                                Toast.makeText(ListDetail.this, "글 작성자만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
    }
}