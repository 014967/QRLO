package com.example.qrlo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MyQrInfo extends Activity {

    ImageView share, logo, mod, del;
    TextView address, phone;
    private static final int QR_CREATE = 10002;
    private static final int QR_MOD = 10001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        logo = findViewById(R.id.qr_info_logo_img);
        share = findViewById(R.id.qr_info_share_imgbtn);
        mod = findViewById(R.id.qr_info_mod_btn);
        del = findViewById(R.id.qr_info_del_btn);
        address = findViewById(R.id.qr_info_qr_address_txt);
        phone = findViewById(R.id.qr_info_phone_txt);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent inIntent = getIntent();

        setTitle(inIntent.getStringExtra("QR name"));

        byte[] byteArray = inIntent.getByteArrayExtra("Logo");
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        logo.setImageBitmap(image);

        address.setText(inIntent.getStringExtra("Address"));
        phone.setText(inIntent.getStringExtra("Phone"));


        final int pos = inIntent.getIntExtra("Position", 0);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModQrActivity.class);
                my_qr_item item = new my_qr_item();

                Bitmap bitmap = item.getIcon();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("ImageURL", item.getIconURI());
                intent.putExtra("QR name", item.getTitle());
                intent.putExtra("Address", item.getAddress());
                intent.putExtra("Detail address", item.getDetailAddress());
                intent.putExtra("Phone", item.getPhone());
                intent.putExtra("Temperature", item.getTemp());
                intent.putExtra("Position", pos);


                startActivityForResult(intent, QR_MOD);

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){
            case QR_CREATE:
                if(resultCode == RESULT_OK) {

                }
                break;
        }
    }
}
