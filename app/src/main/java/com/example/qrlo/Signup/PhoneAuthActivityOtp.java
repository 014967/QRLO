package com.example.qrlo.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.qrlo.After_Login;
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

public class PhoneAuthActivityOtp extends AppCompatActivity {

    EditText OtpNum;
    String stOtpNum;
    Button Verify_OtpNum;
    ProgressBar OtpProBar;
    String verificationId;
    String number;
    String TAG = "PhoneAuthActivityOtp";

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_otp);


        OtpNum = (EditText)findViewById(R.id.otpNum);
        Verify_OtpNum = (Button)findViewById(R.id.Verify_Otpnum);
        OtpProBar = (ProgressBar)findViewById(R.id.OtpProBar);
        mAuth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");
        number = getIntent().getStringExtra("number");
        Verify_OtpNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtpProBar.setVisibility(View.VISIBLE);
                if(!OtpNum.equals("")) {
                    stOtpNum = OtpNum.getText().toString();
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

                            FirebaseUser user = task.getResult().getUser();
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
        Intent in  = new Intent(PhoneAuthActivityOtp.this , After_Login.class);
        startActivity(in);
        finish();

    }
}