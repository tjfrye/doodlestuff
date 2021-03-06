package edu.iastate.scribblestuff;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendsListFragment extends Fragment {

    private static String TAG = "FriendsListFragment";

    private FirebaseDatabase mDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference gamesReference;

    private TableLayout mFriendsListTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFriendsListTable = view.findViewById(R.id.friendsListTable);

        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = mDatabase.getReference("relationships");
        gamesReference = mDatabase.getReference("games");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added");
                Relationship r = dataSnapshot.getValue(Relationship.class);
                if(r.getUser1().equals(mCurrentUser.getUid()) && r.status == 1) {
                    findUser(r.getUser2(), dataSnapshot.getKey());
                } else if (r.getUser2().equals(mCurrentUser.getUid()) && r.status == 1) {
                    findUser(r.getUser1(), dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Changed");
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

    private void findUser(final String uid, final String key) {
        final DatabaseReference ref = mDatabase.getReference("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added");
                User user = dataSnapshot.getValue(User.class);
                if (user.getUid().equals(uid)) {
                    buildRow(key, user.username);
                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Changed");
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

    private void buildRow(String key, String username) {
        if(isAdded()) {
            TableRow row = new TableRow(getActivity().getApplicationContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setTag(key);

            TextView usernameText = new TextView(getActivity().getApplicationContext());
            usernameText.setText(username);
            usernameText.setGravity(Gravity.CENTER);
            usernameText.setTextSize(20);
            usernameText.setPadding(8,8,8,8);
            usernameText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            usernameText.setTextColor(Color.BLACK);
            row.addView(usernameText);

            Button challengeButton = buildChallengeButton(username);
            row.addView(challengeButton);

            Button removeButton = buildRemoveButton();
            row.addView(removeButton);

            mFriendsListTable.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private Button buildChallengeButton(String username) {
        final String temp = username;
        Button challengeButton = new Button(getActivity().getApplicationContext());
        challengeButton.setText(getString(R.string.challenge));
        challengeButton.setTextSize(18);
        challengeButton.setPadding(8,8,8,8);
        challengeButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        Log.d(TAG, "buildChallengeButton");
        challengeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getContext(), temp + " has been challenged!", Toast.LENGTH_SHORT).show();
                challengeRequest(temp);
            }
        });
        return challengeButton;
    }

    private Button buildRemoveButton() {
        Button removeButton = new Button(getActivity().getApplicationContext());
        removeButton.setText(getString(R.string.remove));
        removeButton.setTextSize(18);
        removeButton.setPadding(8,8,8,8);
        removeButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TableRow r = (TableRow)view.getParent();
                TextView t = (TextView)r.getChildAt(0);
                removeRequest(r.getTag().toString());
                removeGames(t.getText().toString());
                Toast.makeText(getContext(), "Removed friend: " + t.getText(), Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        return removeButton;
    }

    private void challengeRequest(String username) {
        Log.d(TAG, "key: " + username);
        Game game = new Game(
                mCurrentUser.getDisplayName(),
                username,
                0,
                mCurrentUser.getDisplayName(),
                "");
        DatabaseReference newReference = gamesReference.push();
        newReference.setValue(game);
    }

    private void removeRequest(String key) {
        DatabaseReference ref = mDatabase.getReference("relationships");
        ref.child(key).removeValue();
    }

    private void removeGames(final String username) {
        final DatabaseReference ref = mDatabase.getReference("games");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added");
                Game game = dataSnapshot.getValue(Game.class);
                if((game.getPartnerName1().equals(mCurrentUser.getDisplayName()) || game.getPartnerName2().equals(mCurrentUser.getDisplayName()))
                        && (game.getPartnerName1().equals(username) || game.getPartnerName2().equals(username))) {
                    ref.child(dataSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Changed");
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

    private void refresh() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new FriendsListFragment()).commit();
    }
}
