package edu.iastate.scribblestuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity where the user's current games are displayed
 */
public class GamesActivity extends AppCompatActivity {

    private static String TAG = "GamesActivity";

    private List<Game> gamesList = new ArrayList<>();
    private List<String> gamesIdList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String displayName;
    private RecyclerView gamesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("games");

        displayName = mAuth.getCurrentUser().getDisplayName();
        Log.d(TAG, "Displayname is " + displayName);

        gamesRecyclerView = findViewById(R.id.gamesRecyclerView);
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //writeNewGame();


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added, id: " + dataSnapshot.getKey());
                Game newGame = dataSnapshot.getValue(Game.class);
                Log.d(TAG, newGame.toString());
                Log.d(TAG, "partnerName1: " + newGame.getPartnerName1() + ", partnerName2: " + newGame.getPartnerName2());
                if(newGame.getPartnerName1().equals(displayName) || newGame.getPartnerName2().equals(displayName)) {
                    //If its not your turn to draw or you already drew
                    if(!newGame.getWhoDrawTurn().equals(displayName) || !newGame.getDrawingComplete()) {
                        Log.d(TAG, "Added to gamesList");
                        gamesList.add(newGame);
                        gamesIdList.add(dataSnapshot.getKey());
                    }
                }
                GamesAdapter gamesAdapter = new GamesAdapter(getApplicationContext(), gamesList, displayName, gamesIdList);
                gamesRecyclerView.setAdapter(gamesAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Changed");
                gamesList.add(dataSnapshot.getValue(Game.class));
                Log.d(TAG, dataSnapshot.getValue(Game.class).toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Child Removed");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Moved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Child Cancelled");
            }
        });

    }

    private void writeNewGame() {
        Game game = new Game("maggie", "simon", 5, "maggie", "cow");
        //databaseReference.child("game2").setValue(game);
        DatabaseReference gameReference = databaseReference.push();
        gameReference.setValue(game);
        Log.d(TAG, gameReference.getKey());
    }

}
