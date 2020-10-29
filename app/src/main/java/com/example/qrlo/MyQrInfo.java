package com.example.qrlo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.qrlo.bottomActivity.Bottom_Administor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class MyQrInfo extends Activity {

    private static final int QR_CREATE = 10002;
    private static final int QR_MOD = 10001;
    private static final int HEIGHT = 200;
    private static final int WIDTH = 200;
    ImageView share, qr, mod, del;
    TextView address, phone;
    my_qr_item item = new my_qr_item();
    int pos;
    Bitmap qrBit;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://qrlo-798fd.appspot.com");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        qr = findViewById(R.id.qr_info_qr_img);
        share = findViewById(R.id.qr_info_share_imgbtn);
        mod = findViewById(R.id.qr_info_mod_btn);
        del = findViewById(R.id.qr_info_del_btn);
        address = findViewById(R.id.qr_info_qr_address_txt);
        phone = findViewById(R.id.qr_info_phone_txt);

        Intent inIntent = getIntent();

        item.setTitle(inIntent.getStringExtra("QR name"));
        item.setIconURI(inIntent.getStringExtra("ImageURL"));
        item.setAddress(inIntent.getStringExtra("Address"));
        item.setDetailAddress(inIntent.getStringExtra("Detail address"));
        item.setTemp(inIntent.getBooleanExtra("Temperature", false));
        item.setPhone(inIntent.getStringExtra("Phone number"));
        item.setKey(inIntent.getStringExtra("Key"));
        item.setIconName(inIntent.getStringExtra("IconName"));
        pos = inIntent.getIntExtra("Position", 0);
        item.updateQR();

        // QR 코드 생성
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = multiFormatWriter.encode(item.getStrQR(), BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrBit = barcodeEncoder.createBitmap(bitMatrix);
            qr.setImageBitmap(qrBit);
        }catch (Exception e){
            Log.w("MY_QR_INFO", e.getMessage());
        }

        setTitle(item.getTitle());
        //Glide.with(getApplicationContext()).load(item.getIconURI()).into(qr);
        address.setText(item.getAddress());
        phone.setText(item.getPhone());

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File cachePath = new File(getExternalCacheDir(), "my_images/");
                cachePath.mkdirs();

                //create png file
                File file = new File(cachePath, "Image_123.png");
                FileOutputStream fileOutputStream;
                try
                {
                    fileOutputStream = new FileOutputStream(file);
                    qrBit.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                //---Share File---//
                //get file uri
                try{
                    Uri myImageFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);

                    //create a intent
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri);
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent, "Share with"));
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }



            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModQrActivity.class);

                intent.putExtra("QR name", item.getTitle());
                intent.putExtra("ImageURL", item.getIconURI());
                intent.putExtra("Address", item.getAddress());
                intent.putExtra("Detail address", item.getDetailAddress());
                intent.putExtra("Temperature", item.getTemp());
                intent.putExtra("Phone number", item.getPhone());
                intent.putExtra("Position", pos);
                intent.putExtra("Key", item.getKey());

                startActivityForResult(intent, QR_MOD);

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MyQrInfo.this);
                dlg.setMessage("정말 삭제하시겠습니까?");
                dlg.setIcon(R.drawable.qrlo_icon);
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        StorageReference storageReference = firebaseStorage.getReference();
                        if(item.getIconName()==null) {
                            databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(getApplicationContext(), "기본 이미지", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        else{
                            storageReference.child(item.getIconName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "이미지 삭제", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        Intent intent = new Intent(getApplicationContext(), Bottom_Administor.class);
                        intent.putExtra("Position", pos);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                dlg.setNegativeButton("아니오", null);
                dlg.show();
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){
            case QR_MOD:
                if(resultCode == RESULT_OK) {
                    StorageReference storageReference = firebaseStorage.getReference();
                    if(item.getIconURI()!="https://firebasestorage.googleapis.com/v0/b/qrlo-798fd.appspot.com/o/qrlo_icon.jpg?alt=media&token=67161f75-9373-48bb-90fd-07988ff217fa"&& item.getIconName()!=null) {
                        storageReference.child(item.getIconURI()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                                public void onSuccess(Void aVoid) {
                                databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                    else{
                        databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }

                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}
