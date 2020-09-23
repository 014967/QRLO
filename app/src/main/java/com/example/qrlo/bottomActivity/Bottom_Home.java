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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;


import com.example.qrlo.R;
import com.example.qrlo.corona19_check;
import com.example.qrlo.my_qr_item;
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

import java.io.IOException;

public class Bottom_Home extends Fragment {


    Camera camera;
    SurfaceView surfaceView;
    String QRvalue;
    boolean qr = false;
    String[] splits;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;


    String TAG = "Qrcode = ";


    public Bottom_Home() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_bottom__home, container, false);


        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView);

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


                                QRvalue = qrCodes.valueAt(0).displayValue;
                                Log.d(TAG, QRvalue);

                                splits = QRvalue.split(my_qr_item.QR_CERTI_SPLIT_TOKEN);

                                if (splits[0].equals(my_qr_item.QR_CERTI)) {

                                    qr = true;


                                    new Thread() {
                                        @Override
                                        public void run() {
                                            cameraSource.stop();
                                        }
                                    }.start();

                                    Intent in = new Intent(getContext(), corona19_check.class);
                                    in.putExtra("QRvalue", splits[1]);
                                    startActivity(in);


                                }

                            }


                        }


                    }
                });
            }
        }.start();

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
    }
}
