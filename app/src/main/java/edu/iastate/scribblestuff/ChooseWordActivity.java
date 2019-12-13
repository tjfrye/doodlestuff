package edu.iastate.scribblestuff;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseWordActivity extends AppCompatActivity implements View.OnClickListener {

    private String chosenWord;
    private String word1 = "apple"; //TODO change words to ones taken from list
    private String word2 = "dog";
    private String word3 = "chair";
    private String customWord;
    private String gameId;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameId = getIntent().getStringExtra("gameId");

        setContentView(R.layout.activity_choose_word);

        Button word1Button = findViewById(R.id.word1Button);
        word1Button.setText(word1);
        word1Button.setOnClickListener(this);

        Button word2Button = findViewById(R.id.word2Button);
        word2Button.setText(word2);
        word2Button.setOnClickListener(this);

        Button word3Button = findViewById(R.id.word3Button);
        word3Button.setText(word3);
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
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
}
