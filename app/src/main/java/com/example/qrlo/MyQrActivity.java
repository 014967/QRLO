package com.example.qrlo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MyQrActivity extends Activity {

    ImageView btnadd;
    RecyclerView recyclerView;
    my_qr_adapter adapter;
    ArrayList<my_qr_item> mList = new ArrayList<my_qr_item>();
    private static final int QR_CREATE = 10002;
    private static final int ITEM_SELECT = 10003;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

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
                my_qr_item item = new my_qr_item();
                item = mList.get(pos);
                Bitmap bitmap = item.getIcon();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(getApplicationContext(), MyQrInfo.class);
                intent.putExtra("Logo", byteArray);
                intent.putExtra("ImageURL", item.getIconURI());
                intent.putExtra("QR name", item.getTitle());
                intent.putExtra("Address", item.getAddress());
                intent.putExtra("Detail address", item.getDetailAddress());
                intent.putExtra("Phone", item.getPhone());
                intent.putExtra("Temperature", item.getTemp());
                intent.putExtra("Position", pos);
                startActivityForResult(intent, ITEM_SELECT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case QR_CREATE:
                if(resultCode == RESULT_OK) {

                }
                break;
            case ITEM_SELECT:
                if(resultCode == RESULT_OK) {
                    String title = data.getStringExtra("QR name");
                    String desc = data.getStringExtra("Detail address");
                    boolean temp = data.getBooleanExtra("Temperature", false);
                    byte[] byteArray = data.getByteArrayExtra("Logo");
                    String address = data.getStringExtra("Address");
                    String phone = data.getStringExtra("Phone number");
                    int pos = data.getIntExtra("Position", 0);
                    Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    my_qr_item item = new my_qr_item();
                    item = mList.get(pos);
                    item.setIcon(image);
                    item.setTitle(title);
                    item.setAddress(address);
                    item.setDetailAddress(desc);
                    item.setTemp(temp);
                    item.setPhone(phone);

                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

}
