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

public class PendingFragment extends Fragment {

    private static String TAG = "PendingFragment";

    private FirebaseDatabase mDatabase;
    private FirebaseUser mCurrentUser;

    private TableLayout mPendingTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPendingTable = view.findViewById(R.id.pendingTable);

        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = mDatabase.getReference("relationships");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added");
                Relationship r = dataSnapshot.getValue(Relationship.class);
                if(r.getUser1().equals(mCurrentUser.getUid()) && r.status == 0) {
                    findUser(r.getUser2(), dataSnapshot.getKey(), true);
                } else if (r.getUser2().equals(mCurrentUser.getUid()) && r.status == 0) {
                    findUser(r.getUser1(), dataSnapshot.getKey(), false);
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

    public void findUser(final String uid, final String key, final boolean outgoing) {
        final DatabaseReference ref = mDatabase.getReference("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Child Added");
                User user = dataSnapshot.getValue(User.class);
                if (user.getUid().equals(uid)) {
                    buildRow(key, user.username, outgoing);
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

    public void buildRow(String key, String username, boolean outgoing) {
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

            if(outgoing) {
                TextView outgoingText = new TextView(getActivity().getApplicationContext());
                outgoingText.setText("Outgoing");
                outgoingText.setGravity(Gravity.CENTER);
                outgoingText.setTextSize(20);
                outgoingText.setPadding(8,8,8,8);
                outgoingText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                outgoingText.setTextColor(Color.BLACK);
                row.addView(outgoingText);

                Button cancelButton = buildCancelButton();
                row.addView(cancelButton);

            } else {
                Button acceptButton = buildAcceptButton();
                row.addView(acceptButton);

                Button ignoreButton = buildIgnoreButton();
                row.addView(ignoreButton);
            }

            mPendingTable.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public Button buildAcceptButton() {
        Button acceptButton = new Button(getActivity().getApplicationContext());
        acceptButton.setText("Accept");
        acceptButton.setTextSize(18);
        acceptButton.setPadding(8,8,8,8);
        acceptButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TableRow r = (TableRow)view.getParent();
                acceptRequest(r.getTag().toString());
                TextView t = (TextView)r.getChildAt(0);
                Toast.makeText(getContext(), "Accepted friend request from " + t.getText(), Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        return acceptButton;
    }

    public Button buildCancelButton() {
        Button cancelButton = new Button(getActivity().getApplicationContext());
        cancelButton.setText("Cancel");
        cancelButton.setTextSize(18);
        cancelButton.setPadding(8,8,8,8);
        cancelButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TableRow r = (TableRow)view.getParent();
                cancelRequest(r.getTag().toString());
                TextView t = (TextView)r.getChildAt(0);
                Toast.makeText(getContext(), "Cancelled friend request to " + t.getText(), Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        return cancelButton;
    }

    public Button buildIgnoreButton() {
        Button ignoreButton = new Button(getActivity().getApplicationContext());
        ignoreButton.setText("Ignore");
        ignoreButton.setTextSize(18);
        ignoreButton.setPadding(8,8,8,8);
        ignoreButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        ignoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TableRow r = (TableRow)view.getParent();
                ignoreRequest(r.getTag().toString());
                TextView t = (TextView)r.getChildAt(0);
                Toast.makeText(getContext(), "Ignored friend request from " + t.getText(), Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        return ignoreButton;
    }

    public void acceptRequest(String key) {
        DatabaseReference ref = mDatabase.getReference("relationships");
        ref.child(key).child("status").setValue(1);
    }

    public void ignoreRequest(String key) {
        DatabaseReference ref = mDatabase.getReference("relationships");
        ref.child(key).removeValue();
    }

    public void cancelRequest(String key) {
        DatabaseReference ref = mDatabase.getReference("relationships");
        ref.child(key).removeValue();
    }

    public void refresh() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeHolder, new PendingFragment()).commit();
    }
}
