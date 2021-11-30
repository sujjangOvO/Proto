package com.example.proto;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemListView extends LinearLayout {
    TextView text_user;
    TextView text_title;
    TextView text_addr;
    ImageView listImage;



    public ItemListView(Context context) {
        super(context);
        init(context);
    }

    public ItemListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);
        text_user = (TextView) findViewById(R.id.listUser);
        text_addr = (TextView) findViewById(R.id.listAddr);
        text_title = (TextView) findViewById(R.id.listTitle);
        listImage = (ImageView) findViewById(R.id.listImage);
    }
    public void setUser(String user) {
        text_user.setText(user);
    }
    public void setTitle(String title) {
        text_title.setText(title);
    }
    public void setAddr(String addr){ text_addr.setText(addr);}
    public void setImage() {
        // firebase storage에서 사진(사진 이름은 user id + title ) 불러오기
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference =  firebaseStorage.getReference();
        StorageReference pathReference = storageReference.child("photo");
        if(pathReference == null){
            //Toast.makeText(ItemListView.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            String storagePath = text_user.getText().toString()+text_title.getText().toString();
            Log.i("listActivity",storagePath);
            StorageReference imgReference = storageReference.child("photo/"+storagePath+".png");
            imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ItemListView.this).load(uri).into(listImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(ItemListView.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
