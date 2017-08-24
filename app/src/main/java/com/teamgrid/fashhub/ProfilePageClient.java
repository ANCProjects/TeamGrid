package com.teamgrid.fashhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

      //  TextView hi = (TextView) findViewById(R.id.text);
      //  hi.setText("Welcome ".concat(user));
    }

    private void signOut() {
        mFirebaseAuth.signOut();
    }

}