package com.example.qrlo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreateQrActivity extends AppCompatActivity {

    Button add
    EditText detailAddress, phone;
    CheckBox isTemperature;
    ImageButton addLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        // 내 qr 코드 목록에서 + 버튼 눌렀을 때 나타날 액티비티
        detailAddress = findViewById(R.id.create_qr_detail_address_edit);
        phone = findViewById(R.id.create_qr_phone_edit);
        isTemperature = findViewById(R.id.create_qr_is_temperature_check);
        addLogo = findViewById(R.id.create_qr_logo_btn);

        //TODO 주소를 가져올 때 다음 우편번호 주소 API : http://dailyddubby.blogspot.com/2018/02/2-api.html

        //TODO 이거 할라면 php로 서버 만들고 해야한다고 함
    }
}