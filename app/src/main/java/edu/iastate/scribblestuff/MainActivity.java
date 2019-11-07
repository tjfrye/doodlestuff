//package edu.iastate.scribblestuff;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//    }
//
//    public void startDraw(View view) {
//        Intent startGame = new Intent(MainActivity.this, DrawActivity.class);
//        startActivity(startGame);
//    }
//}



package edu.iastate.scribblestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//        SignInButton googleSignInButton = findViewById(R.id.signInButtonGoogle);
//        googleSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToGoogleSignIn();
//            }
//        });
//
//        Button emailSignInButton = findViewById(R.id.loginButton);
//        emailSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToEmailSignIn();
//            }
//        });

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE);

        if(sharedPref.getBoolean("isLoggedIn", false)) { //check if someone is logged in
            //check if last sign-in was via Google
//            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            if(account != null) {
//                Log.d(TAG, "Already signed in with Google");
//                goToHome();
//            }

            goToDraw();
        } else {
            goToGoogleSignIn();
        }
    }

    void goToGoogleSignIn() {
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
    }

    void goToEmailSignIn() {
        Intent intent = new Intent(this, EmailSignInActivity.class);
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
