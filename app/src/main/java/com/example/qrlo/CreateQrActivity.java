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

    Button btnOK;
    EditText detailAddress, phone, name;
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
        name = findViewById(R.id.create_qr_name_edit);
        btnOK = findViewById(R.id.create_qr_ok);

        //TODO 주소를 가져올 때 다음 우편번호 주소 API : http://dailyddubby.blogspot.com/2018/02/2-api.html

        //TODO 이거 할라면 php로 서버 만들고 해야한다고 함


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MyQrActivity.class);
                //TODO outIntent.putExtra("Logo", ) 로고는 어떻게 받아오지?
                outIntent.putExtra("QR name", name.getText().toString());
                outIntent.putExtra("Detail address", detailAddress.getText().toString());
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }
}