package com.finiteloop.musica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    Button mSignIn,mSignUp;
    String email, password, username;
    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;
    CoordinatorLayout mCoordinatorLayout;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSignIn = (Button) findViewById(R.id.activity_sign_up_sign_in);
        mSignUp = (Button) findViewById(R.id.activity_sign_up_button);
        mUsername = (EditText) findViewById(R.id.activity_sign_up_username);
        mPassword = (EditText) findViewById(R.id.activity_sign_up_password);
        mEmail = (EditText) findViewById(R.id.activity_sign_up_email);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_sign_up);

        mProgressDialog = new ProgressDialog(this);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateCredentials()) {
                    mProgressDialog.setMessage("Signing up, Please Wait...");
                    mProgressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createNewUser(task.getResult().getUser());
                                Toast.makeText(getBaseContext(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(SignUpActivity.this, HomeStreamActivity.class));
                            } else {
                                Log.d("Error", task.getException().getMessage());
                                Snackbar.make(mCoordinatorLayout, "There was an error while signing you up", Snackbar.LENGTH_SHORT).show();
                            }
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }


    private boolean validateCredentials() {
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        username = mUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Snackbar.make(mCoordinatorLayout, "Username cannot be empty", Snackbar.LENGTH_SHORT).show();
            mUsername.setError("Username cannot be empty");
            return false;
        }
        if(TextUtils.isEmpty(email)){
            Snackbar.make(mCoordinatorLayout, "Email cannot be empty", Snackbar.LENGTH_SHORT).show();
            mEmail.setError("Email cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(mCoordinatorLayout, "Password cannot be empty", Snackbar.LENGTH_SHORT).show();
            mEmail.setError("Password cannot be empty");
            return false;
        }
        if (username.length() < 4) {
            Snackbar.make(mCoordinatorLayout, "Username too small", Snackbar.LENGTH_SHORT).show();
            mUsername.setError("Username should be greater than 4 size");
            return false;
        }
        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            Snackbar.make(mCoordinatorLayout, "Please Enter Valid Email Address", Snackbar.LENGTH_SHORT).show();
            mEmail.setError("Email Address Invalid");
            return false;
        }

        if (password.length() < 6) {
            Snackbar.make(mCoordinatorLayout, "Password should be of minimum size 6", Snackbar.LENGTH_SHORT).show();
            mPassword.setError("Password should not be less than 6 characters");
            return false;
        }
        return true;
    }

    private void createNewUser(FirebaseUser userFromRegistration) {
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();
        HashMap<String, String> user = new HashMap<>();
        user.put(username, email);
        mDatabase.child("Users").child(userId).setValue(user);
    }
}
