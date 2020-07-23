package com.example.qrlo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private TextView textViewUser;
    private LoginButton loginButton;
    private ImageView imageView;


    FirebaseDatabase firebaseDatabase;
    private FirebaseUser User;
    DatabaseReference myRef;

    private ProgressBar pb_login;



    private String stuid;


    private static final String TAG = "FacebookAuthentication";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.my_qr_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        my_qr_adapter adapter = new my_qr_adapter();
        recyclerView.setAdapter(adapter);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

        firebaseDatabase = FirebaseDatabase.getInstance();







        textViewUser = findViewById(R.id.text_user);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        imageView = findViewById(R.id.imageView);
        pb_login = findViewById(R.id.pb_login);



        mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, " ON SUCCESS " + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, " ON Cancel ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, " ON Error " + error);
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();
                if(User!=null)
                {
                    updateUI(User);
                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", User.getUid());
                    editor.putString("email",User.getEmail());
                    editor.apply();
                }
                else
                {
                    updateUI(null);
                }
            }
        };


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null)
                {
                    mFirebaseAuth.signOut();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookToken(AccessToken accessToken)
    {
        // TODO: 2020-07-23
        /*
        accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        final String name = object.optString("name");
                        final String id= object.optString("id");
                        final String email = object.optString("email");
                        final String birthday= object.optString("birthday");
                        final String gender = object.optString("gender");


                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email, birthday,gender");
        request.setParameters(parameters);
        request.executeAsync();

        */

        Log.d(TAG, "handleFacebookToken" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                pb_login.setVisibility(View.VISIBLE);


                if(task.isSuccessful())
                {
                    Log.d(TAG , " sign in with credential: successful");

                    updateUI(User);

                    User = mFirebaseAuth.getCurrentUser();
                    stuid = User.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("user");
                    myRef.child(stuid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String userName = User.getDisplayName();

                            Map<String, Object> profile = new HashMap<String, Object>();
                            profile.put("email", User.getEmail());
                            profile.put("uid",User.getUid());
                            profile.put("name", userName);
                     //  profile.put("birthday" , birthday);
                          //  profile.put("gender" , gender);
                            myRef.child(User.getUid()).setValue(profile);




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Intent in = new Intent(MainActivity.this , After_Login.class);
                    startActivity(in);
                }
                else
                {
                    Log.d(TAG , " sign in with credential: failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication  Failed",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            textViewUser.setText(user.getDisplayName());



            if(user.getPhotoUrl() != null)
            {
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(imageView);
            }
        }
        else {
            textViewUser.setText("");
            imageView.setImageResource(R.drawable.com_facebook_auth_dialog_background);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener !=null)
        {
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
