package com.example.proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Join extends AppCompatActivity{

    private static final String TAG="JoinActivity"; // Tag
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    Button id_check, end;
    EditText ID, PW, NickName, Phone;

    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        id_check = (Button)findViewById(R.id.id_check);
        end = (Button)findViewById(R.id.end);
        ID = (EditText)findViewById(R.id.EditText);
        PW = (EditText)findViewById(R.id.PW);
        NickName = (EditText)findViewById(R.id.NickName);
        Phone=(EditText)findViewById(R.id.Phone);



        //스피너
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arr, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); //어댑터에 연결


        //아이디 중복 확인
        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_duplicate_check()==false){
                    Toast.makeText(getApplicationContext(),"이미 존재하는 ID입니다",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"사용 가능한 ID입니다",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //완료 버튼 눌렀을 때
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nullCheck(ID) || !nullCheck(PW) || !nullCheck(NickName)) {
                    Toast.makeText(getApplicationContext(), "빈칸을 확인해 주세요", Toast.LENGTH_SHORT).show();
                } else if(id_duplicate_check()==false){
                    Toast.makeText(getApplicationContext(),"이미 존재하는 ID입니다",Toast.LENGTH_SHORT).show();
                }else{
                    signUp(); // signup success
                    Intent intent = new Intent(Join.this, Login.class); //현재, 이동할 화면
                    startActivity(intent);
                }
            }
        });



    }

    public boolean nullCheck(EditText editText){
        if(editText.getText().toString().replace(" ", "").equals(""))
            return false;
        else return true;
    }

    public boolean id_duplicate_check(){
        databaseReference.child("userAccount").child(ID.getText().toString()).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(value!=null){ //이미 존재
                    check=false;
                }else{
                    check=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오던 중 에러 발생 시...
            }
        });
        return check;
    }

    private void signUp() {
        String id=((EditText)findViewById(R.id.EditText)).getText().toString();
        String password=((EditText)findViewById(R.id.PW)).getText().toString();
        String nickname=((EditText)findViewById(R.id.NickName)).getText().toString();

        UserAccount userAccount=new UserAccount(id,password,nickname);
        databaseReference.child("userAccount").child(id).setValue(userAccount);

    }
}