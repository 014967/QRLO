package com.example.qrlo.bottomActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrlo.After_Login;
import com.example.qrlo.R;

public class Bottom_Administor_SMS3 extends AppCompatActivity {

    Button mainBtn, sendBtn;
    EditText phone, msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__administor_sms3);

        mainBtn = findViewById(R.id.mainBtn);
        sendBtn = findViewById(R.id.sendBtn);
        phone = findViewById(R.id.admin_qr_phone_edit);
        msg = findViewById(R.id.admin_qr_msg_edit);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), After_Login.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone.getText().toString();
                String sms = msg.getText().toString();

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "전송완료", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Log.d("sms", "SMS");
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
