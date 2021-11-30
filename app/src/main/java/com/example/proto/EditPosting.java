package com.example.proto;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditPosting extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    String str_id, writer;
    EditText title, body, name;
    RadioGroup type, score, waiting;
    Button submit, img, btn_locate;
    TextView txt_locate;
    Switch aSwitch;
    ImageView photo;
    private String title_, addr_, user_,type_,name_,body_,score_,waiting_;

    RadioButton type_btn1, type_btn2, type_btn3, type_btn4, type_btn5, score_btn1, score_btn2, score_btn3, waiting_btn1, waiting_btn2;

    private final int GALLERY_CODE=10;
    //private FirebaseStorage storage;

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE=2001;
    private static final int PERMISSIONS_REQUEST_CODE=100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private FirebaseStorage storage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        setTitle("게시글 수정");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        //EditTExt 설정
        title = (EditText) findViewById(R.id.text_title);
        body =  (EditText)findViewById(R.id.body);
        name = (EditText)findViewById(R.id.text_name);
        txt_locate =  (TextView)findViewById(R.id.txt_locate);
        type=(RadioGroup)findViewById(R.id.group_type);
        score=(RadioGroup)findViewById(R.id.group_score);
        waiting=(RadioGroup)findViewById(R.id.group_waiting);
        photo=(ImageView)findViewById(R.id.photo);
        img=(Button)findViewById(R.id.btn_img);
        btn_locate=(Button)findViewById(R.id.btn_locate);

        // listactivity에서 받아오기
        Intent intent2 = getIntent();

        title_ = intent2.getStringExtra("title");
        addr_ = intent2.getStringExtra("addr");
        str_id = intent2.getStringExtra("user_id");
        writer = intent2.getStringExtra("writer");
        name_=intent2.getStringExtra("name");
        body_=intent2.getStringExtra("body");

        title.setText(title_);
        txt_locate.setText(addr_);
        body.setText(body_);
        name.setText(name_);

        // 사진 edit 불가
        img.setEnabled(false);
        // 제목 edit 불가 =>사진 경로 때문에
        title.setEnabled(false);

        // firebase storage에서 사진(사진 이름은 user id + title ) 불러오기
        database = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference =  firebaseStorage.getReference();
        StorageReference pathReference = storageReference.child("photo");
        if(pathReference == null){
            Toast.makeText(EditPosting.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            String storagePath = writer+title_;
            Log.i("EditPosting ",storagePath);
            StorageReference imgReference = storageReference.child("photo/"+storagePath+".png");
            imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(EditPosting.this).load(uri).into(photo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditPosting.this,"등록된 사진이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }


        //현재위치 가져오기
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker=new GpsTracker(EditPosting.this);
                double latitude=gpsTracker.getLatitude();
                double longitude=gpsTracker.getLongitude();
                String addr=getCurrentAddress(latitude,longitude);

                txt_locate.setText(addr);

            }
        });

        //작성 완료 버튼 이벤트
        submit=findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
                finish();
            }
        });

    } // onCreate end

    public boolean nullCheck(EditText editText){
        if(editText.getText().toString().replace(" ", "").equals(""))
            return false;
        else return true;
    }

    // OnClickListener 커스텀
    View.OnClickListener onClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_img:
                    loadAlbum();
                    break;
            }
        }
    };

    private void loadAlbum(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivity(intent);
    }

    private void post(){
//        String str_id = user_;
        String str_title=title.getText().toString();
        String str_body=body.getText().toString();
        String str_name=name.getText().toString();

        int tmp=type.getCheckedRadioButtonId();
        RadioButton btn_type=findViewById(tmp);
        String str_type=btn_type.getText().toString();

        tmp=score.getCheckedRadioButtonId();
        RadioButton btn_score=findViewById(tmp);
        String str_score=btn_score.getText().toString();

        tmp=waiting.getCheckedRadioButtonId();
        RadioButton btn_waiting=findViewById(tmp);
        String str_waiting=btn_waiting.getText().toString();




//        Item item= new Item(str_id,str_name,str_title,str_body,txt_locate.getText().toString(),str_type,str_score,str_waiting);

//        databaseReference.child("postList").push().setValue(item);

        databaseReference.child("postList").orderByChild("title").equalTo(title_).addListenerForSingleValueEvent(new ValueEventListener() { //                        @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datas: snapshot.getChildren()){
                    String snapshotKey=datas.getKey();
                    if(snapshotKey!=null) {
                        databaseReference.child("postList").child(snapshotKey).child("name").setValue(str_name);
                        databaseReference.child("postList").child(snapshotKey).child("title").setValue(str_title);
                        databaseReference.child("postList").child(snapshotKey).child("body").setValue(str_body);
                        databaseReference.child("postList").child(snapshotKey).child("score").setValue(str_score);
                        databaseReference.child("postList").child(snapshotKey).child("type").setValue(str_type);
                        databaseReference.child("postList").child(snapshotKey).child("waiting").setValue(str_waiting);
                        databaseReference.child("postList").child(snapshotKey).child("address").setValue(txt_locate.getText().toString());

                        Toast.makeText(getApplicationContext(),"글을 성공적으로 수정하였습니다.",Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
                startActivity(new Intent(getApplicationContext(),listActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(EditPosting.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(EditPosting.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(EditPosting.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(EditPosting.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditPosting.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(EditPosting.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(EditPosting.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(EditPosting.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();

    }



    // GPS활성화 함수들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditPosting.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
