package edu.iastate.scribblestuff;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GuessActivity extends AppCompatActivity {
    TextView word;
    String guess="HOUSE"; //this value will need to be recieved from database with bitmap
    Bitmap bmp;
    private Button[] buttons = new Button[10];
    private Character[] letters = new Character[10];
    private Boolean[] pressed = new Boolean[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        buttons[0] = (Button)findViewById(R.id.buttonOne);
        buttons[1] = (Button)findViewById(R.id.buttonTwo);
        buttons[2]= (Button)findViewById(R.id.buttonThree);
        buttons[3] = (Button)findViewById(R.id.buttonFour);
        buttons[4] = (Button)findViewById(R.id.buttonFive);
        buttons[5] = (Button)findViewById(R.id.buttonSix);
        buttons[6]= (Button)findViewById(R.id.buttonSeven);
        buttons[7] = (Button)findViewById(R.id.buttonEight);
        buttons[8] = (Button)findViewById(R.id.buttonNine);
        buttons[9]= (Button)findViewById(R.id.buttonTen);
        word = (TextView)findViewById(R.id.Word);
        for(int i =0;i<10; i++){
            pressed[i] =false;
        }
        setLetters();


    }
    private void setLetters(){
        char[] word = guess.toCharArray();
        Random r = new Random();

        for(int i=0; i<letters.length;i++){
            if(i<word.length){
                letters[i] = word[i];
            }
            else{
                char c = (char) (r.nextInt(26) + 'A');
                letters[i] = c;
            }
        }
        Random rgen = new Random();  // Random number generator

        for (int i=0; i<letters.length; i++) {  //Randomizes array position of letters
            int randomPosition = rgen.nextInt(letters.length);
            Character temp = letters[i];
            letters[i] = letters[randomPosition];
            letters[randomPosition] = Character.toUpperCase(temp);//ensures all values are uppercase
        }
        for(int i=0; i<buttons.length; i++){
            buttons[i].setText(String.valueOf(letters[i])); //sets each button to a letter
        }

    }

    public void onClearClicked(View view) {
        for(int i =0;i<10; i++){
            pressed[i] =false;
            buttons[i].setText(String.valueOf(letters[i]));
            word.setText("");
        }
    }

    public void letterClicked(View view) {
        switch (view.getId()) {

            case R.id.buttonOne:
                if(!pressed[0]) {
                    word.setText(word.getText().toString() + buttons[0].getText().toString());
                    pressed[0] =true;
                    buttons[0].setText("");
                }
                break;
            case R.id.buttonTwo:
                if(!pressed[1]) {
                    word.setText(word.getText().toString() + buttons[1].getText().toString());
                    pressed[1] =true;
                    buttons[1].setText("");
                }
                break;
            case R.id.buttonThree:
                if(!pressed[2]) {
                    word.setText(word.getText().toString() + buttons[2].getText().toString());
                    pressed[2] =true;
                    buttons[2].setText("");
                }
                break;
            case R.id.buttonFour:
                if(!pressed[3]) {
                    word.setText(word.getText().toString() + buttons[3].getText().toString());
                    pressed[3] =true;
                    buttons[3].setText("");
                }
                break;
            case R.id.buttonFive:
                if(!pressed[4]) {
                    word.setText(word.getText().toString() + buttons[4].getText().toString());
                    pressed[4] =true;
                    buttons[4].setText("");
                }
                break;
            case R.id.buttonSix:
                if(!pressed[5]) {
                    word.setText(word.getText().toString() + buttons[5].getText().toString());
                    pressed[5] =true;
                    buttons[5].setText("");
                }
                break;
            case R.id.buttonSeven:
                if(!pressed[6]) {
                    word.setText(word.getText().toString() + buttons[6].getText().toString());
                    pressed[6] =true;
                    buttons[6].setText("");
                }
                break;
            case R.id.buttonEight:
                if(!pressed[7]) {
                    word.setText(word.getText().toString() + buttons[7].getText().toString());
                    pressed[7] =true;
                    buttons[7].setText("");
                }
                break;
            case R.id.buttonNine:
                if(!pressed[8]) {
                    word.setText(word.getText().toString() + buttons[8].getText().toString());
                    pressed[8] =true;
                    buttons[8].setText("");
                }
                break;
            case R.id.buttonTen:
                if(!pressed[9]) {
                    word.setText(word.getText().toString() + buttons[9].getText().toString());
                    pressed[9] =true;
                    buttons[9].setText("");
                }
                break;

        }
        if(word.getText().toString().equals(guess)){
            Toast.makeText(this, "You Guessed It!", Toast.LENGTH_LONG).show();
        }
    }
}
