package com.example.qrlo.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrlo.After_Login;
import com.example.qrlo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class PhoneAuthName extends AppCompatActivity {
    private int mYear = 0, mMonth = 0, mDay = 0;

    Button Finish;

    EditText etName;
    String stName;
    String LocationAgree;
    String MarketingAgree;
    String Birth;

    String stuid;
    String TAG = "PhoneAuthName";

    String number;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_auth_name);


    //    Intent in = getIntent();
    //    number = in.getStringExtra("number");
   //     Log.d(TAG, "number" + number);
        Finish = findViewById(R.id.Finish);
        etName = findViewById(R.id.etName);


        Calendar calendar = new GregorianCalendar();

        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker datePicker = findViewById(R.id.DP);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);



     //   LocationAgree = in.getStringExtra("LocationAgree");
      //  MarketingAgree = in.getStringExtra("MarketingAgree");


        Birth = mYear+"/"+mMonth+"/"+mDay;




        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                stName = etName.getText().toString();
                if(etName.getText().equals(""))
                {
                    Toast.makeText(PhoneAuthName.this, " 이름을 입력해 주세요 ", Toast.LENGTH_SHORT).show();
                }




                    Intent in = new Intent(PhoneAuthName.this, PhoneAuthAgree.class);
                    in.putExtra("name", stName);
                    in.putExtra("birth", Birth);
                    startActivity(in);
                    finish();








            }
        });



    }
    DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener() {

        @Override

        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {

            mYear = yy;

            mMonth = mm;

            mDay = dd;

        }

    };
}