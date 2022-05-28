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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.javainstitute.androidcodingexamtest01.Model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView signUp;
    EditText email, password;

    Button signIn;

    ImageButton pwdShw;

    boolean isPwdVissible;

    ProgressDialog pd;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.button);
        signUp = findViewById(R.id.textView);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        pwdShw = findViewById(R.id.imageButton);

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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                String emailData = email.getText().toString().trim();
                String passwordData = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailData)) {
                    email.setError("Email is required!");
                    return;
                }

                if (!emailData.matches(emailPattern)) {
                    email.setError("Not a Valid Email!");
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

                firebaseFirestore.collection("Users").whereEqualTo("email", emailData).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        if (documents.size() > 0) {

                            firebaseAuth.signInWithEmailAndPassword(emailData, passwordData).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        List<User> user = queryDocumentSnapshots.toObjects(User.class);

                                        if (user.size() > 0) {

                                            User acUser = user.get(0);

                                            pd.dismiss();



                                            Intent intent = new Intent(MainActivity.this, home.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("NAME", acUser.getUsername());
                                            intent.putExtra("EMAIL", acUser.getEmail());
                                            startActivity(intent);
                                            finish();

                                        }

                                    } else {

                                        pd.dismiss();

                                        Toast.makeText(MainActivity.this, "Login Error! ->" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                        } else {

                            pd.dismiss();

                            Toast.makeText(MainActivity.this, "Not Allowed to SIGN IN!", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();

                        Toast.makeText(MainActivity.this, "Not Registered User!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}