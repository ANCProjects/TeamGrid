package com.teamgrid.fashhub.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.teamgrid.fashhub.R;
import com.teamgrid.fashhub.models.Comment;

/**
 * Created by Johnbosco on 07-Sep-17.
 */

public class ChatAdapter
        extends FirebaseRecyclerAdapter<Comment, ChatAdapter.ChatAdapterViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;

    private String uid;
    private boolean textToRed = false;

    public ChatAdapter(DatabaseReference ref, String uid) {
        super(Comment.class, R.layout.chat_item_left, ChatAdapterViewHolder.class, ref);
        this.uid = uid;
    }

    @Override
    public ChatAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new ChatAdapterViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            return new ChatAdapterViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Comment message = getItem(position);

        if (message.getUser().getUid().equals(uid)){
            textToRed = false;
            return RIGHT_MSG;
         }else{
            textToRed = true;
            return LEFT_MSG;
        }
    }


    @Override
    protected void populateViewHolder(ChatAdapterViewHolder viewHolder, Comment model, int position) {
        viewHolder.setTimestamp(model.getTimeStamp());
        if(model.getMessage()!= null){viewHolder.setMessage(model.getMessage());}
       // if(model.getFile()!=null){
        //    if(model.getFile().getType().equalsIgnoreCase("img")){
       //         viewHolder.setImage(model.getFile().getUrl_file());
       //     }else if(model.getFile().getType().equalsIgnoreCase("video")){

       //     }
        //}
       // if(!model.getUser().getUid().equals(uid)){
       //     viewHolder.setAuthor(model.getUser().getName());
       // }
    }


    public class ChatAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvAuthor,txtMessage, tvTimestamp;
        private ImageView ivImage;

        public ChatAdapterViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            txtMessage = (TextView)itemView.findViewById(R.id.message);
            tvAuthor = (TextView)itemView.findViewById(R.id.username);
            ivImage = (ImageView)itemView.findViewById(R.id.image);
            ivImage.setOnClickListener(this);
        }

        public void setMessage(String message){
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(message);
        }

        public void setTimestamp(String timestamp){
            tvTimestamp.setText(convertTimestamp(timestamp));
        }

        public void setAuthor(String username) {
            tvAuthor.setVisibility(View.VISIBLE);
            if(!textToRed){
                tvAuthor.setText(username);
            } else{
                tvAuthor.setTextColor(Color.RED);
                tvAuthor.setText(username);
            }
        }

        public void setImage(String imageUrl){
            ivImage.setVisibility(View.VISIBLE);
            Glide.with(ivImage.getContext()).load(imageUrl)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_person_white_36dp)
                    .into(ivImage);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private CharSequence convertTimestamp(String mileSeconds){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSeconds),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }
}
