/*package com.example.qrlo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyQrActivity extends Activity {

    ImageView btnadd;
    RecyclerView recyclerView;
    my_qr_adapter adapter;
    ArrayList<my_qr_item> mList = new ArrayList<my_qr_item>();

    String tag = "tag = ";
    private static final int QR_CREATE = 10002;
    private static final int QR_INFO = 10003;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        recyclerView = findViewById(R.id.my_qr_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new my_qr_adapter(mList);
        recyclerView.setAdapter(adapter);


        databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                my_qr_item item = snapshot.getValue(my_qr_item.class);

                mList.add(item);

                Log.d(tag , "mlist = " + mList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        btnadd = findViewById(R.id.myQR_addBtn);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateQrActivity.class);
                startActivityForResult(intent, QR_CREATE);
            }
        });

        adapter.setOnItemClickListener(new my_qr_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(), MyQrInfo.class);
                my_qr_item item = mList.get(pos);
                intent.putExtra("QR name", item.getTitle());
                intent.putExtra("ImageURL", item.getIconURI());
                intent.putExtra("Address", item.getAddress());
                intent.putExtra("Detail address", item.getDetailAddress());
                intent.putExtra("Temperature", item.getTemp());
                intent.putExtra("Phone number", item.getPhone());
                intent.putExtra("Position", pos);
                intent.putExtra("Key", item.getKey());
                intent.putExtra("IconName", item.getIconName());

                startActivityForResult(intent, QR_INFO);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case QR_CREATE:
                if(resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "생성되었습니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case QR_INFO:
                if(resultCode == RESULT_OK) {

                }
                break;
        }
    }

}

 */
