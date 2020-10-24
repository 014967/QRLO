package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrlo.bottomActivity.GpsTracker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class corona19_check extends AppCompatActivity {


    Button goHome;
    RadioButton RB1;
    RadioButton RB2;
    RadioButton RB3;
    RadioButton RB4;
    RadioButton RB5;
    RadioButton RB6;

    RadioGroup RG1;
    RadioGroup RG2;
    RadioGroup RG3;

    EditText etVisit;
    EditText etDegree;


    String bodyDegree;

    String stVisitHistorty;
    String stVisit;
    String stDegree;
    String stCorona;

    String QRvalue;
    String[] splitQRvalue;
    String stWhere;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    String wherelogo;
    String key;



    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona19_check);

        intent = getIntent();
        QRvalue = intent.getExtras().getString("QRvalue");
        splitQRvalue =QRvalue.split(",");
        stWhere = splitQRvalue[0] + splitQRvalue[1];// + splitQRvalue[2] + splitQRvalue[3];




        etDegree = findViewById(R.id.etDegree);



        RB1 = findViewById(R.id.RB1);
        RB2 = findViewById(R.id.RB2);
        RB3 = findViewById(R.id.RB3);
        RB4 = findViewById(R.id.RB4);
        RB5 = findViewById(R.id.RB5);
        RB6 = findViewById(R.id.RB6);


        RG1 = findViewById(R.id.RG1);
        RG2 = findViewById(R.id.RG2);
        RG3 = findViewById(R.id.RG3);

        etVisit = findViewById(R.id.etVisit);
        etVisit.setVisibility(View.INVISIBLE);

        RG1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RB1)
                {


                    etVisit.setVisibility(View.VISIBLE);

                    stVisit = "네";

                }
                if(checkedId == R.id.RB2)
                { etVisit.setVisibility(View.INVISIBLE);


                    stVisit ="아니오";
                }
            }
        });

        RB2.setChecked(true);


        RG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RB3)
                {

                    stDegree = "네";

                }
                if(checkedId == R.id.RB4){

                    stDegree ="아니오";
                }
            }
        });

        RB4.setChecked(true);

        RG3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RB5)
                {
                    stCorona = "네";
                }
                if(checkedId == R.id.RB6)
                {
                    stCorona ="아니오";
                }
            }
        });


        RB6.setChecked(true);
        goHome = findViewById(R.id.goHome);





        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("user");
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                wherelogo = splitQRvalue[4];
                key = splitQRvalue[5];

                bodyDegree = etDegree.getText().toString();
                if (bodyDegree.equals("")) {
                    Toast.makeText(corona19_check.this, "체온을 입력해주세요", Toast.LENGTH_SHORT).show();

                } else {

                    if (etVisit.equals("")) {

                    } else {
                        stVisitHistorty = etVisit.getText().toString();


                    }

                    myRef.child(user.getUid()).child("history").push().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Map<String, Object> profile = new HashMap<String, Object>();

                            profile.put("currentDegree", bodyDegree);
                            profile.put("visitHistoryfor2week", stVisit);  // 네 or 아니오
                            if (RB1.isChecked()) {

                                profile.put("stvisithistory", stVisitHistorty);   // 해외 이력 쓰기
                            } else {

                                profile.put("stvisithistory", "아니오, 없습니다");

                            }

                            profile.put("Pulmonarysym", stDegree);
                            profile.put("visitlocationfor2week", stCorona);
                            profile.put("where", stWhere);
                            profile.put("when", ServerValue.TIMESTAMP);
                            profile.put("wherelogo", wherelogo);
                            profile.put("key", key);


                            myRef.child(user.getUid()).child("history").push().updateChildren(profile);
                            // 방문 지역 주제 구독 => 나중에 이 주제로 알림 발송
                            FirebaseMessaging.getInstance().subscribeToTopic(key);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Intent in = new Intent(corona19_check.this, After_Login.class);
                    startActivity(in);
                    finish();
                }
            }
        });
    }
}