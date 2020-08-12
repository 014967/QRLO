package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.FrameLayout;


import com.example.qrlo.bottomActivity.Bottom_Administor;
import com.example.qrlo.bottomActivity.Bottom_History;
import com.example.qrlo.bottomActivity.Bottom_Home;
import com.example.qrlo.bottomActivity.Bottom_Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;


public class After_Login extends AppCompatActivity {



    FragmentManager fragmentManager;
    Fragment Bottom_History;
    Fragment Bottom_Home;
    Fragment Bottom_Setting;
    Fragment Bottom_Administor;

    FrameLayout frameLayout;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);



        AnimatedBottomBar animatedBottomBar = (AnimatedBottomBar)findViewById(R.id.bottom_navigation);
        frameLayout = (FrameLayout)findViewById(R.id.navigation_frame);
        Bottom_Home = new Bottom_Home();
        Bottom_History = new Bottom_History();
        Bottom_Administor = new Bottom_Administor();
        Bottom_Setting = new Bottom_Setting();
        fragmentManager =getSupportFragmentManager();




        fragmentTransaction =getSupportFragmentManager().beginTransaction();


        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Home );
        fragmentTransaction.commit();


        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab lasTab, int i1, @NotNull AnimatedBottomBar.Tab newTab) {
                switch(newTab.getId())
                {
                    case  R.id.navigation_home :
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Home );
                        fragmentTransaction.commit();

                        break;

                    case R.id.navigation_history :
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_History );
                        fragmentTransaction.commit();

                        break;
                    case R.id.navigation_setting :
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Setting );
                        fragmentTransaction.commit();


                        break;
                    case R.id.navigation_administor :
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Administor );
                        fragmentTransaction.commit();

                        break;
                }

            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });




    }


    private void setFragment(Fragment fragment)
    {

    }


}
