package com.teamgrid.fashhub.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teamgrid.fashhub.DesignerProfilePage;
import com.teamgrid.fashhub.R;
import com.teamgrid.fashhub.models.UserDetail;
import com.teamgrid.fashhub.utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;


public class DesignerListAdapter extends RecyclerView.Adapter<DesignerListAdapter.DesignerViewHolder> implements Filterable {

    private Context context;
    private List<UserDetail> items = new ArrayList();
    private List<UserDetail> filterItems = new ArrayList();
    private ItemFilter itemFilter = new ItemFilter();
    private UserDetail currentUser;

    public DesignerListAdapter(Context conxt, List<UserDetail> items, UserDetail currentUser) {
        this.context = conxt;
        this.items = items;
        this.currentUser = currentUser;
        this.filterItems = items;
    }

    @Override
    public DesignerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.designer_adapter, parent, false);
        return  new DesignerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DesignerViewHolder holder, int position) {
        UserDetail designerDetail = this.filterItems.get(position);
        holder.name.setText(designerDetail.getName());
        holder.phone.setText(designerDetail.getPhone());
        holder.gender.setText(designerDetail.getGender());
        if(designerDetail.getAvaterUrl()!=null){
            Glide.with(context).load(designerDetail.getAvaterUrl())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_person_white_36dp)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() { return this.filterItems.size();}

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter{
     private ItemFilter(){}

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            List<String> list = new ArrayList();
            for(int i=0; i<items.size(); i++)
                list.add(DesignerListAdapter.this.items.get(i).getName());

            List<UserDetail> result_list = new ArrayList(list.size());
            for(int i=0; i<list.size(); i++){
                if(list.get(i).toLowerCase().contains(query))
                    result_list.add(DesignerListAdapter.this.items.get(i));
            }

            results.values = result_list;
            results.count = result_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            DesignerListAdapter.this.filterItems = (List) results.values;
            DesignerListAdapter.this.notifyDataSetChanged();
        }
    }


    public class DesignerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView image;
        TextView name;
        TextView phone;
        TextView gender;

        public DesignerViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.avater);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            gender = (TextView) itemView.findViewById(R.id.gender);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            UserDetail designerClicked = filterItems.get(getLayoutPosition());

            Intent intent = new Intent(context, DesignerProfilePage.class);
            intent.putExtra("viewDesigner", designerClicked);
            intent.putExtra("currentUser", currentUser);
            context.startActivity(intent);
        }
    }

}
