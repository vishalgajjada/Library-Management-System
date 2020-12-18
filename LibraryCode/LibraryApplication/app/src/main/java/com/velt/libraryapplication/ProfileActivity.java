package com.velt.libraryapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryapplication.Adapters.ReturnAdapter;
import com.velt.libraryapplication.DataClasses.BorrowedBooks;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView checkedOutRecyclerView;
    ArrayList<BorrowedBooks> list;
    ReturnAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        checkedOutRecyclerView = findViewById(R.id.checked_out_books_recyclerview);
        checkedOutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

        DatabaseReference books = FirebaseDatabase.getInstance().getReference("checkedOut").child(user[0]);
        books.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    BorrowedBooks borrowedBooks = snapshot.getValue(BorrowedBooks.class);
                    list.add(borrowedBooks);
                    adapter = new ReturnAdapter(ProfileActivity.this, list);
                    checkedOutRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
