package com.example.xpressme;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {
    public static void showToast(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
    // get preset board from firebase
    public static CollectionReference getPresetBoards(){
        return FirebaseFirestore.getInstance().collection("presetBoards");
    }
}
