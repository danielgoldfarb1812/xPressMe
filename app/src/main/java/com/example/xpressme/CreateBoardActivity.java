package com.example.xpressme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class CreateBoardActivity extends AppCompatActivity {
    private RecyclerView buttonRecyclerView;
    private ButtonAdapter buttonAdapter;
    private List<Button> buttonList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        // Initialize the RecyclerView
        buttonRecyclerView = findViewById(R.id.button_recycler_view);

        // Set up the buttonList (you'll need to populate this list with your Button objects)
        buttonList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            // Create empty Button objects and add them to the list
            Button emptyButton = new Button(String.valueOf(i), ""); // You can customize the uId and label as needed
            buttonList.add(emptyButton);
        }
        // Initialize and set up the ButtonAdapter
        buttonAdapter = new ButtonAdapter(buttonList);
        buttonRecyclerView.setAdapter(buttonAdapter);

        // Set up the GridLayoutManager with a span count of 5
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        buttonRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);


    }
}