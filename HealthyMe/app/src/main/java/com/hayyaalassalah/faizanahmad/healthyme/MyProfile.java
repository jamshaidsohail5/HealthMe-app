package com.hayyaalassalah.faizanahmad.healthyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

public class MyProfile extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    private CallbackManager callbackManager;
    private LoginManager manager;
    private ProfileTracker mProfileTracker;
    private LoginButton loginButton ;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());



        setContentView(R.layout.activity_my_profile);
        loginButton= (LoginButton) findViewById(R.id.login);
        //loginButton.setPublishPermissions("public_profile");
        //loginButton.setReadPermissions(Arrays.asList(
        //        "public_profile","user_about_me"));

        callbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("publish_actions","public_profile");
        manager = LoginManager.getInstance();
        manager.logInWithPublishPermissions(this,permissionNeeds);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                load();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                profile=newProfile;
                load();

            }
        };

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken==null) {
            login();
            load();
        }

            //load profile




    }
    public void login(){
        List<String> permissionNeeds = Arrays.asList("public_profile");
        manager = LoginManager.getInstance();
        manager.logInWithPublishPermissions(this,permissionNeeds);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                               load();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }
    public void load(){
        profile=Profile.getCurrentProfile();
        if(profile!=null) {
            textView = (TextView) findViewById(R.id.Name);
            imageView = (ImageView) findViewById(R.id.profilepic);
            textView.setText("Welcome" + profile.getFirstName().toString());
            textView = (TextView) findViewById(R.id.age);
        }else {

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        load();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
