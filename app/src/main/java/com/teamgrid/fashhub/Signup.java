package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void signUpDesigner (View v){
        final EditText emailValidate = (EditText) findViewById(R.id.input_email_designer);
        final EditText passwordValidate = (EditText) findViewById(R.id.input_password_designer);
        final EditText reEnterPasswordValidate = (EditText) findViewById(R.id.input_reEnterPassword_designer);

        String email = emailValidate.getText().toString().trim();
        String password = passwordValidate.getText().toString();
        String reEnterPassword = reEnterPasswordValidate.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailValidate.setError("Enter a Valid Email Address");
            Toast.makeText(getApplicationContext(), "invalid email address", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Input a Valid Email", Toast.LENGTH_SHORT).show();
            emailValidate.setError(null);

        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10){
            passwordValidate.setError("between 4 and 10 characters");
        } else{
            passwordValidate.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword != password){
            reEnterPasswordValidate.setError("password do not match");
        } else{
            passwordValidate.setError(null);
        }
    }

    public void login (View v){
        Intent Login = new Intent(this, HomeActivity.class);
        startActivity(Login);
    }
}
