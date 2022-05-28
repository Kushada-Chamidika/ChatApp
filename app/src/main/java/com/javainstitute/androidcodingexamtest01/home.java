package com.javainstitute.androidcodingexamtest01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class home extends AppCompatActivity {

    Toolbar toolbar;

    private static String U_NAME;
    private static String EMAIL;

    FloatingActionButton addFriendsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar2);
        addFriendsBtn = findViewById(R.id.floatingActionButton);

        Intent intent = getIntent();
        U_NAME = intent.getStringExtra("NAME");
        EMAIL = intent.getStringExtra("EMAIL");

        toolbar.setTitle(U_NAME);
        toolbar.setSubtitle(EMAIL);

        addFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, addFriend.class);
                intent.putExtra("NAME", U_NAME);
                intent.putExtra("EMAIL", EMAIL);
                startActivity(intent);
                finish();
            }
        });

    }
}