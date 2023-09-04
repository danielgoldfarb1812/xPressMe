package com.example.xpressme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class CreateButtonDialogFragment extends DialogFragment implements ImageSelectionFragment.ImageSelectionListener{
    // TODO: add logic to select button image from existing images (drawable)


    AppCompatButton btnSampleImg;
    // Define the TextToSpeech object to use
    private TextToSpeech ttsService;
    // Define the interface to communicate data back to the activity


    public interface ButtonCreationDialogListener {
        void onButtonCreated(BoardButton boardButton);
    }

    // Declare necessary views and widgets
    private EditText btnLabelEditText, btnMessageEditText;
    private TextView btnTargetTextView, btnTargetLabel;
    private ImageView btnImgHolder;
    private CheckBox hasTargetCheckBox;
    private AppCompatButton saveBtn, cancelBtn, deleteBtn, testSpeechBtn;
    private int position; // Added to track which button was clicked

    // Create a new instance of the dialog fragment with a position argument
    public static CreateButtonDialogFragment newInstance(int position) {
        CreateButtonDialogFragment fragment = new CreateButtonDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_button_popup_menu, container, false);

        // Retrieve the position from arguments
        position = getArguments().getInt("position", -1);

        initViews(view);
        initButtons(view);
        initTTS(view);

        return view;
    }

    private void initTTS(View view) {
        // Initialize the TextToSpeech engine in your onCreate() or onStart() method
        ttsService = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
    }
    // Implement a method to speak the message
    private void speakMessage(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            ttsService.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private void initViews(View view) {
        btnLabelEditText = view.findViewById(R.id.btn_label_edittext);
        btnMessageEditText = view.findViewById(R.id.btn_message_edittext);
        btnTargetTextView = view.findViewById(R.id.btn_target_textview);
        btnImgHolder = view.findViewById(R.id.btn_img_holder);
        hasTargetCheckBox = view.findViewById(R.id.move_to_board_checkbox);
        saveBtn = view.findViewById(R.id.btn_save);
        cancelBtn = view.findViewById(R.id.btn_cancel);
        deleteBtn = view.findViewById(R.id.btn_delete);
        testSpeechBtn = view.findViewById(R.id.btn_test_audio);
        btnTargetLabel = view.findViewById(R.id.btn_target_label);
        btnSampleImg = view.findViewById(R.id.btn_sample_image);
    }

    private void initButtons(View view) {
        btnSampleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show the image selection fragment
                ImageSelectionFragment imageSelectionFragment = new ImageSelectionFragment();
                imageSelectionFragment.setTargetFragment(CreateButtonDialogFragment.this, 0);
                imageSelectionFragment.show(getFragmentManager(), "image_selection_fragment");
            }
        });

        hasTargetCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (hasTargetCheckBox.isChecked()){
                    btnTargetTextView.setVisibility(View.VISIBLE);
                    btnTargetLabel.setVisibility(View.VISIBLE);
                }
                else{
                    btnTargetTextView.setVisibility(View.GONE);
                    btnTargetLabel.setVisibility(View.GONE);
                }
            }
        });
        // onClickListener to test button message using TTS library
        testSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the string out of the message edittext and sound it out
                String messageText = btnMessageEditText.getText().toString();
                speakMessage(messageText);
            }
        });
        // Function to save a button when all required fields are full
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnLabel = btnLabelEditText.getText().toString();
                String btnMessage = btnMessageEditText.getText().toString();
                int btnDrawable;
                // find a way to get the uploaded image and set it here
                if (btnImgHolder.getTag() != null){
                    btnDrawable = (int)btnImgHolder.getTag();
                }
                else{
                    Utility.showToast(getContext(), "Please choose an image!");
                    return;
                }
                BoardButton boardButton = new BoardButton(btnLabel, btnDrawable);
                boardButton.setTtsMessage(btnMessage);

                // set the saved button to the array (which will be sent as parameter to the board when done)
                CreateBoardActivity.boardButtonArr[position] = boardButton;
                // Check if the activity implements the listener interface
                if (getActivity() instanceof ButtonCreationDialogListener) {
                    // Send the data to the activity
                    ButtonCreationDialogListener listener = (ButtonCreationDialogListener) getActivity();
                    listener.onButtonCreated(boardButton);
                }
                // Close the dialog
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancellationAlert();
            }
        });
    }

    private void showCancellationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DarkAlertDialog);
        builder.setTitle("Confirmation")
                .setMessage("Do you want to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, stay in the dialog
                    }
                })
                .show();
    }
    @Override
    public void onImageSelected(int drawableResourceId) {
        // Handle the selected image here
        // You can set the image in your ImageView or save it as needed
        btnImgHolder.setImageResource(drawableResourceId);
        btnImgHolder.setTag(drawableResourceId);
    }

}
