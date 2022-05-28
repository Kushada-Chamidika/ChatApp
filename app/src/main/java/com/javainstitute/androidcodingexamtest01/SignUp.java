package com.javainstitute.androidcodingexamtest01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.javainstitute.androidcodingexamtest01.Model.User;

public class SignUp extends AppCompatActivity {

    TextView signIn;
    EditText email, username, password;

    Button signUpBtn;

    ImageButton pwdShw;

    boolean isPwdVissible;

    ProgressDialog pd;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpBtn = findViewById(R.id.button4);
        signIn = findViewById(R.id.textView4);
        email = findViewById(R.id.editTextTextEmailAddress4);
        username = findViewById(R.id.editTextTextEmailAddress5);
        password = findViewById(R.id.editTextTextPassword4);
        pwdShw = findViewById(R.id.imageButton4);

        pd = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        pwdShw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPwdVissible) {
                    pwdShw.setImageResource(R.drawable.ic_show);

                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPwdVissible = false;
                } else {
                    pwdShw.setImageResource(R.drawable.ic_hide);

                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPwdVissible = true;
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                String emailData = email.getText().toString().trim();
                String usernameData = username.getText().toString().trim();
                String passwordData = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailData)) {
                    email.setError("Email is required!");
                    return;
                }

                if (!emailData.matches(emailPattern)) {
                    email.setError("Not a Valid Email!");
                    return;
                }

                if (TextUtils.isEmpty(usernameData)) {
                    username.setError("Username is required!");
                    return;
                }

                if (TextUtils.isEmpty(passwordData)) {
                    password.setError("Password is required!");
                    return;
                }

                if (passwordData.length() < 8) {
                    password.setError("Password must be at least 8 characters!");
                    return;
                }

                pd.setMessage("Please Wait!");
                pd.show();

                firebaseAuth.createUserWithEmailAndPassword(emailData, passwordData).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String userId = firebaseAuth.getCurrentUser().getUid();

                            User user = new User(userId, emailData, usernameData);

                            firebaseFirestore.collection("Users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    pd.dismiss();

                                    Toast.makeText(SignUp.this, "Registration Success!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    pd.dismiss();

                                    Toast.makeText(SignUp.this, "Registration Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {

                            pd.dismiss();

                            Toast.makeText(SignUp.this, "Error! ->" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

    }
}