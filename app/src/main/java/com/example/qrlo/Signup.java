package com.example.qrlo;

import android.content.Intent;
import android.content.SharedPreferences;
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
    FirebaseUser user;
    String stuid;



    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


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

    public void registerUser(String id, String password, final String name)
    {
        mAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete." + task.isSuccessful());


                        if(!task.isSuccessful())
                        {
                            Toast.makeText(Signup.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Signup.this,"Authentication success", Toast.LENGTH_SHORT).show();




                            Log.d(TAG, "USER = " + user);
                            Log.d(TAG, "USER = " + user.getUid());

                            if(user!=null) {

                                Hashtable<String, String> profile = new Hashtable<>();
                                profile.put("email", user.getEmail());
                                profile.put("nickname",name);
                                profile.put("age", "");
                                stuid = user.getUid();
                                myRef.child(stuid).setValue(profile);
                            }

                            Intent in = new Intent(Signup.this, MainActivity.class);
                            in.putExtra("stEmail", stEmail);
                            in.putExtra("stPassWord", stPassWord);
                            startActivity(in);

                        }
                    }
                });




    }

}