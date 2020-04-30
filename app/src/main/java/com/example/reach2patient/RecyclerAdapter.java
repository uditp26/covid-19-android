package com.example.reach2patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<RecyclerItem> items;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView post;
        public TextView timestamp;
        public TextView contact;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.post_body);
            timestamp = itemView.findViewById(R.id.post_timestamp);
            contact = itemView.findViewById(R.id.post_contact);
        }
    }

    public RecyclerAdapter(ArrayList<RecyclerItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerItem item = items.get(position);

        holder.post.setText(item.getPost());
        holder.timestamp.setText(item.getTimestamp());
        holder.contact.setText(item.getContact());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
