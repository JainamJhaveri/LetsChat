package com.example.digvijay.letschat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static com.example.digvijay.letschat.MyPreferences.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    LoginButton loginButton;
    CallbackManager callbackmanager;
    public static String USERNAME = "com.example.digvijay.letschat.username";
    Profile profile;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;


    public LoginFragment() {
    }   // empty constructor

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken at = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            if( profile == null)
                try {
                    Log.e(" >> ","getting null profile");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            if( profile != null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hi " + profile.getFirstName() + " :)", Toast.LENGTH_SHORT).show();

                setUsername(getContext(), profile.getName());
                setId(getContext(), profile.getId());
                setLoggedIn(getContext(), true);

                moveToHomeActivity(profile);
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(getContext(), "Cancelled by user", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getContext(), "Please check you Internet Connection !", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackmanager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                moveToHomeActivity( currentProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackmanager, callback);

    }

    @Override
    public void onResume() {
        super.onResume();
        moveToHomeActivity( profile );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult( requestCode, resultCode , data );
    }


    private void moveToHomeActivity(Profile profile) {

        if (profile != null) {
            Intent i = new Intent( getActivity() , Home.class );
            getActivity().finish();
            startActivity(i);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
