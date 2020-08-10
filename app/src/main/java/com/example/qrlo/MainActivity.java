package com.example.qrlo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrlo.Signup.Signup;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private AccessTokenTracker accessTokenTracker;
    EditText Email;
    EditText PassWord;
    Button Login_Button;
    private TextView SignUp;


    private SignInButton btn_google;
    private LoginButton FacebookButton;




    FirebaseDatabase firebaseDatabase;
    private FirebaseUser User;


    private ProgressBar pb_login;
    private String stuid;
    String stEmail;
    String stPassWord;

    private static final String TAG = "FacebookAuthentication";

    ArrayList<my_qr_item> mList = new ArrayList<my_qr_item>();
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stEmail = getIntent().getStringExtra("stEmail");
        stPassWord = getIntent().getStringExtra("stPassWord");

        SignUp = (TextView)findViewById(R.id.textView2);
        FacebookButton = findViewById(R.id.facebook_button);


        if(stEmail !=null && stPassWord != null)
        {
            Email.setText(stEmail);
            PassWord.setText(stPassWord);
        }




        mFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        FacebookButton.setReadPermissions("email", "public_profile");






        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Signup.class);
                startActivity(in);
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        FacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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
                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", User.getUid());
                    editor.putString("email",User.getEmail());

                    editor.apply();

                    Intent in = new Intent(MainActivity.this, After_Login.class);
                    startActivity(in);
                }
                else
                {

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






        createRequest();



        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();





        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(in, RC_SIGN_IN);
            }
        });





        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken ==null)
        {
      //      Intent in  = new Intent(MainActivity.this , After_Login.class);
        //    startActivity(in);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();
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

                        }
                    }
                });
    }

    private void handleFacebookToken(AccessToken accessToken)
    {




        Log.d(TAG, "handleFacebookToken" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                pb_login.setVisibility(View.VISIBLE);


                if(task.isSuccessful())
                {
                    Log.d(TAG , " sign in with credential: successful");
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();


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

                }
            }
        });
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


    private void createRequest()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void updateUI(FirebaseUser currentUser) {
    }




}
