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
import android.widget.Toast;

import com.example.qrlo.MainActivity;
import com.example.qrlo.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Bottom_Setting extends Fragment {
    Button Logout;
    private GoogleApiClient mGoogleApiClient;

    LoginManager loginManager;
    public Bottom_Setting()
    {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_bottom__setting, container, false);
        Logout = v.findViewById(R.id.Logout);

        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                disconnectFromFacebook();
             //   disconnectGoogle();
                Intent i=new Intent(getContext(), MainActivity.class);
                startActivity(i);

            }
        });




        return v;
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();


            }
        }).executeAsync();


    }
/*
    public void disconnectGoogle()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }
                });
    }
*/
}