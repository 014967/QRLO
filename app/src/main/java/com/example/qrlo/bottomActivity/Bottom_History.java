package com.example.qrlo.bottomActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.qrlo.After_Login;
import com.example.qrlo.MyQrActivity;
import com.example.qrlo.R;
public class Bottom_History extends Fragment {


    public Bottom_History()
    {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

















        return inflater.inflate(R.layout.activity_bottom__administor, container, false);
    }
}