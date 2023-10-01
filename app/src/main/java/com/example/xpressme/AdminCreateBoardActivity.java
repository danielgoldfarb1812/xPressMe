package com.example.xpressme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// מסך יצירת לוח preset - מובנים מראש
public class AdminCreateBoardActivity extends AppCompatActivity implements BoardButtonAdapter.ButtonClickListener, CreateButtonDialogFragment.ButtonCreationDialogListener {
    // flag to check for edit mode
    boolean isEditMode;
    // board id to get from main activity
    String boardId, boardName;

    // הכרזה על האלמנטים שיהיו בשימוש
    TextToSpeech ttsService; // משתנה לשירות הטקסט לקול
    FirebaseAuth firebaseAuth; // משתנה לטיפול באימות ב-Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // משתנה לגישה ל-Firebase Firestore
    RecyclerView buttonRecyclerView; // RecyclerView לתצוגת כפתורים
    PopupWindow popupWindow; // PopupWindow להצגת תפריט קופץ
    BoardButtonAdapter boardButtonAdapter; // מתאם (Adapter) לכפתורי הלוח
    ArrayList<BoardButton> boardButtonList; // רשימת כפתורים בלוח
    TextView boardNameTextview; // TextView לשם הלוח
    View popupView; // View ל-PopupWindow
    android.widget.Button confirmBoardNameBtn; // כפתור לאישור שם הלוח
    EditText boardNameEdittext; // תיבת טקסט להזנת שם הלוח
    androidx.appcompat.widget.AppCompatButton menuBtn, doneBtn, deleteBoardBtn; // כפתורי תפריט וסיום
    ImageView helpBtn; // כפתור להצגת הוראות
    private int dialogFragmentPosition; // משתנה לאחסון מיקום פריט הרשימה שנבחר ליצירה

    //יצירת המסך
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        // set the edit mode flag to true or false according to which way we entered this activity
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if (isEditMode){
            boardId = getIntent().getStringExtra("boardId");
            boardName = getIntent().getStringExtra("boardName");
        }

        // קריאה לפעולה המגדירה את הtext to speech
        initTTS();

        // קריאה לפעולה המגדירה את Firebase
        initFirebase();

        // קריאה לפעולה המגדירה את כל הviews
        initViews();

        // קישור המתאם של כפתורי הלוח למסך הנוכחי לצורך האזנה ללחיצה על כפתור בלוח
        boardButtonAdapter.setButtonClickListener(AdminCreateBoardActivity.this);

        // הגדרת מאזיני לחיצה על כל הכפתורים
        initButtons();

