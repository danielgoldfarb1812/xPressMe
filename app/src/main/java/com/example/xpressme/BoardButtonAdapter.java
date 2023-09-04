package com.example.xpressme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardButtonAdapter extends RecyclerView.Adapter<BoardButtonViewHolder> {
    private ArrayList<BoardButton> boardButtonList;
    private Context context;
    private ButtonClickListener buttonClickListener;
    public void setButtonClickListener(ButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public BoardButtonAdapter(ArrayList<BoardButton> boardButtonList, Context context) {
        this.boardButtonList = boardButtonList;
        this.context = context; // Initialize the context
    }

    public interface ButtonClickListener {
        void onButtonClick(int position);
    }

    @NonNull
    @Override
    public BoardButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        // Inflate the layout for regular buttons
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.button_item_layout, parent, false);
        return new BoardButtonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardButtonViewHolder holder, int position) {
        BoardButton boardButton = boardButtonList.get(position);
        holder.bind(boardButton);
        holder.buttonHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int btnPosition = holder.getAdapterPosition();
                // Check if the position is valid
                if (btnPosition != RecyclerView.NO_POSITION && buttonClickListener != null) {
                    // Notify the listener that a button was clicked and pass the position
                    buttonClickListener.onButtonClick(btnPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardButtonList.size();
    }
}
