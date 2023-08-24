package com.example.xpressme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CreateBoardActivity extends AppCompatActivity implements ButtonAdapter.ButtonClickListener, CreateButtonDialogFragment.ButtonCreationDialogListener{
    Button[] buttonArr = new Button[18];
    FirebaseAuth firebaseAuth;
    private RecyclerView buttonRecyclerView;
    private PopupWindow popupWindow;
    private ButtonAdapter buttonAdapter;
    private List<Button> buttonList;
    TextView boardNameTextview;
    View popupView;
    android.widget.Button confirmBoardNameBtn;
    EditText boardNameEdittext;
    androidx.appcompat.widget.AppCompatButton menuBtn, doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);



        // Initialize Firebase
        initFirebase();

        // Initialize views, buttons, and the RecyclerView
        initViews();

        // Pass the activity as a listener to the adapter
        buttonAdapter.setButtonClickListener(CreateBoardActivity.this);

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
        // הפונקציה ליצירת כפתור חדש ושמירתו במערך
        //פה צריך לפתוח תפריט של יצירת כפתור
        // ולאחר סיום, נשמור את הכפתור במערך של הכפתורים
        Button newButton = new Button("0", "");
        showButtonCreationDialog();


    }

    private void showButtonCreationDialog() {
        CreateButtonDialogFragment dialogFragment = new CreateButtonDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "ButtonCreationDialog");
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

        // Set up the button grid
        setUpButtonGrid();

        // Populate the button list with empty buttons
        populateButtonList();
    }

    private void setUpButtonGrid() {
        // Set up the RecyclerView
        buttonList = new ArrayList<>();
        buttonAdapter = new ButtonAdapter(buttonList, this); // Pass the activity context
        buttonRecyclerView.setAdapter(buttonAdapter);

        // Use a GridLayoutManager with a span count of 6
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        buttonRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void populateButtonList() {
        // Populate the button list with empty buttons
        for (int i = 0; i < 18; i++) {
            Button emptyButton = new Button(String.valueOf(i), "");
            buttonList.add(emptyButton);
        }
    }

    private void initButtons() {
        // Set up click listeners for buttons
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

    private void showPopupMenu() {
        // Show a popup menu for logout and home page options
        PopupMenu popupMenu = new PopupMenu(CreateBoardActivity.this, menuBtn);
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
        startActivity(new Intent(CreateBoardActivity.this, LoginActivity.class));
        finish();
    }

    private void navigateToBoardSelection() {
        // Navigate to the board selection screen
        startActivity(new Intent(CreateBoardActivity.this, MainActivity.class));
        finish();
    }

    private void setBoardTitle() {
        // Set the board title to the entered text
        String boardTitle = boardNameEdittext.getText().toString();
        boardNameTextview.setText(boardTitle);
    }

    @Override
    public void onButtonCreated(Button button) {

    }
}
