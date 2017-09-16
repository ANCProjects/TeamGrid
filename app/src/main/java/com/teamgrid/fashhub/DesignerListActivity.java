package com.teamgrid.fashhub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamgrid.fashhub.adapter.DesignerListAdapter;
import com.teamgrid.fashhub.models.UserDetail;
import com.teamgrid.fashhub.utils.Constants;
import com.teamgrid.fashhub.utils.Device;
import com.teamgrid.fashhub.utils.NewSimpleDividerItemDecoration;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class DesignerListActivity extends AppCompatActivity {

    private static final String TAG = DesignerListActivity.class.getSimpleName();

    private ProgressBar progressbar;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDesignerListListener;
    ArrayList<String> designerId = new ArrayList<String>();
    ArrayList<UserDetail> designerDetails = new ArrayList<>();
    DesignerListAdapter adapter;
    UserDetail currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_list);

        if(getIntent()!=null){
            currentUser = (UserDetail) getIntent().getExtras().getSerializable("currentUser");
        }

        Device.dismissProgressDialog(this);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.designer_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FOLDER_DATABASE_DESIGNER);

        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // UserDetail is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // UserDetail is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // updateUI(user);
            }
        };
    */
    }

    private void signOut() {
        mAuth.signOut();

        Intent logout = new Intent(this, SignInActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logout);
    }

    @Override
    protected void onStart(){
        super.onStart();
        progressbar.setVisibility(VISIBLE);
        recyclerView.setVisibility(View.GONE);

        designerId.clear();
        designerDetails.clear();

        final ValueEventListener designerListListener = new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                   for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
                       designerId.add(snapshots.getValue().toString());
                   }
                    designerId.remove(mAuth.getCurrentUser().getUid());

                    for(int i = 0; i < designerId.size(); i++){

                        DatabaseReference designerDetailsRef =  FirebaseDatabase.getInstance().getReference()
                                .child(Constants.FOLDER_DATABASE_USERDETAILS).child(designerId.get(i));
                        designerDetailsRef.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                             UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                             designerDetails.add(userDetail);
                             setupView();
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {
                              findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
                              progressbar.setVisibility(View.GONE);
                          }
                       });
                    }

                } else {
                    findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }
            }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }
            };

            mDatabaseRef.addValueEventListener(designerListListener);
            //mAuth.addAuthStateListener(mAuthListener);
            mDesignerListListener = designerListListener;
    }


    private void setupView(){
        adapter = new DesignerListAdapter(getBaseContext(),designerDetails,currentUser);
        recyclerView.addItemDecoration(new NewSimpleDividerItemDecoration(getBaseContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(adapter.getItemCount()==0) {
            findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
        }else{
            // toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
        }

        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(VISIBLE);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mDesignerListListener != null){
            mDatabaseRef.removeEventListener(mDesignerListListener);
        }

       // if (mAuthListener != null) {
       //        mAuth.removeAuthStateListener(mAuthListener);
       // }
    }

    @Override
    protected void onResume() {
        super.onResume();
     }

    @Override
    protected void onPause() {
        super.onPause();
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
            case R.id.action_search:
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    //          circleReveal(R.id.toolbar_search,1,true,true);
                    //      else
                    //          searchtoolbar.setVisibility(VISIBLE);

                    //      item_search.expandActionView();
                    break;
            case R.id.action_sign_out:
                signOut();
                break;
            case R.id.action_edit_profile:
                if(!currentUser.getRole().equalsIgnoreCase("Guest")){
                    Intent edit = new Intent(this, EditProfile.class);
                    edit.putExtra("user", currentUser.getEmail());
                    startActivity(edit);
                }
             break;
        }

        return super.onOptionsItemSelected(item);
    }
                                                            }
