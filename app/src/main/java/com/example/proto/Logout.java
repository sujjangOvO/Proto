package com.example.proto;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        AlertDialog.Builder dlg = new AlertDialog.Builder(Logout.this);

        dlg.setTitle("로그아웃");
        dlg.setMessage("로그아웃 하시겠습니까?");

        dlg.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        dlg.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(getApplicationContext(), My_page.class);
                startActivity(intent);
                finish();
            }
        });

        dlg.show();

    }
}