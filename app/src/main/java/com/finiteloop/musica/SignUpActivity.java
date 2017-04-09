package com.finiteloop.musica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    EditText mUsername;
    @NotEmpty
    @Email
    EditText mEmail;
    EditText mPassword;
    Button mSignIn,mSignUp;
    String email, password, username;
    Validator mValidator;
    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

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
                mProgressDialog.setMessage("Signing up, Please Wait...");
                mProgressDialog.show();
                if (mPassword.getText().toString().length() < 6) {
                    mPassword.setError("Password too Short");
                }
                mValidator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        username = mUsername.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, HomeStreamActivity.class));
                } else {
                    Snackbar.make(mCoordinatorLayout, "There was an error while signing you up", Snackbar.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }
        });
    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        mProgressDialog.dismiss();
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    /*private boolean validateCredentials() {
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        username = mUsername.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Snackbar.make(mRelativeLayout,"Email cannot be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Snackbar.make(mRelativeLayout,"Password cannot be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(username)){
            Snackbar.make(mRelativeLayout,"Username cannot be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }*/
}
