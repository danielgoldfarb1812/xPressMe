package com.example.xpressme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminCreateBoardActivity extends AppCompatActivity implements BoardButtonAdapter.ButtonClickListener, CreateButtonDialogFragment.ButtonCreationDialogListener{
    TextToSpeech ttsService;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView buttonRecyclerView;
    PopupWindow popupWindow;
    BoardButtonAdapter boardButtonAdapter;
    ArrayList<BoardButton> boardButtonList;
    TextView boardNameTextview;
    View popupView;
    android.widget.Button confirmBoardNameBtn;
    EditText boardNameEdittext;
    androidx.appcompat.widget.AppCompatButton menuBtn, doneBtn;
    ImageView helpBtn;
    private int dialogFragmentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        initTTS();

        // Initialize Firebase
        initFirebase();

        // Initialize views, buttons, and the RecyclerView
        initViews();

        // Pass the activity as a listener to the adapter
        boardButtonAdapter.setButtonClickListener(AdminCreateBoardActivity.this);

        // Set up click listeners for buttons
        initButtons();
        // Initialize the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.create_button_popup_menu, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
    }
    @Override
    public void onButtonClick(int position) {
        // Show the dialog fragment when a button is clicked and pass the position
        showButtonCreationDialog(position);
    }

    private void showButtonCreationDialog(int position) {
        dialogFragmentPosition = position; // Set the dialogFragmentPosition
        CreateButtonDialogFragment dialogFragment = CreateButtonDialogFragment.newInstance(position);
        dialogFragment.show(getSupportFragmentManager(), "ButtonCreationDialog");
    }

    @Override
    public void onButtonCreated(BoardButton boardButton) {
        // Add the created button to the list at the specified position
        int position = dialogFragmentPosition;

        boardButtonList.get(position).setButtonLabel(boardButton.getButtonLabel());
        boardButtonList.get(position).setTtsMessage(boardButton.getTtsMessage());
        boardButtonList.get(position).setImgDrawable(boardButton.getImgDrawable());
        boardButtonAdapter.notifyItemChanged(position);

    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initViews() {
        // Initialize views
        boardNameTextview = findViewById(R.id.board_name_textview);
        menuBtn = findViewById(R.id.menu_btn);
        doneBtn = findViewById(R.id.done_btn);
        confirmBoardNameBtn = findViewById(R.id.btn_confirm_board_name);
        buttonRecyclerView = findViewById(R.id.button_recycler_view);
        boardNameEdittext = findViewById(R.id.board_name_edittext);
        helpBtn = findViewById(R.id.help_icon);

        // Set up the button grid
        setUpButtonGrid();

        // Populate the button list with empty buttons
        populateButtonList();
    }

    private void setUpButtonGrid() {
        // Set up the RecyclerView
        boardButtonList = new ArrayList<>();
        boardButtonAdapter = new BoardButtonAdapter(boardButtonList, this); // Pass the activity context
        buttonRecyclerView.setAdapter(boardButtonAdapter);

        // Use a GridLayoutManager with a span count of 6
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        buttonRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void populateButtonList() {
        for (int i = 0; i < 18; i++) {
            BoardButton emptyBoardButton = new BoardButton("", R.drawable.plus_icon);
            boardButtonList.add(emptyBoardButton);
        }
    }
    private void initTTS() {
        ttsService = new TextToSpeech(AdminCreateBoardActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
    }
    private void speakMessage(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private void initButtons() {
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: fix dimensions for dialog fragment
                CreateBoardInstructionsFragment fragment = new CreateBoardInstructionsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment.show(fragmentManager, "create_board_instructions_dialog");
            }
        });
        // Set up click listeners for buttons
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boardName = boardNameTextview.getText().toString();
                CommunicationBoard newBoard = createBoardObject(boardName, boardButtonList);
                saveBoardToFirestore(newBoard);
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });

        confirmBoardNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardTitle();
            }
        });
    }

    private void saveBoardToFirestore(CommunicationBoard newBoard) {
        try {
            // Create a new Firestore document reference
            DocumentReference boardRef = db.collection("presetBoards").document();

            // Set the board's ID to the document reference's ID
            String boardId = boardRef.getId();
            newBoard.setBoardId(boardId);

            // Create a map with the board data
            Map<String, Object> commBoard = new HashMap<>();
            commBoard.put("boardName", newBoard.getBoardName());
            commBoard.put("boardButtons", newBoard.getButtons());
            commBoard.put("boardId", newBoard.getBoardId());

            // Save the board to Firestore using the document reference
            boardRef.set(commBoard)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utility.showToast(AdminCreateBoardActivity.this, "Successfully created board " + boardNameTextview.getText().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Utility.showToast(CreateBoardActivity.this, e.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            //Utility.showToast(CreateBoardActivity.this, ex.getLocalizedMessage());
        }
    }


    private CommunicationBoard createBoardObject(String boardName, ArrayList<BoardButton> boardButtonArr) {
        return new CommunicationBoard(boardName, boardButtonArr);
    }


    private void showPopupMenu() {
        // Show a popup menu for logout and home page options
        PopupMenu popupMenu = new PopupMenu(AdminCreateBoardActivity.this, menuBtn);
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

    private void handleLogout() {
        // Sign out the current user and navigate to the login screen
        firebaseAuth.signOut();
        startActivity(new Intent(AdminCreateBoardActivity.this, LoginActivity.class));
        finish();
    }

    private void navigateToBoardSelection() {
        // Navigate to the board selection screen
        startActivity(new Intent(AdminCreateBoardActivity.this, BoardSelectionActivity.class));
        finish();
    }

    private void setBoardTitle() {
        // Set the board title to the entered text
        String boardTitle = boardNameEdittext.getText().toString();
        boardNameTextview.setText(boardTitle);
    }


}
