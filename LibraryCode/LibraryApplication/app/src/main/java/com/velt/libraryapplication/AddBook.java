package com.velt.libraryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.velt.libraryapplication.DataClasses.BookDetails;

public class AddBook extends AppCompatActivity {

    EditText name,author,category,copies;
    TextView done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        name = findViewById(R.id.book_title);
        author = findViewById(R.id.book_author);
        category = findViewById(R.id.book_category);
        copies = findViewById(R.id.available_copies);
        done = findViewById(R.id.done_add_book);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameStr, authorStr, categoryStr, copiesStr;

                nameStr = name.getText().toString();
                authorStr  = author.getText().toString();
                categoryStr = category.getText().toString();
                copiesStr = copies.getText().toString();
                
                DatabaseReference checkBook = FirebaseDatabase.getInstance().getReference("books").child(nameStr);
                checkBook.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("books");
                            if (nameStr.length() != 0 && authorStr.length() != 0 && categoryStr.length() != 0){
                                long copiesInt = Long.parseLong(copiesStr);
                                BookDetails details = new BookDetails(nameStr, authorStr, categoryStr, copiesInt);
                                database.child(nameStr).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddBook.this, "Book added", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(AddBook.this, "Book not added, try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(AddBook.this,  name.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(AddBook.this, "This book already exists in the database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
