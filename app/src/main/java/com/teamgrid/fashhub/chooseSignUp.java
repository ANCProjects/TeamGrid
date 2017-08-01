package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class chooseSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_up);
    }

    public void asDesigner (View view){
        Intent client_signup = new Intent(this, Signup.class);
        startActivity(client_signup);
    }

    public void asClient (View view){
        Intent client_signup = new Intent(this, SignUpClient.class);
        startActivity(client_signup);
    }
}
