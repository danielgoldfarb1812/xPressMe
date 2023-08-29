package com.example.xpressme;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ButtonViewHolder extends RecyclerView.ViewHolder {
    protected ImageView buttonImageView;
    protected TextView buttonTextView;

    public ButtonViewHolder(View itemView) {
        super(itemView);
        buttonImageView = itemView.findViewById(R.id.button_image_view);
        buttonTextView = itemView.findViewById(R.id.button_label_textview);
    }

    public void bind(Button button) {
        buttonImageView.setImageResource(R.drawable.plus_icon);
    }
}



