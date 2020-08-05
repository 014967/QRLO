package com.example.qrlo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyQrActivity extends Activity {

    ImageButton btnadd;
    RecyclerView recyclerView;
    my_qr_adapter adapter;
    ArrayList<my_qr_item> mList = new ArrayList<my_qr_item>();


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
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            //이 자리에 로고도 받아와야함

            String title = data.getStringExtra("QR name");
            String desc = data.getStringExtra("Detail address");

            my_qr_item item = new my_qr_item();
            Drawable defaultImage = getResources().getDrawable(R.drawable.ic_baseline_add_24);
            item.setIcon(defaultImage);
            item.setTitle(title);
            item.setDesc(desc);

            mList.add(item);
            adapter.notifyDataSetChanged();
        }
    }

}
