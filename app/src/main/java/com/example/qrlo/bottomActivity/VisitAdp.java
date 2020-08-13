package com.example.qrlo.bottomActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VisitAdp extends RecyclerView.Adapter<VisitAdp.ViewHolder> {

    ArrayList<String> arrayListVisit;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth mAuth;
    public VisitAdp(ArrayList<String> arrayListVisit)
    {
        this.arrayListVisit = arrayListVisit;
    }


    @NonNull
    @Override
    public VisitAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_visit,parent,false);




        return new VisitAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitAdp.ViewHolder holder, int position) {



        database =FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("users"); // DB테이블 연결 작업
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


    }

    @Override
    public int getItemCount() {
        return arrayListVisit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView VisitName;
        TextView VisitTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            VisitName = itemView.findViewById(R.id.VisitName);
            VisitTime = itemView.findViewById(R.id.VisitTime);
        }
    }
}
