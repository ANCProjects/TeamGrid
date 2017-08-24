package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamgrid.fashhub.utils.Device;

public class SignIn extends AppCompatActivity {

    EditText emailField, passwordField;
    AppCompatButton btnLogin;
    TextView tvSignUp, tvGuest;
    String email, password;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mFirebaseAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.input_email);
        passwordField = (EditText) findViewById(R.id.input_password);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        tvSignUp = (TextView) findViewById(R.id.sign_up);
        tvGuest = (TextView) findViewById(R.id.guest);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        tvGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGuest();
            }
        });

    }

    private void signUp() {
        Intent signup = new Intent(this, ChooseSignUp.class);
        startActivity(signup);
    }


    private void signIn() {

        if (!validateForm()) {
            return;
        }

        if (Device.isConnected(this)) {

            if (!Device.isProgressDialogShowing()) {
                Device.showProgressDialog(this, false, "Please wait..");

                Device.hideKeyboard(this);

                   mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Device.dismissProgressDialog();
                                }else{
                                    //Start user
                                    try {
                                        mFirebaseAuth.getCurrentUser().reload()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(!task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            Device.dismissProgressDialog();
                                                        }
                                                        else{
                                                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();

                                                            if(user.isEmailVerified()){
                                                                String authtoken = user.getUid();

                                                                Intent Login = new Intent(SignIn.this, ProfilePageClient.class);
                                                                Login.putExtra("user", user.getEmail());
                                                                startActivity(Login);
                                                                finish();
                                                            }else{
                                                                Toast.makeText(getApplicationContext(), "Please visit your mailbox to verify your account", Toast.LENGTH_SHORT).show();
                                                                Device.dismissProgressDialog();
                                                            }
                                                      }
                                                    }
                                                });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    //End user
                                }
                        }
                 });
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInGuest() {

        if (Device.isConnected(this)) {

            if (!Device.isProgressDialogShowing()) {
                Device.showProgressDialog(this, false, "Please wait..");

                Device.hideKeyboard(this);

                mFirebaseAuth.signInAnonymously()
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Device.dismissProgressDialog();
                                }else{
                                    final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                    String authtoken = user.getUid();

                                    Intent Login = new Intent(SignIn.this, ProfilePageClient.class);
                                    Login.putExtra("user", "Guest");
                                    startActivity(Login);
                                    finish();
                                }
                            }
                        });
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        email = emailField.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a Valid Email Address");
            Toast.makeText(getApplicationContext(), "invalid email address", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            emailField.setError(null);
        }

        password = passwordField.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 10) {
            passwordField.setError("between 6 and 10 characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }
}