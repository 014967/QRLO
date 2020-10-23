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

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final double MIN = 0.01;
    private static final double MAX = 0.01;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission==PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){

        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longtitude){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longtitude, 7);
        }catch (IOException ioException){
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() ==0){
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return  address.getAddressLine(0).toString()+"\n";
    }

    private  void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()){
                    if (checkLocationServicesStatus()){
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona19_check);

        intent = getIntent();
        QRvalue = intent.getExtras().getString("QRvalue");
        splitQRvalue =QRvalue.split(",");
        stWhere = splitQRvalue[0] + splitQRvalue[1];// + splitQRvalue[2] + splitQRvalue[3];

        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else{
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        String address = splitQRvalue[0];

        Geocoder geocoder = new Geocoder(this);
        List<Address> list =null;
        try{
            list=geocoder.getFromLocationName(address, 10);
        }catch (IOException e){
            Toast.makeText(this, "뭔가 문제가잇음", Toast.LENGTH_SHORT).show();
        }

        if(list == null){
                Toast.makeText(this, "해당되는 주소 정보는 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            if(latitude-MIN<=list.get(0).getLatitude() && list.get(0).getLatitude()<=latitude+MAX&&
                    longitude-MIN<=list.get(0).getLongitude() && list.get(0).getLongitude()<=longitude+MAX){
                Toast.makeText(this, "주소정보가 맞습니다.", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this, "주소정보가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
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

                            profile.put("현재 온도", bodyDegree);
                            profile.put("2주간 해외 방문 이력", stVisit);
                            if (RB1.isChecked()) {

                                profile.put("해외 이력", stVisitHistorty);
                            } else {

                                profile.put("해외 이력", "아니오, 없습니다");

                            }

                            profile.put("발열 및 호흡기증상 유무", stDegree);
                            profile.put("2주내에 확진자 발생 지역 방문 유무", stCorona);
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