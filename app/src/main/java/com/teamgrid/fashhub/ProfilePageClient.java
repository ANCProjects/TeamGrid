package com.teamgrid.fashhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class ProfilePageClient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_client);

        EditText userNameClient = (EditText) findViewById(R.id.input_username_client);
        userNameClient.getText().toString();

    }
}
