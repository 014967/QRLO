package com.example.qrlo.Signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qrlo.After_Login;
import com.example.qrlo.MainActivity;
import com.example.qrlo.R;
import com.example.qrlo.bottomActivity.DateInfoAdp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PhoneAuthActivityOtp extends AppCompatActivity {

    EditText OtpNum;
    String stOtpNum;
    Button Verify_OtpNum;
    ProgressBar OtpProBar;
    String verificationId;
    String number;
    String TAG = "PhoneAuthActivityOtp";


    FirebaseDatabase database;
    String stuid;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    String LocationAgree;
    String MarketingAgree;
    String birth;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_otp);


        OtpNum = (EditText)findViewById(R.id.otpNum);
        Verify_OtpNum = (Button)findViewById(R.id.Verify_Otpnum);
        OtpProBar = (ProgressBar)findViewById(R.id.OtpProBar);
        mAuth = FirebaseAuth.getInstance();
        Intent in = getIntent();
        verificationId = in.getStringExtra("verificationId");
        number =in.getStringExtra("number");
        LocationAgree =in.getStringExtra("LocationAgree");
        MarketingAgree= in.getStringExtra("MarketingAgree");
        birth = in.getStringExtra("birth");
        name = in.getStringExtra("name");




        database = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", user.getUid());
                    editor.putString("email",user.getEmail());

                    editor.apply();


                }
                else
                {

                }
            }
        };


        Verify_OtpNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtpProBar.setVisibility(View.VISIBLE);
                if(!OtpNum.equals("")) {
                    stOtpNum = OtpNum.getText().toString();
                }
                else
                {
                    Toast.makeText(PhoneAuthActivityOtp.this, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, stOtpNum);
                signInWithPhoneAuthCredential(credential);

            }
        });





    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            user = task.getResult().getUser();
                            SendToHomeActivity();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void SendToHomeActivity() {

        myRef = database.getReference("user");
        stuid = user.getUid();
        myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> profile = new HashMap<String, Object>();
                profile.put("uid",user.getUid());
                profile.put("name", name);
                profile.put("birth", birth);
                profile.put("LocationAgree" , LocationAgree);
                profile.put("MarketingAgree", MarketingAgree);
                profile.put("number",number);

                myRef.child(stuid).setValue(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Intent in = new Intent(PhoneAuthActivityOtp.this, After_Login.class);
        startActivity(in);
        finish();

    }
}