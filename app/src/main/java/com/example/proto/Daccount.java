package com.example.proto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Daccount extends AppCompatActivity {

    private FirebaseDatabase database=FirebaseDatabase.getInstance();;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    Button Delete;
    EditText ID, PW;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_account);

        Delete = (Button)findViewById(R.id.find_pw_button);
        ID = findViewById(R.id.EditText);
        PW = findViewById(R.id.PW);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strID=ID.getText().toString();
                String strPW=PW.getText().toString();

                if (strID.replace(" ", "").equals("") || //공백 체크
                        strPW.replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 확인해 주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("userAccount").child(strID).child("pw").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value=snapshot.getValue(String.class);
                            if(strPW.equals(value)){ //로그인 성공
                                databaseReference.child("userAccount").child(strID).removeValue();

                                Toast.makeText(getApplicationContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"비밀번호를 확인하세요",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //디비를 가져오던 중 에러 발생 시...
                        }
                    });

                }
            }
        });
    }
}
