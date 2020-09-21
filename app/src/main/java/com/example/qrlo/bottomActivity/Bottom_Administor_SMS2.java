package com.example.qrlo.bottomActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Bottom_Administor_SMS2 extends AppCompatActivity {

    RecyclerView recyclerView;
    Button sendto;
    ArrayList<bottom_item2> mList = new ArrayList<bottom_item2>();
    bottom_adapter2 adapter;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__administor_sms2);

        sendto = findViewById(R.id.admin_qr_sendto_btn);
        recyclerView = findViewById(R.id.admin_qr_sms2_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new bottom_adapter2(mList);
        recyclerView.setAdapter(adapter);
        mList.clear();

        Intent inIntent = getIntent();
        String phone = inIntent.getStringExtra("Phone");

        Query query = databaseReference.child("user").orderByChild("email").equalTo(phone);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    bottom_item2 item = new bottom_item2();
                    item.setPhoneStr(dataSnapshot.child("history").child("where").getValue(String.class));
                    Toast.makeText(getApplicationContext(), dataSnapshot.child("history").child("where").getValue(String.class), Toast.LENGTH_SHORT).show();
                    //item.setPhoneStr(dataSnapshot.child("email").getValue(String.class));
                    mList.add(item);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);

        sendto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bottom_Administor_SMS3.class);
                startActivity(intent);
            }
        });
    }
}
