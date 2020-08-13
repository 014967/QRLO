package com.example.qrlo.bottomActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrlo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Bottom_History extends Fragment {


    RecyclerView rvGroup;
    RecyclerView.Adapter Adapter;

    RecyclerView.LayoutManager manager;
    DateInfo dateInfo;
    String TAG = "RECYCLER : ";
    String date;
    ArrayList<DateInfo> ArrayDate;
    ArrayList<VisitInfo> visitInfos;

    //GroupAdp adapterGroup;
     FirebaseDatabase database;
     DatabaseReference databaseReference;
     FirebaseUser user;
     FirebaseAuth mAuth;

    public Bottom_History()
    {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_bottom__history, container, false);


        manager = new LinearLayoutManager(getActivity());
        rvGroup = v.findViewById(R.id.rvGroup);
        rvGroup.setLayoutManager(manager);

        ArrayDate = new ArrayList<DateInfo>();
        visitInfos = new ArrayList<VisitInfo>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Adapter = new DateInfoAdp(ArrayDate,getActivity());




       databaseReference.child(user.getUid()).child("history").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    DateInfo dateInfo =snapshot.getValue(DateInfo.class);

                    Log.d(TAG, "date Info  = " + dateInfo.getWhen());
                    ArrayDate.add(dateInfo);







                }

                rvGroup.setAdapter(Adapter);
                Adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return v;

    }

    private String getDate(long time)
    {
        date = DateFormat.format("dd-MM-yyyy", time).toString();
        return date;
    }
}