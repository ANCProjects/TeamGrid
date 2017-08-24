package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamgrid.fashhub.models.User;
import com.teamgrid.fashhub.utils.Device;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";

    EditText usernameValidate, emailValidate, passwordValidate, reEnterPasswordValidate;
    AppCompatButton btnSignUp;
    ImageView image;
    TextView login;

    String email, password,reEnterPassword;
    String userRole;

    private FirebaseAuth mFirebaseAuth;
   // private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        image = (ImageView) findViewById(R.id.topImg);
        usernameValidate = (EditText) findViewById(R.id.input_username);
        emailValidate = (EditText) findViewById(R.id.input_email);
        passwordValidate = (EditText) findViewById(R.id.input_password);
        reEnterPasswordValidate = (EditText) findViewById(R.id.input_reEnterPassword);

        if(getIntent()!=null){
            userRole = getIntent().getExtras().getString("user");
            if(userRole.equals("client"))
                image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.client));
            else
                image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.designer));
        }

        btnSignUp = (AppCompatButton) findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpClient();
            }
        });

        login = (TextView) findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginpage = new Intent(SignUp.this, SignIn.class);
                loginpage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginpage);
            }
        });

/*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
             // updateUI(user);
            }
        };
*/
    }

    @Override
    public void onStart() {
        super.onStart();
     //   mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
     //   if (mAuthListener != null) {
     //       mFirebaseAuth.removeAuthStateListener(mAuthListener);
     //   }
    }

    public void signUpClient() {

        if (!validateForm()) {
            return;
        }

        if (Device.isConnected(this)) {

            if (!Device.isProgressDialogShowing()) {

                Device.showProgressDialog(this, false, "Creating account...");

                Device.hideKeyboard(this);

              // AuthCredential credential = EmailAuthProvider.getCredential(email, password);
              //  Toast.makeText(getApplicationContext(),credential.toString(),Toast.LENGTH_LONG).show();

                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                              if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                   Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_LONG).show();
                                 } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                 }
                                Device.dismissProgressDialog();
                            } else {
                              final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                              sendEmailVerification();
                              onAuthSuccess(user);

                                usernameValidate.setText("");
                                emailValidate.setText("");
                                passwordValidate.setText("");
                                reEnterPasswordValidate.setText("");
                            }
                        }
                    });
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
           String username;
           username = usernameValidate.getText().toString().trim();
           if(TextUtils.isEmpty(username)){
             username = usernameFromEmail(user.getEmail());
           }

           addNewUser(user.getUid(), username, user.getEmail(), userRole);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void addNewUser(String userId, String name, String email, String userRole) {
        User user = new User(name, email, userRole);
        mDatabase.child("users").child(userId).setValue(user);
    }


    private void sendEmailVerification() {
        Device.messageProgressDialog("Sending verification mail");
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_LONG).show();
                        }
                   Device.dismissProgressDialog();
                    }
              });
     }

    private boolean validateForm() {
        boolean valid = true;

        email = emailValidate.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailValidate.setError("Enter a Valid Email Address");
            Toast.makeText(getApplicationContext(), "invalid email address", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            emailValidate.setError(null);
        }

        password = passwordValidate.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 10) {
            passwordValidate.setError("between 6 and 10 characters");
            valid = false;
        } else {
            passwordValidate.setError(null);
        }

        reEnterPassword = reEnterPasswordValidate.getText().toString().trim();
        if (TextUtils.isEmpty(reEnterPassword) || !reEnterPassword.equals(password)) {
            reEnterPasswordValidate.setError("password do not match");
            valid = false;
        } else {
            passwordValidate.setError(null);
        }

        return valid;
    }
}