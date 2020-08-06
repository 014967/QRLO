package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;


import com.example.qrlo.bottomActivity.Bottom_Administor;
import com.example.qrlo.bottomActivity.Bottom_History;
import com.example.qrlo.bottomActivity.Bottom_Home;
import com.example.qrlo.bottomActivity.Bottom_Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class After_Login extends AppCompatActivity {

    Fragment Bottom_History;
    Fragment Bottom_Home;
    Fragment Bottom_Setting;
    Fragment Bottom_Administor;

    FrameLayout frameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = (FrameLayout)findViewById(R.id.navigation_frame);
        Bottom_Home = new Bottom_Home();
        Bottom_History = new Bottom_History();
        Bottom_Administor = new Bottom_Administor();
        Bottom_Setting = new Bottom_Setting();







        bottomNavigationView.setSelectedItemId(R.id.navigation_history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId())
                {

                    case  R.id.navigation_home :

                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Home );
                        fragmentTransaction.commit();

                        return true;

                    case R.id.navigation_history :

                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_History );
                        fragmentTransaction.commit();

                        return true;
                    case R.id.navigation_setting :

                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Setting );
                        fragmentTransaction.commit();


                        return true;
                    case R.id.navigation_administor :

                        fragmentTransaction.replace(R.id.navigation_frame, Bottom_Administor );
                        fragmentTransaction.commit();

                        return true;


                }
                return false;
            }
        });




    }


    private void setFragment(Fragment fragment)
    {

    }


}
