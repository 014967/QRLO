package com.example.qrlo.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.qrlo.After_Login;
import com.example.qrlo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PhoneAuthAgree extends AppCompatActivity {

    Button NextButton1;
    CheckBox AllCheck;
    CheckBox LocationCheck;
    CheckBox MarketingCheck;

    String LocationAgree;
    String MarketingAgree;

    String TAG= "PhoneAuthAgree";
    String name;
    String birth;
    FirebaseAuth mAuth;
    FirebaseUser User;
    String stuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_agree);


        AllCheck = findViewById(R.id.AllCheck);
        LocationCheck = findViewById(R.id.LocationCheck);
        MarketingCheck = findViewById(R.id.MarketingCheck);
        NextButton1 = findViewById(R.id.NextButton1);


        Intent in = getIntent();
        birth = in.getStringExtra("birth");
        name = in.getStringExtra("name");

        MarketingAgree = "DisAgree";
        LocationAgree = "DisAgree";



        AllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(AllCheck.isChecked()) {
                    LocationCheck.setChecked(true);
                    LocationAgree = "Agree";
                    MarketingCheck.setChecked(true);
                    MarketingAgree = "Agree";
                }
                else
                {
                    LocationCheck.setChecked(false);
                    LocationAgree = "DisAgree";
                    MarketingCheck.setChecked(false);
                    MarketingAgree = "DisAgree";
                }

            }
        });

        LocationCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(LocationCheck.isChecked())
                {
                    LocationAgree = "Agree";
                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
                else
                {
                    if(MarketingCheck.isChecked())
                    {
                        AllCheck.setChecked(false);
                        MarketingCheck.setChecked(true);
                    }

                    LocationAgree = "false";
                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
            }
        });
        MarketingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MarketingCheck.isChecked())
                {
                    MarketingAgree = "Agree";

                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
                else
                {

                    if(LocationCheck.isChecked())
                    {
                        AllCheck.setChecked(false);
                        LocationCheck.setChecked(true);
                    }
                    MarketingAgree = "false";
                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
            }
        });


        NextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                User= mAuth.getCurrentUser();


                if(!LocationCheck.isChecked())
                {
                    Toast.makeText(PhoneAuthAgree.this, "위치 기반 서비스 이용약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                }

                else {

                    if(User!=null)
                    {
                        stuid = User.getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference("user");
                        myRef.child(stuid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String userName = User.getDisplayName();

                                Map<String, Object> profile = new HashMap<String, Object>();
                                profile.put("LocationAgree", LocationAgree);
                                profile.put("MarketingAgree",MarketingAgree);
                                profile.put("birth", birth);
                                profile.put("name", name);

                                myRef.child(User.getUid()).setValue(profile);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });
                        Intent in = new Intent(PhoneAuthAgree.this , After_Login.class);
                        startActivity(in);
                        finish();
                    }
                    else {
                        Intent in = new Intent(PhoneAuthAgree.this, PhoneAuthActivity.class);

                        in.putExtra("LocationAgree", LocationAgree);
                        in.putExtra("MarketingAgree", MarketingAgree);
                        in.putExtra("birth", birth);
                        in.putExtra("name", name);

                        startActivity(in);
                    }
                }

            }
        });






    }
}