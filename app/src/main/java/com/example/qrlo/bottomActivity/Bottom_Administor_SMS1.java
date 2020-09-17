package com.example.qrlo.bottomActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;

public class Bottom_Administor_SMS1 extends AppCompatActivity {

    EditText name;
    Button search;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__administor_sms1);

        name = findViewById(R.id.admin_qr_name_edit);
        search = findViewById(R.id.admin_qr_search_btn);
        recyclerView = findViewById(R.id.admin_qr_sms1_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }
}
