package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModQrActivity extends AppCompatActivity {
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int ADD_LOGO = 10001;
    boolean isImgChanged = false;

    Button btnAddress, btnOK;
    EditText address, detailAddress, phone, name;
    Switch isTemperature;
    ImageView addLogo;
    my_qr_item item = new my_qr_item();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        // xml 참조
        address = findViewById(R.id.create_qr_address_edit);
        detailAddress = findViewById(R.id.create_qr_detail_address_edit);
        phone = findViewById(R.id.create_qr_phone_edit);
        isTemperature = findViewById(R.id.create_qr_is_temperature_switch);
        addLogo = findViewById(R.id.create_qr_logo_btn);
        name = findViewById(R.id.create_qr_name_edit);
        btnOK = findViewById(R.id.create_qr_ok);
        btnAddress = findViewById(R.id.create_qr_address_btn);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://qrlo-798fd.appspot.com");
        Date date = new Date();
        final String imagedata = firebaseUser.getUid()+"/"+date.toString()+".jpg";
        final StorageReference storageReference = firebaseStorage.getReference().child(imagedata);

        Intent inIntent = getIntent();

        item.setTitle(inIntent.getStringExtra("QR name"));
        item.setIconURI(inIntent.getStringExtra("ImageURL"));
        item.setAddress(inIntent.getStringExtra("Address"));
        item.setDetailAddress(inIntent.getStringExtra("Detail address"));
        item.setTemp(inIntent.getBooleanExtra("Temperature", false));
        item.setPhone(inIntent.getStringExtra("Phone number"));
        pos = inIntent.getIntExtra("Position", 0);

        Glide.with(getApplicationContext()).load(item.getIconURI()).into(addLogo);
        name.setText(item.getTitle());
        address.setText(item.getAddress());
        detailAddress.setText(item.getDetailAddress());
        isTemperature.setChecked(item.getTemp());
        phone.setText(item.getPhone());


        if (btnAddress != null)
            btnAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ModQrActivity.this, AddressWebViewActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                }
            });

        addLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, ADD_LOGO);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MyQrInfo.class);

                if(isImgChanged) {
                    Bitmap bitmap = ((BitmapDrawable)addLogo.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    UploadTask uploadTask = storageReference.putBytes(byteArray);

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUrl = task.getResult();
                                String photoUrl = String.valueOf(downloadUrl);

                                my_qr_item item1 = new my_qr_item();

                                item1.setIconURI(photoUrl);
                                item1.setIconName(imagedata);
                                item1.setTitle(name.getText().toString());
                                item1.setAddress(address.getText().toString());
                                item1.setDetailAddress(detailAddress.getText().toString());
                                item1.setTemp(isTemperature.isChecked());
                                item1.setPhone(phone.getText().toString());
                                String key = databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").push().getKey();
                                item1.setKey(key);
                                databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").child(key).setValue(item1);
                            }

                        }
                    });

                }
                else {
                    my_qr_item item2 = new my_qr_item();

                    String photoUrl = "https://firebasestorage.googleapis.com/v0/b/qrlo-798fd.appspot.com/o/qrlo_icon.jpg?alt=media&token=67161f75-9373-48bb-90fd-07988ff217fa";
                    item2.setIconURI(photoUrl);
                    item2.setTitle(name.getText().toString());
                    item2.setAddress(address.getText().toString());
                    item2.setDetailAddress(detailAddress.getText().toString());
                    item2.setTemp(isTemperature.isChecked());
                    item2.setPhone(phone.getText().toString());
                    String key = databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").push().getKey();
                    item2.setKey(key);
                    databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").child(key).setValue(item);
                }//로고가 없는 상태에서도 넘어가게 만들어야됨

                outIntent.putExtra("Position", pos);
                setResult(RESULT_OK, outIntent);

                finish();


            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){
            case SEARCH_ADDRESS_ACTIVITY :
                if(resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");
                    if (data != null)
                        address.setText(data);
                }
                break;
            case ADD_LOGO :
                if(resultCode == RESULT_OK)
                {
                    try{
                        InputStream in = getContentResolver().openInputStream(intent.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        addLogo.setImageBitmap(img);

                        isImgChanged =true;


                    }catch (Exception e){

                    }
                }
                else if(resultCode == RESULT_CANCELED)
                {
                }
                break;
        }
    }
}
