package edu.iastate.scribblestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //check old fashioned sharedpreferences for logged in user
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE);

        //Check if user is logged in via firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(sharedPref.getBoolean("isLoggedIn", false)) { //check if someone is logged in
            goToHome();
        } else if(currentUser != null) {
            goToDraw(); //TODO set up sharedpreferences with user info
        } else {
            goToSignIn();
        }
    }

    void goToSignIn() {
        Log.d(TAG, "goToSignIn");
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    void goToHome() {
        Log.d(TAG, "goToHome");
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    void goToDraw() {
        Log.d(TAG, "goToDraw");
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

}
