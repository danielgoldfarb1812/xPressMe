package com.example.xpressme;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommunicationBoardAdapter extends RecyclerView.Adapter<CommunicationBoardViewHolder>{
    private ArrayList<CommunicationBoard> boardsList;
    private Context context;
    private BoardClickListener boardClickListener;
    public void setButtonClickListener(BoardClickListener listener) {
        this.boardClickListener = listener;
    }

    public CommunicationBoardAdapter(ArrayList<CommunicationBoard> boardsList, Context context) {
        this.boardsList = boardsList;
        this.context = context; // Initialize the context
    }

    public interface BoardClickListener {
        void onBoardClick(int position);
    }

    @NonNull
    @Override
    public CommunicationBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        // Inflate the layout for regular buttons
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_item_layout, parent, false);
        return new CommunicationBoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunicationBoardViewHolder holder, final int position) {
        CommunicationBoard communicationBoard = boardsList.get(position);
        Log.d("TAG", "onBindViewHolder: " + communicationBoard.getBoardId());
        holder.bind(communicationBoard);
        holder.boardItemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boardId = communicationBoard.getBoardId();
                String boardName = communicationBoard.getBoardName();

                // Pass the board ID to MainActivity
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("boardId", boardId);
                intent.putExtra("boardName", boardName);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return boardsList.size();
    }
}
