package com.example.qrlo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyQrActivity extends Activity {

    ImageView btnadd;
    RecyclerView recyclerView;
    my_qr_adapter adapter;
    ArrayList<my_qr_item> mList = new ArrayList<my_qr_item>();
    private static final int QR_CREATE = 10002;
    private static final int ITEM_SELECT = 10003;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        recyclerView = findViewById(R.id.my_qr_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new my_qr_adapter(mList);
        recyclerView.setAdapter(adapter);

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
                intent.putExtra("QR name", item.getTitle());
                intent.putExtra("Address", item.getAddress());
                intent.putExtra("Detail address", item.getDetailAddress());
                intent.putExtra("Phone number", item.getPhone());
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
                    String title = data.getStringExtra("QR name");
                    String desc = data.getStringExtra("Detail address");
                    boolean temp = data.getBooleanExtra("Temperature", false);
                    byte[] byteArray = data.getByteArrayExtra("Logo");
                    String address = data.getStringExtra("Address");
                    String phone = data.getStringExtra("Phone number");
                    Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    my_qr_item item = new my_qr_item();
                    item.setIcon(image);
                    item.setTitle(title);
                    item.setAddress(address);
                    item.setDetailAddress(desc);
                    item.setTemp(temp);
                    item.setPhone(phone);

                    mList.add(item);
                    adapter.notifyDataSetChanged();
                }
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
        }
    }

}
