package com.javainstitute.androidcodingexamtest01.Adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.javainstitute.androidcodingexamtest01.R;

import java.sql.SQLOutput;

public class friendsAdapter extends RecyclerView.ViewHolder {

    public TextView userName, email;
    public ImageView userImage;
    public ImageButton addFriendBtn;

    FirebaseFirestore firebaseFirestore;

    public friendsAdapter(@NonNull View itemView) {
        super(itemView);

        firebaseFirestore = FirebaseFirestore.getInstance();

        userName = itemView.findViewById(R.id.textView2);
        email = itemView.findViewById(R.id.textView3);

        userImage = itemView.findViewById(R.id.imageView2);

        addFriendBtn = itemView.findViewById(R.id.imageButton3);

    }
}
