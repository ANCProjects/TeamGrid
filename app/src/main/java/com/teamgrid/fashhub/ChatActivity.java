package com.teamgrid.fashhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamgrid.fashhub.adapter.ChatAdapter;
import com.teamgrid.fashhub.models.Comment;
import com.teamgrid.fashhub.models.UserDetail;

import java.util.Calendar;

/**
 * Created by Johnbosco on 07-Sep-17.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private EditText commentField;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private static final String CHAT_REFERENCE = "chat";
    private DatabaseReference mDatabaseReference;
    private UserDetail user, talking_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(getIntent()!=null){
            talking_to = (UserDetail) getIntent().getExtras().getSerializable("chatDesigner");
            user = (UserDetail) getIntent().getExtras().getSerializable("chatUser");
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CHAT_REFERENCE);

        mRecyclerView = (RecyclerView)findViewById(R.id.chat_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        commentField = (EditText)findViewById(R.id.edit_message);
        findViewById(R.id.btn_send).setOnClickListener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                super.onScrollStateChanged(mRecyclerView, scrollState);
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(mRecyclerView, dx, dy);
                boolean enable = false;
                if (mRecyclerView != null && mRecyclerView.getChildCount() > 0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    boolean firstItemVisible = linearLayoutManager.findFirstVisibleItemPosition() == 0;
                    boolean topOfFirstItemVisible = mRecyclerView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                if(enable) {
                    getMessage();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                if(!TextUtils.isEmpty(commentField.getText().toString().trim()))
                    sendMessage();
                break;
            default:
                break;
        }
    }

    private void sendMessage(){
        Comment message = new Comment(user, talking_to, commentField.getText().toString(), null, Calendar.getInstance().getTime().getTime()+"" /*Calendar.getInstance().getTime().toString()*/);
        mDatabaseReference.child(user.getUid()+" to "+talking_to.getUid()).push().setValue(message);
        mDatabaseReference.child(talking_to.getUid()+" to "+user.getUid()).push().setValue(message);
        commentField.setText("");
    }

    private void getMessage(){

        final ChatAdapter firebaseAdapter = new ChatAdapter(mDatabaseReference.child(user.getUid()+" to "+talking_to.getUid()), user.getUid());
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMessage();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}