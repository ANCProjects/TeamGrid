package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextInputLayout usernamewrapper = (TextInputLayout) findViewById(R.id.usernamewrapper);

        final TextInputLayout passwordwrapper = (TextInputLayout) findViewById(R.id.passwordwrapper);




    }

    public void sign_up (View view){
        Intent signup = new Intent(this, chooseSignUp.class);
        startActivity(signup);
    }
}