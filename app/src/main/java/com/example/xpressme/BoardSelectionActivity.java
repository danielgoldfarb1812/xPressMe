package com.example.xpressme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import io.grpc.okhttp.internal.Util;

public class BoardSelectionActivity extends AppCompatActivity {

    ImageView helpIcon;
    String adminUid = AdminUser.getUID();
    ArrayList<CommunicationBoard> boardsList;
    AppCompatButton logoutBtn;
    RecyclerView boardsRecyclerView;
    LinearLayout createNewBoardBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    CommunicationBoardAdapter boardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_selection);
        initFirebase();
        initViews();
        initButtons();


    }
    private void setUpBoardsList() {
        // Set up the RecyclerView
        boardsList = new ArrayList<>();
        boardAdapter = new CommunicationBoardAdapter(boardsList, this); // Pass the activity context
        boardsRecyclerView.setAdapter(boardAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        boardsRecyclerView.setLayoutManager(layoutManager);
    }
    private void populateBoardsList() {
        // get the preset boards collection and populate the recycler view
        db.collection("presetBoards")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                String boardName = document.getString("boardName");
                                String boardId = document.getString("boardId");
                                Object boardButtons = document.get("boardButtons");
                                ArrayList<Object> boardButtonsList = (ArrayList<Object>)boardButtons;
                                Object firstButtonObj = boardButtonsList.get(0);
                                // NEED TO CAST TO HASHMAP AND GET IMGDRAWABLE
                                HashMap<String, Object> firstBtnMap = (HashMap<String, Object>)firstButtonObj;
                                int previewDrawable = (int)(long)firstBtnMap.get("imgDrawable");
                                CommunicationBoard communicationBoard = new CommunicationBoard(boardName);
                                communicationBoard.setBoardId(boardId);
                                communicationBoard.setPreviewDrawable(previewDrawable);
                                boardsList.add(communicationBoard);
                            }
                            boardAdapter.notifyDataSetChanged(); // Notify the adapter of the data change

                        }
                    }

                );
    }




    private void initFirebase() {
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void initButtons() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });
        createNewBoardBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                navigateToBoardCreation();
                finish();
            }
        });
        helpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });
    }

    private void showHelpDialog() {
        final Dialog helpDialog = new Dialog(BoardSelectionActivity.this);
        helpDialog.setContentView(R.layout.board_selection_instructions_fragment);
        helpDialog.getWindow().setLayout(1600, 1200);
        helpDialog.show();
    }

    private void navigateToBoardCreation() {
        Intent intent = new Intent(BoardSelectionActivity.this, AdminCreateBoardActivity.class);
        intent.putExtra("isEditMode", false); // Set the edit mode flag
        startActivity(intent);
        finish();
    }

    private void initViews() {
        logoutBtn = findViewById(R.id.logout_btn);
        boardsRecyclerView = findViewById(R.id.board_recycler_view);
        createNewBoardBtn = findViewById(R.id.create_new_board_btn);
        helpIcon = findViewById(R.id.help_icon);
        // Set up the boards list
        setUpBoardsList();

        // Populate the boards list with preset boards from firestore
        populateBoardsList();
    }



    private void handleLogout() {
        // Sign out the current user and navigate to the login screen
        showLogoutConfirmDialog();

    }

    private void showLogoutConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DarkAlertDialog);
        builder.setTitle("Sign Out Confirmation");
        builder.setMessage("Are you sure you want to sign out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the sign-out action here
                // You can call a method to handle the sign-out logic
                fAuth.signOut();
                startActivity(new Intent(BoardSelectionActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}