package edu.iastate.scribblestuff;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseWordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChooseWordActivity";
    private String chosenWord;
    private String word1;
    private String word2;
    private String word3;
    private String customWord;
    private String gameId;
    private Button word1Button;
    private Button word2Button;
    private Button word3Button;
    private ArrayList<String> wordbank = new ArrayList();

    DatabaseReference wordbankReference;
    DatabaseReference pastWordsReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameId = getIntent().getStringExtra("gameId");

        setContentView(R.layout.activity_choose_word);

        word1Button = findViewById(R.id.word1Button);
        word1Button.setOnClickListener(this);

        word2Button = findViewById(R.id.word2Button);
        word2Button.setOnClickListener(this);

        word3Button = findViewById(R.id.word3Button);
        word3Button.setOnClickListener(this);

        Button customWordButton = findViewById(R.id.customWordButton);
        customWordButton.setOnClickListener(this);

        EditText customWordEditText = findViewById(R.id.newWordEditText);
        customWordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customWord = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Get the wordbank
        wordbankReference = database.getReference("wordbank");
        wordbankReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Added word: " + dataSnapshot.getValue(String.class));

                if(dataSnapshot.getValue().equals("end")) {
                    setRandomWords();
                } else {
                    wordbank.add(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pastWordsReference = database.getReference(gameId).child("pastWords");

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DrawActivity.class);

        switch(v.getId()) {
            case R.id.word1Button:
                chosenWord = word1;
                break;
            case R.id.word2Button:
                chosenWord = word2;
                break;
            case R.id.word3Button:
                chosenWord = word3;
                break;
            case R.id.customWordButton:
                chosenWord = customWord;
                break;
        }

        intent.putExtra("chosenWord", chosenWord);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }

    private void setRandomWords() {
        int wordbankSize = wordbank.size();
        Random random = new Random();

        word1 = wordbank.get(random.nextInt(wordbankSize));
        String temp2 = wordbank.get(random.nextInt(wordbankSize));
        while(temp2.equals(word1)) {
            temp2 = wordbank.get(random.nextInt(wordbankSize));
        }
        word2 = temp2;

        String temp3 = wordbank.get(random.nextInt(wordbankSize));
        while(temp3.equals(word1) || temp3.equals(word2)) {
            temp3 = wordbank.get(random.nextInt(wordbankSize));
        }
        word3 = temp3;

        word1Button.setText(word1);
        word2Button.setText(word2);
        word3Button.setText(word3);

    }
}
