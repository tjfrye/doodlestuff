package edu.iastate.scribblestuff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateAccountFragment extends Fragment {

    private String TAG = "CreateAccountFragment";

    private FirebaseAuth mAuth;

    private SharedPreferences.Editor editor;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordValidationField;
    private EditText mUsernameField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mEmailField = view.findViewById(R.id.emailEditText);
        mPasswordField = view.findViewById(R.id.passwordEditText);
        mPasswordValidationField = view.findViewById(R.id.passwordVerifyEditText);
        mUsernameField = view.findViewById(R.id.usernameEditText);

        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString(),
                        mPasswordField.getText().toString(),
                        mUsernameField.getText().toString());
            }
        });
    }

    /**
     * Creates an account using Firebase for a user with their email/password
     * @param email user's email
     * @param password user's password
     */
    private void createAccount(String email, String password, final String username) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.toString());
                            addPersonalInfo(user, username);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                            Random rand = new Random();
                            String code = String.format("%04d", rand.nextInt(10000));
                            User u = new User(username, code, user.getUid());
                            ref.child(ref.push().getKey()).setValue(u);

                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            goToMainActivity();
                        } else {
                            try {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getContext(), "Password too weak", Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getContext(), "User with these credentials already exists", Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Toast.makeText(getContext(), e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.getMessage());
                            }

                        }
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void addPersonalInfo(FirebaseUser user, String username) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Username has been added");
                        } else {
                            Log.d(TAG, "Something went wrong adding username to profile");
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String username = mUsernameField.getText().toString();
        if(TextUtils.isEmpty(username)) {
            mUsernameField.setError("Required.");
            valid = false;
        } else {
            mUsernameField.setError(null);
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String validationPassword = mPasswordValidationField.getText().toString();
        if(TextUtils.isEmpty(validationPassword)) {
            mPasswordValidationField.setError("Required.");
            valid = false;
        } else if(!validationPassword.equals(password)) {
            mPasswordValidationField.setError("Passwords must match");
            valid = false;
        } else {
            mPasswordValidationField.setError(null);
        }

        return valid;
    }
}