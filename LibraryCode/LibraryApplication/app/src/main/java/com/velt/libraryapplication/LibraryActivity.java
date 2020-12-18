package com.velt.libraryapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.velt.libraryapplication.Adapters.BooksAdapter;
import com.velt.libraryapplication.DataClasses.BookDetails;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView booksRecyclerView;
    ImageView profile;
    ArrayList<BookDetails> arrayList;
    BooksAdapter adapter;
    SearchView searchView;

    /*@Override
    protected void onStart() {
        super.onStart();
        String[] email = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
        final DatabaseReference reserve = FirebaseDatabase.getInstance().getReference("users").child(email[0]).child("reserve");
        reserve.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    final String reserveStr = snapshot.getValue(String.class);
                    assert reserveStr != null;
                    if (!reserveStr.equals("none")){
                        DatabaseReference available = FirebaseDatabase.getInstance().getReference("books").child(reserveStr).child("copies");
                        available.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Long copiesStr = snapshot.getValue(Long.class);
                                    assert copiesStr != null;
                                    if (copiesStr > 0){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                                        builder.setTitle("Book Availability")
                                                .setMessage("The book you reserves, " + reserveStr + ", is currently available")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        reserve.setValue("none");
                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        booksRecyclerView = findViewById(R.id.books_recycler);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        profile = findViewById(R.id.profile_logo);
        searchView = findViewById(R.id.search_view);

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }

                private void search(String newText) {
                    ArrayList<BookDetails> newlist = new ArrayList<>();
                    for (BookDetails object : arrayList) {
                        if (object.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                                object.getCategory().toLowerCase().contains(newText.toLowerCase()) ||
                                object.getAuthor().toLowerCase().contains(newText.toLowerCase())) {
                            newlist.add(object);
                        }
                    }
                    adapter = new BooksAdapter(LibraryActivity.this, newlist);
                    booksRecyclerView.setAdapter(adapter);
                }
            });

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("books");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        BookDetails details = snapshot.getValue(BookDetails.class);
                        arrayList.add(details);
                        adapter = new BooksAdapter(LibraryActivity.this, arrayList);
                        booksRecyclerView.setAdapter(adapter);
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



            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));
                }
            });


        }


    }
}
