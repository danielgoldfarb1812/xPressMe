package com.example.xpressme;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CommunicationBoardViewHolder extends RecyclerView.ViewHolder{
    protected TextView boardNameTextview;
    protected LinearLayout boardItemHolder;

    public CommunicationBoardViewHolder(View itemView) {
        super(itemView);
        boardNameTextview = itemView.findViewById(R.id.board_name_recycler_textview);
        boardItemHolder = itemView.findViewById(R.id.board_item_holder);
    }

    public void bind(CommunicationBoard communicationBoard) {
        boardNameTextview.setText(communicationBoard.getBoardName());
    }
}
