package edu.iastate.scribblestuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startDraw(View view) {
        Intent startGame = new Intent(MainActivity.this, DrawActivity.class);
        startActivity(startGame);
    }
}
