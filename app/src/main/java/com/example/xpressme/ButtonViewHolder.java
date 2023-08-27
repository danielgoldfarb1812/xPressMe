package com.example.xpressme;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ButtonViewHolder extends RecyclerView.ViewHolder {
    protected ImageView buttonImageView;


    public ButtonViewHolder(View itemView) {
        super(itemView);
        buttonImageView = itemView.findViewById(R.id.button_image_view);

    }

    public void bind(Button button) {
        buttonImageView.setImageResource(R.drawable.plus_icon);
    }
}



