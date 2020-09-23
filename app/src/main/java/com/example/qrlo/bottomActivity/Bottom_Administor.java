package com.example.qrlo.bottomActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qrlo.CreateQrActivity;
import com.example.qrlo.MyQrInfo;
import com.example.qrlo.R;
import com.example.qrlo.RecyclerDecoration;
import com.example.qrlo.my_qr_adapter;
import com.example.qrlo.my_qr_item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Bottom_Administor extends Fragment {


    public Bottom_Administor()
    {

    }
    //Button adminBtn;
    ImageView btnadd;
    RecyclerView recyclerView;
    my_qr_adapter adapter;
    ArrayList<my_qr_item> mList = new ArrayList<my_qr_item>();
    private static final int QR_CREATE = 10002;
    private static final int QR_INFO = 10003;
    Integer pos;
    Bundle bundle;
    boolean getArg = true;
    Integer posChg = -1;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_my_qr, container, false);


        recyclerView = v.findViewById(R.id.my_qr_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new my_qr_adapter(mList);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(30);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setAdapter(adapter);
/*
        mList.clear();
        adapter.notifyDataSetChanged();
        databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                my_qr_item item = snapshot.getValue(my_qr_item.class);

                mList.add(item);
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

        adminBtn = v.findViewById(R.id.myQR_adminBtn);
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Bottom_Admin.class);
                getActivity().startActivity(intent);
            }
        });

 */

        btnadd = v.findViewById(R.id.myQR_addBtn);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateQrActivity.class);
                getActivity().startActivityForResult(intent, QR_CREATE);
            }
        });

        adapter.setOnItemClickListener(new my_qr_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), MyQrInfo.class);
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

                getActivity().startActivityForResult(intent, QR_INFO);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case QR_INFO:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getActivity(), "aweoirhjaerwioawerji", Toast.LENGTH_SHORT).show();
                    mList.remove(pos);
                    adapter.notifyDataSetChanged();
                }

                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
        databaseReference.child("user").child(firebaseUser.getUid()).child("myQR").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                my_qr_item item = snapshot.getValue(my_qr_item.class);

                mList.add(item);
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
/*
        if(getArguments()!=null && posChg!=getArguments().getInt("Position")) {
            bundle =getArguments();
            Toast.makeText(getActivity(), "bbbbbbbbbbb", Toast.LENGTH_SHORT).show();
            mList.remove(bundle.getInt("Position"));
            adapter.notifyDataSetChanged();
            posChg = getArguments().getInt("Position");
        }

 */

    }
}