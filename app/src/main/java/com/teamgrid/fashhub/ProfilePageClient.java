package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.teamgrid.fashhub.utils.Device;

public class ProfilePageClient extends AppCompatActivity {

    String user;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_client);

        if(getIntent()!=null){
            user = getIntent().getExtras().getString("user");
        }

        Device.dismissProgressDialog(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void signOut() {
        mFirebaseAuth.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
              //  Intent settings = new Intent(this, SettingsActivity.class);
              //  startActivity(settings);
                break;
            case R.id.action_edit_profile:
                Intent edit = new Intent(this, EditProfile.class);
                edit.putExtra("user", user);
                startActivity(edit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}