package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;

import android.widget.FrameLayout;


import com.example.qrlo.bottomActivity.Bottom_Administor;
import com.example.qrlo.bottomActivity.Bottom_History;
import com.example.qrlo.bottomActivity.Bottom_Home;
import com.example.qrlo.bottomActivity.Bottom_Notice;
import com.example.qrlo.bottomActivity.Bottom_Setting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import nl.joery.animatedbottombar.AnimatedBottomBar;


public class After_Login extends AppCompatActivity {

    final int OLD_HISTORY_DEL_DATE = 21;    // 21일이 지난 기록은 삭제

    FragmentManager fragmentManager;
    Fragment Bottom_History;
    Fragment Bottom_Home;
    Fragment Bottom_Setting;
    Fragment Bottom_Administor;
    Fragment Bottom_Notice;

    FrameLayout frameLayout;
    FragmentTransaction fragmentTransaction;

    private static final int QR_CREATE = 10002;
    private static final int QR_INFO = 10003;

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
        Bottom_Notice = new Bottom_Notice();
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


                    case R.id.navigation_administor :
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Administor );
                        fragmentTransaction.commit();

                        break;

                    case R.id.navigation_notice :
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Notice );
                        fragmentTransaction.commit();

                        break;

                }

            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });


        deleteOldHistory();     // 앱 시작하면 자신의 3주 지난 기록은 삭제됨

    }


    private void setFragment(Fragment frag)
    {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


    private void deleteOldHistory(){
        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(OLD_HISTORY_DEL_DATE, TimeUnit.DAYS);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = auth.getUid();

        if( userId != null){
            DatabaseReference ref = database.getReference().child(userId).child("history");
            Query oldItems = ref.orderByChild("when").endAt(cutoff);
            oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot item : snapshot.getChildren()){
                        item.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

}
