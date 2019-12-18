package edu.iastate.scribblestuff;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FriendsActivity extends AppCompatActivity {

    private String TAG = "FriendsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new FriendsListFragment()).commit();
    }

    public void onFriendsListClicked(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new FriendsListFragment()).commit();
    }

    public void onPendingClicked(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new PendingFragment()).commit();
    }

    public void onAddFriendClicked(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new AddFriendFragment()).commit();
    }

    /*
    public void refresh(Frag) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frag);
        ft.attach(frag);
        ft.commit();
    }

     */
}
