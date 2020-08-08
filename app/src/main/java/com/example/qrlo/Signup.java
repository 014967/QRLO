package com.example.qrlo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.Hashtable;
import java.util.Map;

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
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser User;
    String stuid;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");

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


                mAuth.createUserWithEmailAndPassword(stEmail, stPassWord)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(Signup.this, "회원가입 성공!",
                                            Toast.LENGTH_SHORT).show();
                                    User = firebaseAuth.getCurrentUser();
                                    stuid = User.getUid();
                                    registerUser(stEmail,stPassWord,stName);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Signup.this, "회원가입 실패!",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }



                            }
                        });


                Intent in = new Intent(Signup.this, MainActivity.class);
                in.putExtra("stEmail", stEmail);
                in.putExtra("stPassWord", stPassWord);
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    public void registerUser(String id, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete." + task.isSuccessful());


                        if (!task.isSuccessful()) {
                            //Toast.makeText(MainActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(MainActivity.this,"Authentication success", Toast.LENGTH_SHORT).show();

                            if (User != null) {

                                myRef.child(stuid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Hashtable<String, String> profile = new Hashtable<>();
                                        profile.put("email", User.getEmail());
                                        profile.put("name", stName);
                                        profile.put("birth", String.valueOf(mYear + "/" + mMonth + "/" + mDay));
                                        profile.put("location agree", "");
                                        profile.put("marketing agree", "");
                                        myRef.child(stuid).setValue(profile);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }
                    }
                });


    }
}