package com.javainstitute.androidcodingexamtest01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.javainstitute.androidcodingexamtest01.Adapter.friendsAdapter;
import com.javainstitute.androidcodingexamtest01.Model.Friends;
import com.javainstitute.androidcodingexamtest01.Model.User;

import java.util.Calendar;
import java.util.List;

public class addFriend extends AppCompatActivity {

    Toolbar toolbar;

    SearchView searchView;

    private static String U_NAME;
    private static String EMAIL;

    RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<User, friendsAdapter> recyclerAdapter;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        toolbar = findViewById(R.id.toolbar2);
        recyclerView = findViewById(R.id.friendsLoadRecycler);
        searchView = findViewById(R.id.searchViewFriends);

        Intent intent = getIntent();
        U_NAME = intent.getStringExtra("NAME");
        EMAIL = intent.getStringExtra("EMAIL");

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query loadFriends = firebaseFirestore.collection("Users").whereNotEqualTo("email", EMAIL);
        FirestoreRecyclerOptions firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<User>().setQuery(loadFriends, User.class).build();

        recyclerAdapter = new FirestoreRecyclerAdapter<User, friendsAdapter>(firestoreRecyclerOptions) {
            @NonNull
            @Override
            public friendsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_view, parent, false);
                return new friendsAdapter(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull friendsAdapter holder, int position, @NonNull User model) {

                String username = model.getUsername();
                String email = model.getEmail();

                holder.userName.setText(username);
                holder.email.setText(email);

                holder.addFriendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Task<QuerySnapshot> data = firebaseFirestore.collection("Friends").whereEqualTo("userEmail", EMAIL).whereEqualTo("friendEmail", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Friends> fdList = task.getResult().toObjects(Friends.class);
                                    if (fdList.size() > 0) {
                                        Toast.makeText(addFriend.this, "Already Friends!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Friends friends = new Friends(EMAIL, email, username, String.valueOf(Calendar.getInstance().getTime()));
                                        firebaseFirestore.collection("Friends").add(friends).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                Toast.makeText(addFriend.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(addFriend.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(addFriend.this, "Error -> " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addFriend.this, "Error -> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(addFriend.this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        recyclerAdapter.startListening();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query loadFriends = firebaseFirestore.collection("Users").whereNotEqualTo("email", EMAIL).whereEqualTo("email", query);
                FirestoreRecyclerOptions firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<User>().setQuery(loadFriends, User.class).build();

                recyclerAdapter = new FirestoreRecyclerAdapter<User, friendsAdapter>(firestoreRecyclerOptions) {
                    @NonNull
                    @Override
                    public friendsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_view, parent, false);
                        return new friendsAdapter(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull friendsAdapter holder, int position, @NonNull User model) {

                        String username = model.getUsername();
                        String email = model.getEmail();


                        holder.userName.setText(username);
                        holder.email.setText(email);

                        holder.addFriendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Task<QuerySnapshot> data = firebaseFirestore.collection("Friends").whereEqualTo("userEmail", EMAIL).whereEqualTo("friendEmail", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<Friends> fdList = task.getResult().toObjects(Friends.class);
                                            if (fdList.size() > 0) {
                                                Toast.makeText(addFriend.this, "Already Friends!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Friends friends = new Friends(EMAIL, email, username, String.valueOf(Calendar.getInstance().getTime()));
                                                firebaseFirestore.collection("Friends").add(friends).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        Toast.makeText(addFriend.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(addFriend.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(addFriend.this, "Error -> " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(addFriend.this, "Error -> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                    }
                };

                recyclerView.setLayoutManager(new LinearLayoutManager(addFriend.this));
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
                recyclerAdapter.startListening();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Query loadFriends = firebaseFirestore.collection("Users").whereNotEqualTo("email", EMAIL).orderBy("email").startAt(newText).endAt(newText + "\uf8ff");
                FirestoreRecyclerOptions firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<User>().setQuery(loadFriends, User.class).build();

                recyclerAdapter = new FirestoreRecyclerAdapter<User, friendsAdapter>(firestoreRecyclerOptions) {
                    @NonNull
                    @Override
                    public friendsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_view, parent, false);
                        return new friendsAdapter(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull friendsAdapter holder, int position, @NonNull User model) {

                        String username = model.getUsername();
                        String email = model.getEmail();


                        holder.userName.setText(username);
                        holder.email.setText(email);

                        holder.addFriendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Task<QuerySnapshot> data = firebaseFirestore.collection("Friends").whereEqualTo("userEmail", EMAIL).whereEqualTo("friendEmail", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<Friends> fdList = task.getResult().toObjects(Friends.class);
                                            if (fdList.size() > 0) {
                                                Toast.makeText(addFriend.this, "Already Friends!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Friends friends = new Friends(EMAIL, email, username, String.valueOf(Calendar.getInstance().getTime()));
                                                firebaseFirestore.collection("Friends").add(friends).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        Toast.makeText(addFriend.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(addFriend.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(addFriend.this, "Error -> " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(addFriend.this, "Error -> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                    }
                };

                recyclerView.setLayoutManager(new LinearLayoutManager(addFriend.this));
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
                recyclerAdapter.startListening();
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(addFriend.this, home.class);
        intent.putExtra("NAME", U_NAME);
        intent.putExtra("EMAIL", EMAIL);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}