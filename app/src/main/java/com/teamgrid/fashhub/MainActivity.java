package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void login(View v) {

        final EditText emailValidate = (EditText) findViewById(R.id.input_email);
        final EditText passwordValidate = (EditText) findViewById(R.id.input_password);

        String email = emailValidate.getText().toString().trim();
        String password = passwordValidate.getText().toString();

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

    }





    public void sign_up (View view){
        Intent signup = new Intent(this, chooseSignUp.class);
        startActivity(signup);
    }
}