package com.example.xpressme;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BoardButtonViewHolder extends RecyclerView.ViewHolder {
    protected ImageView buttonImageView;
    protected TextView buttonTextView;
    protected RelativeLayout buttonHolder;

    public BoardButtonViewHolder(View itemView) {
        super(itemView);
        buttonImageView = itemView.findViewById(R.id.button_image_view);
        buttonTextView = itemView.findViewById(R.id.button_label_textview);
        buttonHolder = itemView.findViewById(R.id.button_holder);
    }

    public void bind(BoardButton boardButton) {
        buttonImageView.setImageResource(boardButton.getImgDrawable());
        buttonTextView.setText(boardButton.getButtonLabel());
    }
}



