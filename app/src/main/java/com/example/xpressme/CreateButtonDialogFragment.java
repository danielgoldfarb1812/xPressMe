package com.example.xpressme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

public class CreateButtonDialogFragment extends DialogFragment {
    // Define the interface to communicate data back to the activity
    public interface ButtonCreationDialogListener {
        void onButtonCreated(Button button);
    }

    // Declare necessary views and widgets
    private EditText btnLabelEditText, btnMessageEditText;
    private TextView btnTargetTextView;
    private ImageView imageView;
    private CheckBox hasTargetCheckBox;
    private AppCompatButton saveBtn, cancelBtn, deleteBtn;
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

        return view;
    }

    private void initViews(View view) {
        btnLabelEditText = view.findViewById(R.id.btn_label_edittext);
        btnMessageEditText = view.findViewById(R.id.btn_message_edittext);
        btnTargetTextView = view.findViewById(R.id.btn_target_textview);
        imageView = view.findViewById(R.id.btn_img_src);
        hasTargetCheckBox = view.findViewById(R.id.move_to_board_checkbox);
        saveBtn = view.findViewById(R.id.btn_save);
        cancelBtn = view.findViewById(R.id.btn_cancel);
        deleteBtn = view.findViewById(R.id.btn_delete);
    }

    private void initButtons(View view) {
        // Function to save a button when all required fields are full
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For testing, only create label and message
                String btnLabel = btnLabelEditText.getText().toString();
                String btnMessage = btnMessageEditText.getText().toString();

                Button button = new Button("0", btnLabel);
                button.setTtsMessage(btnMessage);
                Utility.showToast(getContext(), "Button created at pos" + position);

                // Check if the activity implements the listener interface
                if (getActivity() instanceof ButtonCreationDialogListener) {
                    // Send the data to the activity
                    ButtonCreationDialogListener listener = (ButtonCreationDialogListener) getActivity();
                    listener.onButtonCreated(button);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
}
