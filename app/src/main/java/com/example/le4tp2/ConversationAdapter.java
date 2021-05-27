package com.example.le4tp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    ArrayList<Conversation> list;
    Context context;
    private static RecyclerViewListener itemListener;

    public ConversationAdapter(Context c, RecyclerViewListener itemListener,ArrayList<Conversation> conversations){
        context = c;
        this.list = conversations;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ConversationAdapter.ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.conversation_adapter_row, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.convName.setText(list.get(position).getTheme());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView convName;

        public ConversationViewHolder(@NonNull View itemView){
            super(itemView);
            convName = itemView.findViewById(R.id.conversation_row_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(ConversationAdapter.this.list.get(this.getLayoutPosition()));
        }
    }
}
