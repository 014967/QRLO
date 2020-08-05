package com.example.qrlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class After_Login extends AppCompatActivity {
    Button btn_myQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);

        btn_myQR = findViewById(R.id.btn_myQR);

        btn_myQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyQrActivity.class);
                startActivity(intent);
            }
        });

    }
}
