package com.example.proto;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class Posting extends AppCompatActivity {
    EditText title;
    EditText body;
    EditText name;
    RadioGroup type;
    RadioGroup score;
    RadioGroup waiting;
    Button submit;
    Button btn_img;
    Button btn_locate;
    TextView txt_locate;
    ImageView photo;


    // RadioButton type_btn1, type_btn2, type_btn3, type_btn4, type_btn5, score_btn1, score_btn2, score_btn3, waiting_btn1, waiting_btn2;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private String str_id, posting_title;
    private final int GALLERY_CODE=10;
    private FirebaseStorage storage;
    Uri downloadUri;

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE=2001;
    private static final int PERMISSIONS_REQUEST_CODE=100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        setTitle("게시글 작성");
        title=(EditText) findViewById(R.id.text_title);
        body=(EditText) findViewById(R.id.body);
        name=(EditText) findViewById(R.id.text_name);
        type=(RadioGroup) findViewById(R.id.group_type);
        score=(RadioGroup) findViewById(R.id.group_score);
        waiting=(RadioGroup) findViewById(R.id.group_waiting);
        photo=(ImageView) findViewById(R.id.photo);
        btn_img=(Button) findViewById(R.id.btn_img);
        btn_locate=(Button) findViewById(R.id.btn_locate);
        txt_locate=(TextView) findViewById(R.id.txt_locate);

        /*
        type_btn1 = (RadioButton) findViewById(R.id.type_btn1);
        type_btn2 = (RadioButton) findViewById(R.id.type_btn2);
        type_btn3 = (RadioButton) findViewById(R.id.type_btn3);
        type_btn4 = (RadioButton) findViewById(R.id.type_btn4);
        type_btn5 = (RadioButton) findViewById(R.id.type_btn5);

        score_btn1 = (RadioButton) findViewById(R.id.score_btn1);
        score_btn2 = (RadioButton) findViewById(R.id.score_btn2);
        score_btn3 = (RadioButton) findViewById(R.id.score_btn3);

        waiting_btn1 = (RadioButton) findViewById(R.id.waiting_btn1);
        waiting_btn2 = (RadioButton) findViewById(R.id.waiting_btn2); */

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //사용자 아이디 받아오기
        str_id=getIntent().getStringExtra("id");

        checkRunTimePermission();

        //현재위치 가져오기
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker=new GpsTracker(Posting.this);
                double latitude=gpsTracker.getLatitude();
                double longitude=gpsTracker.getLongitude();
                String addr=getCurrentAddress(latitude,longitude);

                txt_locate.setText(addr);

            }
        });

        // 타이틀이 0글자면 사진등록버튼 활성화x
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    btn_img.setEnabled(true);
                }
                else{
                    btn_img.setEnabled(false);
                }
            }
        });
        // 사진 등록 버튼
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE); // 갤러리 액티비티로 이동
                startActivityForResult(intent,GALLERY_CODE);
            }
        });



        //작성 완료 버튼 이벤트
        submit=findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!nullCheck(title) || !nullCheck(body) || !nullCheck(name)){
                    Toast.makeText(Posting.this,"빈칸을 확인하세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    post();
                    Toast.makeText(Posting.this,"글을 성공적으로 등록하였습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    } // onCreate

    public boolean nullCheck(EditText editText){
        if(editText.getText().toString().replace(" ", "").equals(""))
            return false;
        else return true;
    }


    private void post(){
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




        Item item= new Item(str_id,str_name,str_title,str_body,txt_locate.getText().toString(),
                str_type,str_score,str_waiting);
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    //전체공개
//                    item.setOpen("1");
//                }else{
//                    //친구공개
//                    item.setOpen("0");
//                }
//            }
//        });
        databaseReference.child("postList").push().setValue(item);
        //Toast.makeText(getApplicationContext(),"글을 성공적으로 등록하였습니다.",Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(Posting.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(Posting.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Posting.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Posting.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Posting.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Posting.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Posting.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Posting.this, REQUIRED_PERMISSIONS,
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Posting.this);
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

        if(resultCode!=RESULT_OK) {
            Toast.makeText(getApplicationContext(), "사진 업로드 실패", Toast.LENGTH_SHORT).show();
        }

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

            case GALLERY_CODE: // 갤러리에서 받아온 사진 storage에 저장하기
                try {
                    Uri file = data.getData();
                    posting_title = title.getText().toString();
                    String filePath = str_id + posting_title;
                    StorageReference storageReference = storage.getReference();
                    StorageReference riversRef = storageReference.child("photo/" + filePath + ".png");
                    UploadTask uploadTask = riversRef.putFile(file); // 파일 upload

                    // 이미지 url Item에 등록하려는거 시도한 흔적
                    /*
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUri = task.getResult();
                            }
                        }
                    }); */


                try{
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    photo.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"사진 업로드 실패",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"사진 업로드 성공",Toast.LENGTH_SHORT).show();
                    }
                });
                }
                catch (NullPointerException e){
                    Toast.makeText(Posting.this,"이미지 선택 안함",Toast.LENGTH_SHORT).show();
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



