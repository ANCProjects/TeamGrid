package com.teamgrid.fashhub;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teamgrid.fashhub.models.UserDetail;
import com.teamgrid.fashhub.utils.views.CircleImageView;

public class DesignerProfilePage extends AppCompatActivity implements View.OnClickListener {

    UserDetail designerClicked, user;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_profile_page);

        if(getIntent()!=null){
            designerClicked = (UserDetail) getIntent().getExtras().getSerializable("viewDesigner");
            user = (UserDetail) getIntent().getExtras().getSerializable("currentUser");
        }

        CircleImageView pic = (CircleImageView) findViewById(R.id.user_photo);
        if(designerClicked.getAvaterUrl()!=null){
            Glide.with(this).load(designerClicked.getAvaterUrl())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_person_white_36dp)
                    .into(pic);
        }

        ImageView call = (ImageView) findViewById(R.id.call);
        call.setOnClickListener(this);
        ImageView mail = (ImageView) findViewById(R.id.mail);
        mail.setOnClickListener(this);
        TextView name = (TextView) findViewById(R.id.full_name);
        name.setText(designerClicked.getName());
        TextView bio = (TextView) findViewById(R.id.bio);
        bio.setText(designerClicked.getBio());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.designer_works_recycler);
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call:
                break;
            case R.id.mail:
                break;
            case R.id.fab:
                if(!user.getRole().equalsIgnoreCase("Guest")){
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("chatDesigner", designerClicked);
                    intent.putExtra("chatUser", user);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Guests are not allowed to chat, please create an account with us today.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}