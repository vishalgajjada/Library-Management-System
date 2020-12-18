package com.velt.libraryadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryadmin.Adapters.PendingReturnsAdapter;
import com.velt.libraryadmin.Adapters.UsersAdapter;
import com.velt.libraryadmin.DataClasses.UserData;

import java.net.Inet4Address;
import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView recyclerView, usersRecyclerView;
    LinearLayout viewBooks,addBook,viewReserves;
    SearchView searchView;
    ArrayList<UserData> userList;
    UsersAdapter usersAdapter;
    PendingReturnsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        recyclerView = findViewById(R.id.recycler_view);
        usersRecyclerView = findViewById(R.id.users_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        userList = new ArrayList<>();
        viewBooks = findViewById(R.id.view_books);
        viewReserves = findViewById(R.id.view_reserves);
        searchView = findViewById(R.id.search_view);
        addBook = findViewById(R.id.add_book);


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
                    ArrayList<UserData> newlist = new ArrayList<>();
                    for (UserData object : userList) {
                        if (object.getUserName().toLowerCase().contains(newText.toLowerCase()) ||
                                object.getUserEmail().contains(newText.toLowerCase())) {
                            newlist.add(object);
                        }
                    }
                    adapter = new PendingReturnsAdapter(LibraryActivity.this, newlist);
                    recyclerView.setAdapter(adapter);
                }
            });
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    UserData list = snapshot.getValue(UserData.class);
                    userList.add(list);
                    ArrayList<UserData> newlist = new ArrayList<>();
                    for(UserData object : userList) {
                        if (object.getBooks() > 0) {
                            newlist.add(object);
                        }
                    }
                    usersAdapter = new UsersAdapter(LibraryActivity.this, userList);
                    adapter = new PendingReturnsAdapter(LibraryActivity.this, newlist);
                    recyclerView.setAdapter(adapter);
                    usersRecyclerView.setAdapter(usersAdapter);
                }
                else{
                    Toast.makeText(LibraryActivity.this, "No data available", Toast.LENGTH_SHORT).show();
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

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LibraryActivity.this, AddBook.class));
            }
        });
        viewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LibraryActivity.this, BooksActivity.class));
            }
        });
        viewReserves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LibraryActivity.this, ReservedBooks.class));
            }
        });
    }
}
