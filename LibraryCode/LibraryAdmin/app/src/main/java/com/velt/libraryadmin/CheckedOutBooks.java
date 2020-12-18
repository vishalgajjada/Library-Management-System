package com.velt.libraryadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerResultsIntent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryadmin.Adapters.BorrowedBooksAdapter;
import com.velt.libraryadmin.DataClasses.BorrowedBooks;

import java.util.ArrayList;

public class CheckedOutBooks extends AppCompatActivity {

    RecyclerView booksRecycler;
    ArrayList<BorrowedBooks> borrowedBooks;
    BorrowedBooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_out_books);

        String[] email = getIntent().getStringExtra("email").split("@");

        booksRecycler = findViewById(R.id.checked_out_books);
        booksRecycler.setLayoutManager(new LinearLayoutManager(CheckedOutBooks.this));
        borrowedBooks = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("checkedOut").child(email[0]);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    BorrowedBooks books = snapshot.getValue(BorrowedBooks.class);
                    borrowedBooks.add(books);
                    adapter = new BorrowedBooksAdapter(CheckedOutBooks.this, borrowedBooks);
                    booksRecycler.setAdapter(adapter);
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
