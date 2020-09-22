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
import com.example.qrlo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
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

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {




    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    EditText etCtyNum;
    EditText etPhoneNum;
    Button OpenOTP;
    ProgressBar waitingBar;
    String TAG = "PhoneAuthActivity";


    String CtyNum;
    String PhoneNum;
    String FullNum;
    String extraNum;




    String LocationAgree;
    String MarketingAgree;
    String name;
    String birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);


        Intent in = getIntent();

        LocationAgree = in.getStringExtra("LocationAgree");
        MarketingAgree = in.getStringExtra("MarketingAgree");
        name = in.getStringExtra("name");
        birth = in.getStringExtra("birth");


        etCtyNum = (EditText)findViewById(R.id.etCtyNum);
        etPhoneNum = (EditText)findViewById(R.id.etPhoneNum);
        OpenOTP = (Button)findViewById(R.id.OpenOtp);
        waitingBar = (ProgressBar)findViewById(R.id.waitingBar);
        mAuth = FirebaseAuth.getInstance();



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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




        OpenOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CtyNum = etCtyNum.getText().toString();
                PhoneNum = etPhoneNum.getText().toString();
                if(etCtyNum.equals("") ||CtyNum.length()==0 )
                {
                    Toast.makeText(PhoneAuthActivity.this, "국가번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                if(etPhoneNum.equals(""))
                {
                    Toast.makeText(PhoneAuthActivity.this, "휴대폰번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                FullNum = "+"+CtyNum+PhoneNum;
                AttemptAuth(FullNum);
            }
        });

    }

    private void AttemptAuth(String fullnum)
    {
        extraNum = fullnum;
        waitingBar.setVisibility(View.VISIBLE);
        OpenOTP.setText("기다려주세요");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fullnum,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.d(TAG, "onVerificationCompleted:" + credential);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
               Toast.makeText(PhoneAuthActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

            Intent intent = new Intent(PhoneAuthActivity.this , PhoneAuthActivityOtp.class);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("number", extraNum);
            intent.putExtra("LocationAgree", LocationAgree);
            intent.putExtra("MarketingAgree", MarketingAgree);
            intent.putExtra("name",name);
            intent.putExtra("birth",birth);
            startActivity(intent);
            finish();
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
         //   mVerificationId = verificationId;
         //   mResendToken = token;

            // ...
        }
    };

    private void SendToHomeActivity() {

        Intent in  = new Intent(PhoneAuthActivity.this , After_Login.class);
        startActivity(in);
        finish();




    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            SendToHomeActivity();
                            FirebaseUser user = task.getResult().getUser();



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

}