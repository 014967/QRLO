package com.example.qrlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CreateQrActivity extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int ADD_LOGO = 10001;
    boolean isImgChanged = false;

    Button btnAddress, btnOK;
    EditText address, detailAddress, phone, name;
    Switch isTemperature;
    ImageView addLogo;

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

        if (btnAddress != null)
            btnAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CreateQrActivity.this, AddressWebViewActivity.class);
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
                Intent outIntent = new Intent(getApplicationContext(), MyQrActivity.class);

                if(isImgChanged) {
                    Bitmap bitmap = ((BitmapDrawable)addLogo.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    outIntent.putExtra("Logo", byteArray);
                }
                else {
                    Drawable drawable = getResources().getDrawable(R.drawable.base);
                    Bitmap bitmap2 = ((BitmapDrawable)drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray2 = stream.toByteArray();
                    outIntent.putExtra("Logo", byteArray2);
                }
                outIntent.putExtra("Address", address.getText().toString());
                outIntent.putExtra("Temperature", isTemperature.isChecked());
                outIntent.putExtra("Detail address", detailAddress.getText().toString());
                outIntent.putExtra("QR name", name.getText().toString());
                outIntent.putExtra("Phone number", phone.getText().toString());

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