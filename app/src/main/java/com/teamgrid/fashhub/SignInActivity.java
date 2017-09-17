package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamgrid.fashhub.utils.Constants;
import com.teamgrid.fashhub.utils.Device;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    EditText emailField, passwordField;
    String email, password;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private DatabaseReference mDatabaseRef;

    private static final int GOOGLE_SIGN_IN = 1;
    private static final int FACEBOOK_SIGN_IN = 2;
    private static final int TWITTER_SIGN_IN = 3;
    Map<String, Object> userUpdates;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signin);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        emailField = (EditText) findViewById(R.id.input_email);
        passwordField = (EditText) findViewById(R.id.input_password);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.google_sign_in_button).setOnClickListener(this);
        //findViewById(R.id.facebook_sign_in_button).setOnClickListener(this);
        //findViewById(R.id.twitter_sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);
        findViewById(R.id.guest).setOnClickListener(this);
        TextView home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
            // case R.id.facebook_sign_in_button:
            //     break;
            // case R.id.twitter_sign_in_button:
            //     break;
            case R.id.sign_up:
                signUp();
                break;
            case R.id.guest:
                signInGuest();
                break;
            case R.id.home:
                Home();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Toast.makeText(getApplicationContext(), "Not activated yet", Toast.LENGTH_SHORT).show();

            if (result.isSuccess()) {
                //   Toast.makeText(getApplicationContext(), "result Success", Toast.LENGTH_SHORT).show();
                // Google Sign In was successful, authenticate with Firebase
                //   GoogleSignInAccount account = result.getSignInAccount();
                //   firebaseAuthWithGoogle(account);
            } else {
                //       Toast.makeText(getApplicationContext(), "result failed.", Toast.LENGTH_SHORT).show();
                // Google Sign In failed
            }

        }

        // Result returned from launching the Intent from ;
        else if (requestCode == FACEBOOK_SIGN_IN) {

        }

        // Result returned from launching the Intent from ;
        else if (requestCode == TWITTER_SIGN_IN) {

        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if (Device.isConnected(this)) {
            if (!Device.isProgressDialogShowing()) {
                Device.showProgressDialog(this, false, "Please wait...");

                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                if (credential != null) {
                    Toast.makeText(getApplicationContext(), acct.getIdToken(), Toast.LENGTH_SHORT).show();
                }

                mFirebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication successful.", Toast.LENGTH_SHORT).show();
                                }
                                Device.dismissProgressDialog();
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void signUp() {
        Intent signup = new Intent(this, ChooseSignUp.class);
        startActivity(signup);
    }

    private void Home() {
        Intent home = new Intent(this, Home.class);
        startActivity(home);
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
                                } else {
                                    //Start user
                                    try {
                                        mFirebaseAuth.getCurrentUser().reload()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            Device.dismissProgressDialog();
                                                        } else {
                                                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();

                                                            if (user.isEmailVerified()) {
                                                                String authtoken = user.getUid();

                                                                userUpdates = new HashMap<String, Object>();
                                                                userUpdates.put("isVerified", true);
                                                                mDatabaseRef.child(Constants.FOLDER_DATABASE_USERDETAILS)
                                                                        .child(user.getUid()).updateChildren(userUpdates);

                                                                Intent Login = new Intent(SignInActivity.this, DesignerListActivity.class);
                                                                Login.putExtra("user", user.getEmail());
                                                                startActivity(Login);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Please visit your mailbox to verify your account", Toast.LENGTH_SHORT).show();
                                                                Device.dismissProgressDialog();
                                                            }
                                                        }
                                                    }
                                                });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //End user
                                }
                            }
                        });
            }
        } else {
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
                                } else {
                                    final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                    String authtoken = user.getUid();

                                    Intent Login = new Intent(SignInActivity.this, DesignerListActivity.class);
                                    Login.putExtra("user", "Guest");
                                    startActivity(Login);
                                    finish();
                                }
                            }
                        });
            }
        } else {
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