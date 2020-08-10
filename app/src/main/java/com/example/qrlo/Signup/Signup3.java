package com.example.qrlo.Signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_PRIVATE;


public class Signup3 extends Fragment {
    private int mYear = 0, mMonth = 0, mDay = 0;

    Button Finish;
    FragmentManager fragmentManager;
    Bundle bundle;

    EditText etName;
    String stName;
    String LocationAgree;
    String MarketingAgree;
    String Birth;

    String stuid;




    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    String TAG = "Signup3 =" ;



    public Signup3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_signup3, container, false);

        Finish = v.findViewById(R.id.Finish);
        etName = v.findViewById(R.id.etName);
        fragmentManager = getActivity().getSupportFragmentManager();


        Calendar calendar = new GregorianCalendar();

        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker datePicker = v.findViewById(R.id.DP);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);







        bundle = getArguments();
        if(bundle!=null)
        {
            LocationAgree = bundle.getString("LocationAgree");
            MarketingAgree = bundle.getString("MarketingAgree");


        }


        Birth = mYear+"/"+mMonth+"/"+mDay;

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
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email", MODE_PRIVATE); // Mode_private = 해당액티비티에서만 접근가능
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", user.getUid());
                    editor.putString("email", user.getEmail());
                    editor.apply(); //SharedPreerence를 통해 정보를 저장


                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stuid = user.getUid();
                stName = etName.getText().toString();
                if(etName.getText().equals(""))
                {
                    Toast.makeText(getActivity(), " 이름을 입력해 주세요 ", Toast.LENGTH_SHORT).show();
                }
                myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String, Object> profile = new HashMap<String, Object>();
                        profile.put("uid",user.getUid());
                        profile.put("name", stName);
                        profile.put("birth", Birth);
                        profile.put("LocationAgree" , LocationAgree);
                        profile.put("MarketingAgree", MarketingAgree);

                        myRef.child(stuid).setValue(profile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                Intent in = new Intent(getActivity(), After_Login.class);
                startActivity(in);
                getActivity().finish();





            }
        });






        return v;
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