package edu.iastate.scribblestuff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class AddFriendFragment extends Fragment {

    private static String TAG = "AddFriendFragment";

    private FirebaseDatabase mDatabase;
    private FirebaseUser mCurrentUser;

    private EditText mUsernameField;
    private EditText mFriendCodeField;
    private TextView mResultText;
    private Button mSearchButton;
    private Button mSendRequestButton;

    private String uid;
    private boolean result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        result = false;
        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mUsernameField = view.findViewById(R.id.usernameField);
        mFriendCodeField = view.findViewById(R.id.friendCodeField);
        mResultText = view.findViewById(R.id.resultText);
        mSearchButton = view.findViewById(R.id.searchButton);
        mSendRequestButton = view.findViewById(R.id.sendRequestButton);

        mResultText.setVisibility(View.INVISIBLE);
        mSendRequestButton.setVisibility(View.INVISIBLE);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUser();
            }
        });

        mSendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    public void searchUser() {
        final DatabaseReference ref = mDatabase.getReference("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added");
                User user = dataSnapshot.getValue(User.class);
                mResultText.setVisibility(View.VISIBLE);
                Log.d(TAG, user.getUsername());
                Log.d(TAG, user.getFriendcode());
                if (user.getUsername().equals(mUsernameField.getText().toString()) && user.getFriendcode().equals(mFriendCodeField.getText().toString())) {
                    mResultText.setText("User found");
                    mSendRequestButton.setVisibility(View.VISIBLE);
                    uid = user.getUid();
                    hasRelationship(mCurrentUser.getUid(), user.getUid());
                    ref.removeEventListener(this);
                } else {
                    mResultText.setText("User not found");
                    mSendRequestButton.setVisibility(View.INVISIBLE);
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

    public void sendRequest() {
        DatabaseReference ref = mDatabase.getReference("relationships");
        if(!result) {
            Relationship r = new Relationship(mCurrentUser.getUid(), uid, 0);
            ref.child(ref.push().getKey()).setValue(r);
            Toast.makeText(getContext(), "Sent friend request to " + mUsernameField.getText(), Toast.LENGTH_SHORT).show();
            refresh();
        } else {
            mResultText.setText("User already added");
        }
    }

    public void hasRelationship(final String u1, final String u2) {
        if(!u1.equals(u2)) {
            final DatabaseReference ref = mDatabase.getReference("relationships");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(TAG, "Child Added");
                    Relationship r = dataSnapshot.getValue(Relationship.class);
                    if ((r.getUser1().equals(u1) || r.getUser1().equals(u2)) &&
                            (r.getUser2().equals(u1) || r.getUser2().equals(u2))) {
                        result = true;
                        ref.removeEventListener(this);
                    } else {
                        result = false;
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
        } else {
            mResultText.setText("That's you, you silly goose!");
            mSendRequestButton.setVisibility(View.INVISIBLE);
        }
    }

    public void refresh() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new AddFriendFragment()).commit();
    }
}



