package edu.iastate.scribblestuff;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onGamesClicked(View view) {

    }

    public void onDrawClicked(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

    public void onFriendsClicked(View view) {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
    }

    public void onSignOutClicked(View view) {

    }

}
