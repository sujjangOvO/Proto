package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchPw extends AppCompatActivity {

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    EditText ID_text;
    Button find_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pw);

        ID_text=(EditText)findViewById(R.id.EditText);
        find_button=findViewById(R.id.find_pw_button);

        find_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String strID=ID_text.getText().toString();
                databaseReference.child("userAccount").child(strID).child("pw").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value=snapshot.getValue(String.class);
                        if(value!=null){
                            Toast.makeText(getApplicationContext(),"비밀번호는 "+value+"입니다",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"ID를 확인해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}