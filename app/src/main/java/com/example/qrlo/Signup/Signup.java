package com.example.qrlo.Signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.qrlo.MainActivity;
import com.example.qrlo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

public class Signup extends AppCompatActivity {
    private int mYear = 0, mMonth = 0, mDay = 0;
    Button assignButton;
    EditText email;
    EditText password;
    EditText name;
    String stEmail;
    String stPassWord;
    String stName;
    String TAG = "SignUp.class";

    String stuid;


    Fragment Signup1;
    FrameLayout frameLayout;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        frameLayout = findViewById(R.id.frameLayout);
        Signup1 = new Signup1();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, Signup1).commit();


/*
        database =FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE); // Mode_private = 해당액티비티에서만 접근가능
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", user.getUid());
                    editor.putString("email", user.getEmail());
                    editor.apply(); //SharedPreerence를 통해 정보를 저장


                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        assignButton = findViewById(R.id.assignBtn);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.PassWord);
        name = findViewById(R.id.Name);



        Calendar calendar = new GregorianCalendar();

        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePicker datePicker = findViewById(R.id.DP);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);


        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = email.getText().toString();
                stPassWord = password.getText().toString();
                stName = name.getText().toString();

                if(stEmail.isEmpty() || stEmail.equals("") ||stPassWord.isEmpty() || stPassWord.equals("")||stName.isEmpty() ||stName.equals(""))
                {
                    Toast.makeText(Signup.this, "이름과 이메일,비밀번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerUser(stEmail,stPassWord,stName);
                }






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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);




    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }



    }



    private void updateUI(FirebaseUser currentUser) {
    }

  */
    }
}