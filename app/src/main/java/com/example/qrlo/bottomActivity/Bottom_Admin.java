package com.example.qrlo.bottomActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrlo.R;

public class Bottom_Admin extends AppCompatActivity {

    Button confirmed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_admin);

        confirmed = findViewById(R.id.admin_qr_confirmed_btn);

        confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bottom_Administor_SMS1.class);
                startActivity(intent);
            }
        });
    }
}
