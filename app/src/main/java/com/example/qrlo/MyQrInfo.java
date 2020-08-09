package com.example.qrlo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class MyQrInfo extends Activity {

    EditText address, detailAddress, phone, name;
    CheckBox isTemperature;
    ImageButton share;
    Button mod, del;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);
        address = findViewById(R.id.qr_info_address_edit);
        detailAddress = findViewById(R.id.qr_info_detail_address_edit);
        phone = findViewById(R.id.qr_info_phone_number_edit);
        name = findViewById(R.id.qr_info_qr_name_edit);
        isTemperature = findViewById(R.id.qr_info_is_temperature_check);
        share = findViewById(R.id.qr_info_share_imgbtn);
        mod = findViewById(R.id.qr_info_mod_btn);
        del = findViewById(R.id.qr_info_del_btn);



    }
}
