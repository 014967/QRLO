package com.example.qrlo.bottomActivity;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;

public class Bottom_Administor_SMS2 extends AppCompatActivity {

    RecyclerView recyclerView;
    Button sendto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__administor_sms2);

        sendto = findViewById(R.id.admin_qr_sendto_btn);
        recyclerView = findViewById(R.id.admin_qr_sms2_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}
