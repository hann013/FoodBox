package com.waterloohacks2015.foodbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String USER_ID = "USER_ID";
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize Firebase
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(ListActivity.FIREBASE_URI);

        final EditText email = (EditText) findViewById(R.id.user_email);
        final EditText password = (EditText) findViewById(R.id.user_password);

        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signupButton = (Button) findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(email.getText().toString(), password.getText().toString());
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser(email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void loginUser(final String email, final String password) {
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // save user id to SharedPreferences
                saveUserIdAndEmail(authData.getUid(), email);
                redirectToList();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNewUser(final String email, final String password) {
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                // save user email in database and SharedPreferences
                String userId = result.get("uid").toString();

                Firebase newUserRef = ref.child("users").child(userId);
                FoodBoxUser newUser = new FoodBoxUser(email);
                newUserRef.setValue(newUser);

                saveUserIdAndEmail(userId, email);

                redirectToList();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(LoginActivity.this, "Error. Try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserIdAndEmail(String userId, String userEmail) {
        // save user id and email to SharedPreferences
        SharedPreferences.Editor prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE).edit();
        prefs.putString(USER_ID, userId);
        prefs.putString(ListActivity.USER_EMAIL, userEmail);
        prefs.apply();
    }

    private void redirectToList() {
        // redirect to list page
        Intent goToList = new Intent(this, ListActivity.class);
        startActivity(goToList);
    }
}
