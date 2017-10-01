package com.quesan.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class MainActivity extends AppCompatActivity
{

    private EditText txtEmail,txtPassword;
    private Button btnLogin,btnRegister;
    private FirebaseAuth firebaseAuth;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            updateUI();
        }
    }

    public void updateUI()
    {

        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
        //new CheckInternet().execute();
    }

    public void initialize()
    {
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button) findViewById(R.id.btnRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        addListener();

//FB
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
       // loginButton.setReadPermissions("email","public_profile","first_name","last_name");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {

            @Override
            public void onSuccess(LoginResult loginResult)
            {

             Toast.makeText(MainActivity.this,"successfully logged in",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });




    }
        private boolean validate()
        {
            boolean b=true;
            if(txtEmail.getText().toString().trim().length()==0)
            {
                txtEmail.setError("Email cant be empty");
                b=false;
            }
            else if(txtPassword.getText().toString().trim().length()<=0)
            {
                txtPassword.setError("very Weak password");
                b=false;
            }
            return b;
        }

        private void addListener()
        {
            //Login with Email and Password

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validate())
                    {
                        String email=txtEmail.getText().toString();
                        String password=txtPassword.getText().toString();

                        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    updateUI();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });

            //create a new user with email and password
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                        if(validate())
                        {
                            String email=txtEmail.getText().toString();
                            String password=txtPassword.getText().toString();

                            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        updateUI();
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }


                }
            });

        }

        /*
    public  void printHashKey(Context pContext) {
        try {

            PackageInfo info = PackageManager.getPackageInfo(pContext, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hash key", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("key hash", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("keyhash", "printHashKey()", e);
        }
    }
*/

}









