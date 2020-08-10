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

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MyQrInfo extends Activity {

    EditText address, detailAddress, phone, name;
    CheckBox isTemperature;
    ImageView share, logo, mod, del;
    private static final int ADD_LOGO = 10001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        address = findViewById(R.id.qr_info_address_edit);
        detailAddress = findViewById(R.id.qr_info_detail_address_edit);
        phone = findViewById(R.id.qr_info_phone_number_edit);
        name = findViewById(R.id.qr_info_qr_name_edit);
        isTemperature = findViewById(R.id.qr_info_is_temperature_check);
        logo = findViewById(R.id.qr_info_logo_img);
        share = findViewById(R.id.qr_info_share_imgbtn);
        mod = findViewById(R.id.qr_info_mod_btn);
        del = findViewById(R.id.qr_info_del_btn);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent inIntent = getIntent();
        byte[] byteArray = inIntent.getByteArrayExtra("Logo");
        final int pos = inIntent.getIntExtra("Position", 0);
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        logo.setImageBitmap(image);
        name.setText(inIntent.getStringExtra("QR name"));
        address.setText(inIntent.getStringExtra("Address"));
        detailAddress.setText(inIntent.getStringExtra("Detail address"));
        if(inIntent.getBooleanExtra("Temperature", false)==false)
            isTemperature.setChecked(false);
        else
            isTemperature.setChecked(true);
        phone.setText(inIntent.getStringExtra("Phone number"));

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, ADD_LOGO);
            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MyQrInfo.this);
                dlg.setMessage("수정하시겠습니까?");
                dlg.setNegativeButton("아니오", null);
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent outIntent = new Intent(getApplicationContext(), MyQrActivity.class);

                        Bitmap bitmap = ((BitmapDrawable)logo.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        outIntent.putExtra("Logo", byteArray);
                        outIntent.putExtra("Address", address.getText().toString());
                        outIntent.putExtra("Temperature", isTemperature.isChecked());
                        outIntent.putExtra("Detail address", detailAddress.getText().toString());
                        outIntent.putExtra("QR name", name.getText().toString());
                        outIntent.putExtra("Phone number", phone.getText().toString());
                        outIntent.putExtra("Position", pos);

                        my_qr_item item = new my_qr_item();
                        item.setTitle(name.getText().toString());
                        item.setAddress(address.getText().toString());
                        item.setDetailAddress(detailAddress.getText().toString());
                        item.setTemp(isTemperature.isChecked());
                        item.setPhone(phone.getText().toString());

                        setResult(RESULT_OK, outIntent);

                        databaseReference.child("user").child(user.getUid()).child("myQR").push().setValue(item);

                        finish();
                    }
                });

                dlg.show();
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
            case ADD_LOGO :
                if(resultCode == RESULT_OK)
                {
                    try{
                        InputStream in = getContentResolver().openInputStream(intent.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        logo.setImageBitmap(img);
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
