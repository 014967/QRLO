package com.example.qrlo.bottomActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrlo.R;
import com.example.qrlo.RecyclerDecoration;
import com.example.qrlo.my_qr_adapter;
import com.example.qrlo.my_qr_item;

import java.util.ArrayList;

public class Bottom_Notice extends Fragment {

    RecyclerView recyclerView;
    bottom_notice_adapter adapter;
    ArrayList<bottom_notice_item> mList = new ArrayList<bottom_notice_item>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_bottom__notice, container, false);
        recyclerView = v.findViewById(R.id.bottom_notice_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new bottom_notice_adapter(mList);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(30);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setAdapter(adapter);
        mList.clear();

        return v;
    }
}
