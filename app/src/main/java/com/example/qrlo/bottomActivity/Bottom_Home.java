package com.example.qrlo.bottomActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.example.qrlo.R;
import com.example.qrlo.corona19_check;
import com.example.qrlo.my_qr_item;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Bottom_Home extends Fragment {


    String CurrentTime;
    String ServerTime;
    Camera camera;
    SurfaceView surfaceView;
    String QRvalue;
    boolean qr = false;
    String[] splits;
    String[] splitQRvalue;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;

    double latitude;
    double longitude;

    ArrayList<VisitInfo> visitHistory;
    boolean isadded;
    String TAG = "Qrcode = ";

    Button userBTN;
    Button empBTN;
    Toast toast1;
    Toast toast2;

    Calendar calendar;

    public Bottom_Home() {

    }
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission==PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){

        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])){
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longtitude){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longtitude, 7);
        }catch (IOException ioException){
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() ==0){
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return  address.getAddressLine(0).toString()+"\n";
    }

    private  void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_bottom__home, container, false);


        userBTN = v.findViewById(R.id.userBTN);
        empBTN = v.findViewById(R.id.empBTN);
        userBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://youtu.be/f8uNRajXz2E"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        empBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://youtu.be/prf6H8Yy3hQ"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        visitHistory = new ArrayList<>();

        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else{
            checkRunTimePermission();
        }

        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView);



        gpsTracker = new GpsTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        toast1 = Toast.makeText(getContext(), "주소정보가 맞지 않습니다.", Toast.LENGTH_SHORT);
        toast2= Toast.makeText(getContext(),"체크인을 완료했습니다.",Toast.LENGTH_SHORT);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        qr = false;


    }

    @Override
    public void onResume() {
        super.onResume();

        barcodeDetector = new BarcodeDetector.Builder(getContext()).
                setBarcodeFormats(Barcode.QR_CODE).build();
        barcodeDetector.release();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedFps(29.8f).setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(640, 480).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        new Thread() {
            public void run() {
                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {


                        SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                        if (!(qrCodes == null && qr == false)) {


                            Log.d(TAG, String.valueOf(qrCodes));
                            if (qrCodes.size() != 0) {

                                new Thread() {
                                    @Override
                                    public void run() {
                                        cameraSource.stop();
                                    }
                                }.start();
                                QRvalue = qrCodes.valueAt(0).displayValue;
                                Log.d(TAG, QRvalue);

                                splits = QRvalue.split(my_qr_item.QR_CERTI_SPLIT_TOKEN);

                                if (splits[0].equals(my_qr_item.QR_CERTI)) {

                                    qr = true;

                                    splitQRvalue =splits[1].split(",");
                                    String address = splitQRvalue[0];

                                    Geocoder geocoder = new Geocoder(getActivity());
                                    List<Address> list =null;
                                    try{
                                        list=geocoder.getFromLocationName(address, 10);
                                    }catch (IOException e){
                                        //Toast.makeText(getActivity(), "뭔가 문제가잇음", Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(getActivity(), list.get(0).toString(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG+"lllllll", Double.toString(list.get(0).getLatitude())+"+"+Double.toString(latitude));

                                    if(list == null){
                                    }else{
                                        if(latitude-MIN<=list.get(0).getLatitude() && list.get(0).getLatitude()<=latitude+MAX&&
                                                longitude-MIN<=list.get(0).getLongitude() && list.get(0).getLongitude()<=longitude+MAX){
                                            //Toast.makeText(getActivity(), "주소정보가 맞습니다.", Toast.LENGTH_SHORT).show();
                                            isadded=false;
                                            myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChild("history")) {
                                                        Query currentime = myRef.child(user.getUid()).child("history").orderByChild("when").limitToLast(1);
                                                        Log.d(TAG, currentime.toString());

                                                        currentime.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    VisitInfo visitInfo = dataSnapshot.getValue(VisitInfo.class);
                                                                    SimpleDateFormat sfd = new SimpleDateFormat("ddMMyyyy");
                                                                    Date date = new Date(visitInfo.getWhen());
                                                                    String d = sfd.format(date);
                                                                    Log.d(TAG, "String d  : " + d);
                                                                    SimpleDateFormat sfd2 = new SimpleDateFormat("ddMMyyyy");

                                                                    calendar = Calendar.getInstance();
                                                                    String f = sfd2.format(calendar.getTime());

                                                                    Log.d(TAG, "String f : " + f);

                                                                    if (d.equals(f)) {
                                                                        Map<String, Object> profile = new HashMap<String, Object>();
                                                                        profile.put("currentDegree", visitInfo.getCurrentDegree());
                                                                        profile.put("visitHistoryfor2week", visitInfo.getVisitHistoryfor2week());
                                                                        profile.put("stvisithistory", visitInfo.getStvisithistory());

                                                                        profile.put("Pulmonarysym", visitInfo.getPulmonarysym());
                                                                        profile.put("visitlocationfor2week", visitInfo.getVisitlocationfor2week());
                                                                        profile.put("where", visitInfo.getWhere());
                                                                        profile.put("when", ServerValue.TIMESTAMP);
                                                                        profile.put("wherelogo", visitInfo.getWherelogo());
                                                                        profile.put("key", visitInfo.getKey());
                                                                        if(!isadded) {
                                                                            myRef.child(user.getUid()).child("history").push().updateChildren(profile);
                                                                            isadded = true;
                                                                            toast2.show();
                                                                        }

                                                                    }
                                                                    else
                                                                    {

                                                                        Intent in =new Intent(getContext(),corona19_check.class);
                                                                        in.putExtra("QRvalue", splits[1]);
                                                                        startActivity(in);
                                                                    }
                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }
                                                    else
                                                    {

                                                        Intent in =new Intent(getContext(),corona19_check.class);
                                                        in.putExtra("QRvalue", splits[1]);
                                                        startActivity(in);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }else
                                        {

                                            toast1.show();
                                        }
                                    }




                                    new Thread() {
                                        @Override
                                        public void run() {
                                            cameraSource.stop();
                                        }
                                    }.start();














                                }

                            }


                        }


                    }
                });
            }
        }.start();
    }
}
