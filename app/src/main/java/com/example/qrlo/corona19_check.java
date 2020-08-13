package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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
                else
                { etVisit.setVisibility(View.INVISIBLE);


                    stVisit ="아니오";
                }
            }
        });


        RG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RB3)
                {

                    stDegree = "네";

                }
                else{

                    stDegree ="아니오";
                }
            }
        });

        RG3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.RB5)
                {
                    stCorona = "네";
                }
                else
                {
                    stCorona ="아니오";
                }
            }
        });

        goHome = findViewById(R.id.goHome);




        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("user");
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                wherelogo = splitQRvalue[4];

                bodyDegree = etDegree.getText().toString();
                if(etVisit.equals(""))
                {

                }
                else {
                    stVisitHistorty = etVisit.getText().toString();




                }

                myRef.child(user.getUid()).child("history").push().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String, Object> profile = new HashMap<String, Object>();

                        profile.put("현재 온도", bodyDegree);
                        profile.put("2주간 해외 방문 이력" , stVisit);
                        if(RB1.isChecked())
                        {

                            profile.put("해외 이력", stVisitHistorty);
                        }
                        else
                        {

                                profile.put("해외 이력" , "아니오, 없습니다");

                        }

                        profile.put("발열 및 호흡기증상 유무", stDegree);
                        profile.put("2주내에 확진자 발생 지역 방문 유무", stCorona);
                        profile.put("where",stWhere);
                        profile.put("when", ServerValue.TIMESTAMP);
                        profile.put("wherelogo", wherelogo);



                        myRef.child(user.getUid()).child("history").push().updateChildren(profile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });









                Intent in = new Intent(corona19_check.this, After_Login.class);
                startActivity(in);
                finish();
            }
        });
    }
}