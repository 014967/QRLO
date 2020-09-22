package com.example.qrlo.Signup;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrlo.MainActivity;
import com.example.qrlo.R;
import com.facebook.all.All;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Signup1 extends Fragment  {




    FragmentManager fragmentManager;
    Button GenerateButton;

    EditText CountryNum;
    EditText PhoneNum;
    Fragment SignupOtp;

    FirebaseUser User;
    FirebaseAuth firebaseAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    String TAG = "Signup1 = " ;


    public Signup1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup1, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        firebaseAuth = FirebaseAuth.getInstance();
        User = firebaseAuth.getCurrentUser();
        CountryNum = v.findViewById(R.id.etNum);
        PhoneNum = v.findViewById(R.id.etPhone);
        GenerateButton = v.findViewById(R.id.GenerateOTP);

        SignupOtp = new SignupOtp();

        GenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CountryCode = CountryNum.getText().toString();
                String PhoneNumber = PhoneNum.getText().toString();

                String Complete_phone_number = "+" + CountryCode + PhoneNumber;


                if(CountryCode.isEmpty() || PhoneNumber.isEmpty())
                {
                    Toast.makeText(getActivity(), " 국가 번호와 휴대폰번호를 입력해주세요 ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            Complete_phone_number,
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,
                            (AppCompatActivity)getActivity(),   // Unit of timeout               // Activity (for callback binding)
                            mCallBack
                    );

                }
            }
        });


        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout, SignupOtp).commit();
                Bundle bundle = new Bundle();
                bundle.putString("AuthCredentials", s);
                SignupOtp.setArguments(bundle);

            }
        };






        return v;
    }

    @Override
    public void onStart() {

        super.onStart();
        if(User !=null)
        {
            Intent homeIntent  = new Intent(getActivity() , MainActivity.class);
            startActivity(homeIntent);
            getActivity().finish();
        }
    }



}