package com.example.qrlo.Signup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrlo.R;


public class Signup2 extends Fragment {


    FragmentManager fragmentManager;
    Button NextButton1;
    CheckBox AllCheck;
    CheckBox LocationCheck;
    CheckBox MarketingCheck;

    Fragment Signup3;
    String LocationAgree;
    String MarketingAgree;

    String TAG  =  "Signup2 ";


    Bundle bundle;
    public Signup2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_signup2, container, false);





        Signup3 = new Signup3();
        AllCheck = v.findViewById(R.id.AllCheck);
        LocationCheck = v.findViewById(R.id.LocationCheck);
        MarketingCheck = v.findViewById(R.id.MarketingCheck);
        NextButton1 = v.findViewById(R.id.NextButton1);
        fragmentManager = getActivity().getSupportFragmentManager();

        MarketingAgree = "DisAgree";
        LocationAgree = "DisAgree";

        AllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(AllCheck.isChecked()) {
                    LocationCheck.setChecked(true);
                    LocationAgree = "Agree";
                    MarketingCheck.setChecked(true);
                    MarketingAgree = "Agree";

                }
                else
                {
                    LocationCheck.setChecked(false);
                    LocationAgree = "DisAgree";
                    MarketingCheck.setChecked(false);
                    MarketingAgree = "DisAgree";



                }



            }
        });


        LocationCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(LocationCheck.isChecked())
                {
                    LocationAgree = "Agree";
                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
                else
                {
                    if(MarketingCheck.isChecked())
                    {
                        AllCheck.setChecked(false);
                        MarketingCheck.setChecked(true);
                    }

                    LocationAgree = "false";
                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
            }
        });
        MarketingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MarketingCheck.isChecked())
                {
                    MarketingAgree = "Agree";

                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
                else
                {

                    if(LocationCheck.isChecked())
                    {
                        AllCheck.setChecked(false);
                        LocationCheck.setChecked(true);
                    }
                    MarketingAgree = "false";
                    Log.d(TAG ,"LocationAgree = " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
                }
            }
        });

        Log.d(TAG ,"Last " + LocationAgree + "  " + "MarketingAgree = " + MarketingAgree);
        NextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!LocationCheck.isChecked())
                {
                    Toast.makeText(getActivity(), "위치 기반 서비스 이용약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frameLayout, Signup3).commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("LocationAgree", LocationAgree);
                    bundle.putString("MarketingAgree", MarketingAgree);
                    Signup3.setArguments(bundle);

                }

            }
        });
















        return v;
    }
}