        // הגדרת החלון הקופץ לצורך יצירת כפתור
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.create_button_popup_menu, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
    }

    // פונקציה זו מתבצעת כאשר לוחצים על כפתור ברשימת הכפתורים
    @Override
    public void onButtonClick(int position) {
        // הצג את דיאלוג היצירה של כפתור כאשר לוחצים על כפתור ושלח את המיקום
        showButtonCreationDialog(position);
    }

    // הצג דיאלוג ליצירת כפתור וקבע את המיקום שנבחר
    private void showButtonCreationDialog(int position) {
        dialogFragmentPosition = position; // קבע את המיקום במשתנה dialogFragmentPosition
        CreateButtonDialogFragment dialogFragment = CreateButtonDialogFragment.newInstance(position);
        dialogFragment.show(getSupportFragmentManager(), "ButtonCreationDialog");
    }

    // פונקציה זו מתבצעת כאשר הכפתור נוצר ומשנה את המידע שלו ברשימה
    @Override
    public void onButtonCreated(BoardButton boardButton) {
        // הוסף את הכפתור שנוצר לרשימה בהתאם למיקום שנבחר
        int position = dialogFragmentPosition;

        boardButtonList.get(position).setButtonLabel(boardButton.getButtonLabel());
        boardButtonList.get(position).setTtsMessage(boardButton.getTtsMessage());
        boardButtonList.get(position).setImgDrawable(boardButton.getImgDrawable());
        //הודעה לadapter שכפתור במיקום מסוים התעדכן ובכך עדכון התצוגה של הכפתור בהתאם
        boardButtonAdapter.notifyItemChanged(position);
    }

    // פונקציה לאתחול של Firebase Authentication
    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // פונקציה לאתחול של אלמנטי ממשק משתמש והתצוגה
    private void initViews() {
        // אתחול אלמנטים בממשק המשתמש
        boardNameTextview = findViewById(R.id.board_name_textview);
        menuBtn = findViewById(R.id.menu_btn);
        doneBtn = findViewById(R.id.done_btn);
        confirmBoardNameBtn = findViewById(R.id.btn_confirm_board_name);
        buttonRecyclerView = findViewById(R.id.button_recycler_view);
        boardNameEdittext = findViewById(R.id.board_name_edittext);
        deleteBoardBtn = findViewById(R.id.delete_board_btn);
        if (isEditMode){
            deleteBoardBtn.setVisibility(View.VISIBLE);
        }
        else{
            deleteBoardBtn.setVisibility(View.GONE);
        }
        helpBtn = findViewById(R.id.help_icon);
        if (boardName != null){
            boardNameTextview.setText(boardName);
        }
        // קביעת תצוגת הכפתורים ברשימה
        setUpButtonGrid();

        // מילוי הרשימה בכפתורים ריקים בהתאם למספר המקפים
        populateButtonList();
    }

    // קביעת תצוגת הכפתורים ברשימה
    private void setUpButtonGrid() {
        // יצירת ה-RecyclerView
        boardButtonList = new ArrayList<>();
        boardButtonAdapter = new BoardButtonAdapter(boardButtonList, this); // העברת הפעילות כמאזין
        buttonRecyclerView.setAdapter(boardButtonAdapter);

        // שימוש ב-GridLayoutManager עם 6 עמודות
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        buttonRecyclerView.setLayoutManager(gridLayoutManager);
    }

    // מילוי הרשימה בכפתורים ריקים
    private void populateButtonList() {
        // if we're not in edit mode, we're in new board mode
        if (isEditMode == false){
            for (int i = 0; i < 18; i++) {
                BoardButton emptyBoardButton = new BoardButton("", R.drawable.plus_icon);
                boardButtonList.add(emptyBoardButton);
            }
        }
        else{
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

                                    BoardButton boardButton = new BoardButton(buttonLabel, btnDrawable, ttsMessage);
                                    boardButtonList.add(boardButton);
                                    boardButtonAdapter.notifyDataSetChanged();

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
    }

    // אתחול של TextToSpeech
    private void initTTS() {
        ttsService = new TextToSpeech(AdminCreateBoardActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
    }

    // השמעת הודעה באמצעות TextToSpeech
    private void speakMessage(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private void deleteBoardFromFirestore(String boardId) {
        try {
            // Create a reference to the board document in Firestore
            DocumentReference boardRef = db.collection("presetBoards").document(boardId);

            // Delete the board document from Firestore
            boardRef.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utility.showToast(AdminCreateBoardActivity.this, "Board deleted successfully");
                            startActivity(new Intent(AdminCreateBoardActivity.this, BoardSelectionActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the delete operation
                        }
                    });
        } catch (Exception ex) {
            // Handle any exceptions that may occur
        }
    }

    // אתחול של כפתורים
    private void initButtons() {
        deleteBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirmation dialog to delete board
                showDeleteBoardConfirmationDialog();


            }
        });
        // טיפול בלחיצה על כפתור ההוראות
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();

            }
        });

        // טיפול בלחיצה על כפתור הסיום
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boardName = boardNameTextview.getText().toString();
                CommunicationBoard updatedBoard = createBoardObject(boardName, boardButtonList);

                if (isEditMode) {
                    // If in edit mode, update the existing board in Firestore
                    updateBoardInFirestore(boardId, updatedBoard);
                } else {
                    // If not in edit mode, create a new board in Firestore
                    saveBoardToFirestore(updatedBoard);
                }
            }
        });


        // טיפול בלחיצה על כפתור התפריט
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });

        // טיפול בלחיצה על כפתור אישור שם הלוח
        confirmBoardNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoardTitle();
            }
        });
    }

    private void showDeleteBoardConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DarkAlertDialog);
        builder.setTitle("Delete board confirmation");
        builder.setMessage("Are you sure you want to delete this board?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBoardFromFirestore(boardId);
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

    private void showHelpDialog() {
        final Dialog helpDialog = new Dialog(AdminCreateBoardActivity.this);
        helpDialog.setContentView(R.layout.create_board_instructions_fragment);
        helpDialog.getWindow().setLayout(1600, 1200);
        helpDialog.show();
    }
    private void updateBoardInFirestore(String boardId, CommunicationBoard updatedBoard) {
        try {
            // Create a reference to the existing board document in Firestore
            DocumentReference boardRef = db.collection("presetBoards").document(boardId);

            // Create a map with the updated data for the board
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("boardName", updatedBoard.getBoardName());
            updatedData.put("boardButtons", updatedBoard.getButtons());

            // Update the board document in Firestore with the new data
            boardRef.update(updatedData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utility.showToast(AdminCreateBoardActivity.this, "Board updated successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the update operation
                        }
                    });
        } catch (Exception ex) {
            // Handle any exceptions that may occur
        }
    }

    // שמירת הלוח ל-Firestore
    private void saveBoardToFirestore(CommunicationBoard newBoard) {
        try {
            // יצירת מצביע למסמך חדש ב-Firestore
            DocumentReference boardRef = db.collection("presetBoards").document();

            // קביעת המזהה של הלוח למזהה של המסמך ב-Firestore
            String boardId = boardRef.getId();
            newBoard.setBoardId(boardId);

            // יצירת מפתח-ערך עם המידע של הלוח
            Map<String, Object> commBoard = new HashMap<>();
            commBoard.put("boardName", newBoard.getBoardName());
            commBoard.put("boardButtons", newBoard.getButtons());
            commBoard.put("boardId", newBoard.getBoardId());

            // שמירת הלוח ל-Firestore באמצעות המצביע למסמך
            boardRef.set(commBoard)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utility.showToast(AdminCreateBoardActivity.this, "Successfully added board " + boardNameTextview.getText().toString());
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

    // יצירת אובייקט לוח תקשורת
    private CommunicationBoard createBoardObject(String boardName, ArrayList<BoardButton> boardButtonArr) {
        return new CommunicationBoard(boardName, boardButtonArr);
    }

    // הצגת תפריט קופץ
    private void showPopupMenu() {
        // הצג תפריט קופץ עם אפשרויות התנתקות וחזרה לבחירת הלוח
        PopupMenu popupMenu = new PopupMenu(AdminCreateBoardActivity.this, menuBtn);
        popupMenu.getMenu().add("Board Selection");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();

        // הגדר מאזין ללחיצה על פריטי התפריט
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                handleMenuItemClick(menuItem);
                return true;
            }
        });
    }

    // טיפול בלחיצה על פריטי התפריט
    private void handleMenuItemClick(MenuItem menuItem) {
        // טיפול בלחיצה על פריטי התפריט (לדוגמה: התנתקות או חזרה לבחירת לוח)
        if (menuItem.getTitle().equals("Logout")) {
            handleLogout();
        } else if (menuItem.getTitle().equals("Board Selection")) {
            navigateToBoardSelection();
        }
    }

    // התנתקות מהחשבון הנוכחי וניווט למסך ההתחברות
    private void handleLogout() {
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
                firebaseAuth.signOut();
                startActivity(new Intent(AdminCreateBoardActivity.this, LoginActivity.class));
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

    // ניווט למסך בחירת לוח
    private void navigateToBoardSelection() {
        startActivity(new Intent(AdminCreateBoardActivity.this, BoardSelectionActivity.class));
        finish();
    }

    // הגדרת שם הלוח
    private void setBoardTitle() {
        // הגדר את שם הלוח לטקסט שהוזן
            String boardTitle = boardNameEdittext.getText().toString();
            boardNameTextview.setText(boardTitle);
    }
}
