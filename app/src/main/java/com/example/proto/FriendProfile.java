package com.example.proto;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendProfile extends AppCompatActivity {
    String str_id;
    String str_myid;
    TextView id, NickName;
    Button btn_del;


    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Intent intent=getIntent();
        str_id=intent.getStringExtra("friend_id");
        str_myid=intent.getStringExtra("user_id");
        id=(TextView) findViewById(R.id.name);
        btn_del=(Button)findViewById(R.id.delete);
        id.setText(str_id);


        NickName = (TextView) findViewById(R.id.NickName);
        NickName = (TextView) findViewById(R.id.NickName);
        databaseReference.child("userAccount").child(str_id).addValueEventListener(new ValueEventListener() {
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


        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              databaseReference.child("userAccount").child(str_myid).child("friends").child(str_id).setValue(null);
              Intent intent1=getIntent();
              intent1.putExtra("del_id",str_id);
              setResult(RESULT_OK,intent1);
              finish();
            }
        });





    }
}