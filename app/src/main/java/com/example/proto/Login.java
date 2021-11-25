package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button login, join;
    private EditText ID, PW;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private TextView search_id,search_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.find_pw_button);
        join = findViewById(R.id.join);
        ID = findViewById(R.id.EditText);
        PW = findViewById(R.id.PW);
        search_id=findViewById(R.id.search_id);
        search_pw=findViewById(R.id.search_pw);

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String strID=ID.getText().toString();
                String strPW=PW.getText().toString();

                if (strID.replace(" ", "").equals("") || //공백 체크
                        strPW.replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 확인해 주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    login(strID,strPW);
                }
            }
        }); // 로그인 리스너

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Join.class);
                startActivity(intent);
            }
        }); // 회원가입 리스너

        search_id.setOnClickListener(new View.OnClickListener() {
             @Override
               public void onClick(View v) {
                    Intent intent = new Intent(Login.this,SearchId.class);
                    startActivity(intent);
            }
        }); // 아이디 찾기 리스너

        search_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SearchPw.class);
                startActivity(intent);
            }
        }); // 비밀번호찾기 리스너

    } // oncreate

    public void login(String id,String pw){
        databaseReference.child("userAccount").child(id).child("pw").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(pw.equals(value)){ //로그인 성공
                    Intent intent = new Intent(Login.this, MainActivity.class); //현재, 이동할 화면
                    intent.putExtra("id",ID.getText().toString());//사용자 아이디 전달
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"아이디 혹은 비밀번호를 확인하세요",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오던 중 에러 발생 시...
            }
        });
    }

} // main