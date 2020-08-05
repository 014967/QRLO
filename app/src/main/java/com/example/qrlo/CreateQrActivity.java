package com.example.qrlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreateQrActivity extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    Button btnAddress, btnOK;
    EditText address, detailAddress, phone, name;
    CheckBox isTemperature;
    ImageButton addLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        // xml 참조
        address = findViewById(R.id.create_qr_address_edit);
        detailAddress = findViewById(R.id.create_qr_detail_address_edit);
        phone = findViewById(R.id.create_qr_phone_edit);
        isTemperature = findViewById(R.id.create_qr_is_temperature_check);
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

        /*
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MyQrActivity.class);
                //TODO outIntent.putExtra("Logo", ) 로고는 어떻게 받아오지?
                outIntent.putExtra("Detail address", detailAddress.getText().toString());
                outIntent.putExtra("QR name", name.getText().toString());

                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

         */
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
        }
    }
}