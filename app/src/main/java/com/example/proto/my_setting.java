package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class my_setting extends AppCompatActivity {

    EditText setName, setPhone, setPw, checkPw;
    Button setBtn, backBtn;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String cur_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);

        Intent intent = getIntent();
        String str_id = intent.getStringExtra("id"); // 현재 id

        setName = (EditText) findViewById(R.id.setName);
        setPhone = (EditText) findViewById(R.id.setPhone);
        setPw = (EditText) findViewById(R.id.setPw);
        checkPw = (EditText) findViewById(R.id.checkPw);

        setBtn = (Button) findViewById(R.id.setBtn);
        backBtn = (Button) findViewById(R.id.backBtn);


        // 기존 pw 받아오기
        databaseReference = FirebaseDatabase.getInstance().getReference(); // realtime 파베에서 정보 가져오기
        databaseReference.child("userAccount").child(str_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                cur_pw = userAccount.getPw();
                Log.i("my_setting",cur_pw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // 저장하기 버튼 리스너
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!nullCheck(setName) || !nullCheck(setPhone) || !nullCheck(setPw) || !nullCheck(checkPw)){
                    Toast.makeText(getApplicationContext(), "빈칸을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String setname = setName.getText().toString();
                    String setphone = setPhone.getText().toString();
                    String setpw = setPw.getText().toString();
                    String checkpw = checkPw.getText().toString();
                    if(!pw_check(cur_pw,checkpw)){ // 기존 비밀번호와 기존 pw 비교.
                        Toast.makeText(getApplicationContext(),"기존 비밀번호를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else{ // 정상반영
                        databaseReference.child("userAccount").child(str_id).child("nickname").setValue(setname);
                        databaseReference.child("userAccount").child(str_id).child("phone").setValue(setphone);
                        databaseReference.child("userAccount").child(str_id).child("pw").setValue(setpw);

                        Toast.makeText(getApplicationContext(),"회원 정보를 성공적으로 변경했습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        // 삭제하기 버튼 리스너
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(my_setting.this);

                dlg.setTitle("확인메시지");
                dlg.setMessage("변경 내용을 삭제하시겠습니까?");

                dlg.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                dlg.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dlg.show();
            }
        });

    } // onCreate

    public boolean pw_check(String setPw,String checkPw){
        if(setPw.equals(checkPw)) return true;
        else return false;
    }

    public boolean nullCheck(EditText editText){
        if(editText.getText().toString().replace(" ", "").equals(""))
            return false;
        else return true;
    }
}