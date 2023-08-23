package com.example.xpressme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonViewHolder> {

    private List<Button> buttonList;
    private Context context;
    private ButtonClickListener buttonClickListener;
    public void setButtonClickListener(ButtonClickListener listener){
        this.buttonClickListener = listener;
    }

    public ButtonAdapter(List<Button> buttonList, Context context) {
        this.buttonList = buttonList;
        this.context = context; // Initialize the context
    }
    public interface ButtonClickListener {
        void onButtonClick(int position);
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            // Inflate the layout for regular buttons
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.button_item_layout, parent, false);


        return new ButtonViewHolder(itemView);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        Button button = buttonList.get(position);
        holder.bind(button);

        // Handle click events for regular buttons
        holder.buttonImageView.setImageResource(R.drawable.plus_icon);
        holder.buttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int btnPosition = holder.getAdapterPosition(); // Get the button position (id)
                // Check if the position is valid
                if (btnPosition != RecyclerView.NO_POSITION && buttonClickListener != null) {
                    // Notify the listener that a button was clicked and pass the position
                    buttonClickListener.onButtonClick(btnPosition);
                }
            }
        });

    }

    private void showMessage(String message) {
        // Create and show a toast message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }


}
