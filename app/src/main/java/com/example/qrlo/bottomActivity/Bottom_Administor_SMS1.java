package com.example.qrlo.bottomActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Bottom_Administor_SMS1 extends AppCompatActivity {

    EditText name;
    Button search;
    RecyclerView recyclerView;
    ArrayList<bottom_item1> mList = new ArrayList<bottom_item1>();
    bottom_adapter1 adapter;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__administor_sms1);

        name = findViewById(R.id.admin_qr_name_edit);
        search = findViewById(R.id.admin_qr_search_btn);
        recyclerView = findViewById(R.id.admin_qr_sms1_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new bottom_adapter1(mList);
        recyclerView.setAdapter(adapter);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.clear();
                adapter.notifyDataSetChanged();
                String search_name = name.getText().toString();

                Query query = databaseReference.child("user").orderByChild("name").equalTo(search_name);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            bottom_item1 item = new bottom_item1();
                            item.setNameStr(dataSnapshot.child("name").getValue(String.class));
                            item.setPhoneStr(dataSnapshot.child("email").getValue(String.class));
                            mList.add(item);

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        adapter.setOnItemClickListener(new bottom_adapter1.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(), Bottom_Administor_SMS2.class);
                bottom_item1 item = mList.get(pos);
                intent.putExtra("Phone", item.getPhoneStr());
                startActivity(intent);
            }
        });
    }
}
