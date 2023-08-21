package com.example.xpressme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonViewHolder> {
    private List<Button> buttonList;

    public ButtonAdapter(List<Button> buttonList) {
        this.buttonList = buttonList;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.button_item_layout, parent, false); // Use the empty button layout
        return new ButtonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        // No need to bind data for empty buttons
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }
}
