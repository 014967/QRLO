package com.example.qrlo.Signup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrlo.After_Login;
import com.example.qrlo.MainActivity;
import com.example.qrlo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


public class SignupOtp extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseUser User;
    String mAuthVerificationId;
    private Bundle bundle;
    EditText OTPtext;
    Button Verify_otp;
    Fragment Signup2;
    FragmentManager fragmentManager;

    public SignupOtp() {
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_signup_otp, container, false);



        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        bundle = getArguments();
        fragmentManager = getActivity().getSupportFragmentManager();
        Signup2 = new Signup2();

        OTPtext= v.findViewById(R.id.otpText);
        Verify_otp = v.findViewById(R.id.Verify_Otp);
        mAuthVerificationId =bundle.getString("AuthCredentials");




        Verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = OTPtext.getText().toString();

                if (otp.isEmpty() || otp.length() < 6)
                {
                    Toast.makeText(getActivity(), " 인증번호를 입력해주세요 ", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                    signInWithPhoneAuthCredential(credential );
                }
            }
        });











        return v;
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                   sendUserToHome();
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {

                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(User!=null)
        {
            sendUserToHome();
        }
    }

    public void sendUserToHome()
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, Signup2).commit();
    }



}