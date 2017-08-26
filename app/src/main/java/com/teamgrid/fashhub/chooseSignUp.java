package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class ChooseSignUp extends AppCompatActivity implements View.OnClickListener {
    AppCompatButton client, designer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_up);

        client = (AppCompatButton) findViewById(R.id.asClient);
        designer = (AppCompatButton) findViewById(R.id.asDesigner);
        client.setOnClickListener(this);
        designer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent signup = new Intent(this, SignUpActivity.class);
        if(v == client){
            signup.putExtra("user", "client");
        }else if(v == designer){
            signup.putExtra("user", "designer");
        }
        startActivity(signup);
    }
}