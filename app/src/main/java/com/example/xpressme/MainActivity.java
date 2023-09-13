package com.example.xpressme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements BoardButtonAdapter.ButtonClickListener {
    String boardId, boardName;
    ArrayList<BoardButton> boardButtonList;
    AppCompatButton editBtn, menuBtn;
    TextView boardNameTextview;
    RecyclerView boardBtnRecyclerView;
    BoardButtonAdapter boardButtonAdapter;
    TextToSpeech ttsService;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardId = getIntent().getStringExtra("boardId");
        boardName = getIntent().getStringExtra("boardName");
        initViews();
        initButtons();

        boardButtonAdapter.setButtonClickListener(MainActivity.this);
        initTTS();

        // Initialize Firebase
        initFirebase();

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initTTS() {
        ttsService = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
    }

    private void initButtons() {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implement logic for editing current board
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });
    }
    private void setUpButtonGrid() {
        // Set up the RecyclerView
        boardButtonList = new ArrayList<>();
        boardButtonAdapter = new BoardButtonAdapter(boardButtonList, this); // Pass the activity context
        boardBtnRecyclerView.setAdapter(boardButtonAdapter);

        // Use a GridLayoutManager with a span count of 6
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        boardBtnRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void populateButtonList(String boardId) {
        DocumentReference boardRef = db.collection("presetBoards").document(boardId);
        boardRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Check if the document exists
                    List<Map<String, Object>> boardButtons = (List<Map<String, Object>>) documentSnapshot.get("boardButtons");
                    if (boardButtons != null) {
                        // Now, you have the list of boardButtons, and you can process it as needed
                        for (Map<String, Object> buttonData : boardButtons) {
                            String buttonLabel = (String) buttonData.get("buttonLabel");
                            String ttsMessage = (String) buttonData.get("ttsMessage");
                            int btnDrawable = (int)(long) buttonData.get("imgDrawable");
                            // You can add this button data to your boardButtonList or perform any other actions
                            if (btnDrawable != R.drawable.plus_icon){
                                BoardButton boardButton = new BoardButton(buttonLabel, btnDrawable, ttsMessage);
                                boardButtonList.add(boardButton);
                                boardButtonAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        // The boardButtons field is null or doesn't exist
                    }
                } else {
                    // The document doesn't exist
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors here
            }
        });
    }



    private void showErrorMessage(String message) {
        Utility.showToast(this, message);
    }


    private void showPopupMenu() {
        // Show a popup menu for logout and home page options
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);
        popupMenu.getMenu().add("Board Selection");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();

        // Set click listeners for menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                handleMenuItemClick(menuItem);
                return true;
            }
        });
    }

    private void handleMenuItemClick(MenuItem menuItem) {
        // Handle menu item clicks (e.g., logout or return to board selection)
        if (menuItem.getTitle().equals("Logout")) {
            handleLogout();
        } else if (menuItem.getTitle().equals("Board Selection")) {
            navigateToBoardSelection();
        }
    }

    private void navigateToBoardSelection() {
        // Navigate to the board selection screen
        startActivity(new Intent(MainActivity.this, BoardSelectionActivity.class));
        finish();
    }

    private void handleLogout() {
        // Sign out the current user and navigate to the login screen
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void initViews() {
        editBtn = findViewById(R.id.edit_btn);
        menuBtn = findViewById(R.id.menu_btn);
        boardNameTextview = findViewById(R.id.board_name_textview);
        boardBtnRecyclerView = findViewById(R.id.button_recycler_view);
        setUpButtonGrid();
        if (boardId != null) {
            // Now, you have the board ID, and you can use it to fetch the corresponding board's buttons from Firebase
            populateButtonList(boardId);
            boardNameTextview.setText(boardName);
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DarkAlertDialog);
        builder.setMessage("Do you want to logout or exit the app?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Logout
                        // Your logout code here
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Exit
                        finishAffinity(); // Closes all activities
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onButtonClick(int position) {
        String message = boardButtonList.get(position).getTtsMessage();
        speakMessage(message);
    }

    private void speakMessage(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}