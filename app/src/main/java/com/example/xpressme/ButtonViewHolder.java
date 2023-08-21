package com.example.xpressme;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ButtonViewHolder extends RecyclerView.ViewHolder {
    private ImageView buttonImageView;

    public ButtonViewHolder(View itemView) {
        super(itemView);
        buttonImageView = itemView.findViewById(R.id.button_image_view);
    }

    public void bind(Button button) {
        // You can set image resources or other attributes for the image view here
        // For empty buttons, you might set a default empty image or background
        buttonImageView.setImageResource(R.drawable.plus_icon);
    }
}



