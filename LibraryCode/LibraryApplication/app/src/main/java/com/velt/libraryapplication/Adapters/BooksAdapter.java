package com.velt.libraryapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.velt.libraryapplication.DataClasses.BookDetails;
import com.velt.libraryapplication.DataClasses.BorrowedBooks;
import com.velt.libraryapplication.DataClasses.ReserveData;
import com.velt.libraryapplication.DataClasses.UserData;
import com.velt.libraryapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.myViewHolder> {

    private Context context;
    ArrayList<BookDetails> list;

    public BooksAdapter(Context c, ArrayList<BookDetails> b) {
        context = c;
        list = b;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.books_card,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.author.setText(list.get(position).getAuthor());
        holder.category.setText(list.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView title, author, category,checkOut;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_book_title);
            author = itemView.findViewById(R.id.card_book_author);
            category = itemView.findViewById(R.id.card_book_category);
            checkOut = itemView.findViewById(R.id.check_out);
            final ArrayList<BookDetails> books = new ArrayList<>();

            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference bookReference = FirebaseDatabase.getInstance().getReference("books");
                    bookReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.exists()){
                                final ArrayList<BookDetails> bookDetails2 = new ArrayList<>();
                                BookDetails details = snapshot.getValue(BookDetails.class);
                                books.add(details);
                                for(BookDetails object : list){
                                    if(object.getTitle().toLowerCase().contains(title.getText().toString().toLowerCase())){
                                        bookDetails2.add(object);
                                    }
                                }
                                DatabaseReference bookCount = FirebaseDatabase.getInstance().getReference("books").child(title.getText().toString()).child("copies");
                                bookCount.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Long numberOfCopies = snapshot.getValue(Long.class);
                                            if (numberOfCopies > 0){
                                                checkOutBook(bookDetails2);
                                            }
                                            else{
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Book Unavailable")
                                                        .setMessage("Do you want to reserve the book?")
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        })
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
                                                                final String title = bookDetails2.get(0).getTitle();
                                                                final DatabaseReference reservedRef = FirebaseDatabase.getInstance().getReference("reserved").child(user[0]);
                                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user[0]);
                                                                userRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()){
                                                                            UserData data = snapshot.getValue(UserData.class);
                                                                            String phoneNumber = data.getPhoneNumber();
                                                                            ReserveData reserveData = new ReserveData(title, phoneNumber);
                                                                            reservedRef.setValue(reserveData);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                                Toast.makeText(context, "Book Reserved", Toast.LENGTH_SHORT).show();
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
            });
        }
    }

    private void checkOutBook(final ArrayList<BookDetails> bookDetails2) {
        final DatabaseReference borrowedBooks = FirebaseDatabase.getInstance().getReference("checkedOut");
        final String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String key = bookDetails2.get(0).getTitle();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        final String date = dateFormat.format(calendar.getTime());

        final DatabaseReference userBooks = FirebaseDatabase.getInstance().getReference("users").child(user[0]).child("books");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customView = LayoutInflater.from(context).inflate(R.layout.period_activity,null);
        builder.setView(customView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText period = customView.findViewById(R.id.time_period);
                        String time = period.getText().toString();
                        if (Integer.parseInt(time) < 14){
                            if (Integer.parseInt(time) > 0){
                                final BorrowedBooks book = new BorrowedBooks(bookDetails2.get(0).getTitle(), bookDetails2.get(0).getAuthor(),
                                        bookDetails2.get(0).getCategory(), time, date);
                                borrowedBooks.child(user[0]).child(bookDetails2.get(0).getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()){
                                            userBooks.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        int books = snapshot.getValue(int.class);
                                                        if (books < 3){
                                                            updateContent(user[0], bookDetails2.get(0).getTitle());
                                                            borrowedBooks.child(user[0]).child(key).setValue(book);
                                                            Toast.makeText(context, "Book checked out successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                            Toast.makeText(context, "You cannot check out any more books", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(context, "You have already checked out this book", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void updateContent(String s, String title) {
        final DatabaseReference updateUser = FirebaseDatabase.getInstance().getReference("users").child(s).child("books");
        final DatabaseReference updateBookCount = FirebaseDatabase.getInstance().getReference("books").child(title).child("copies");

        updateUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int checkedOutBooks = snapshot.getValue(Integer.class);
                    int newCheckedOut  = checkedOutBooks + 1;
                    updateUser.setValue(newCheckedOut);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateBookCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Long availableBooks = snapshot.getValue(Long.class);
                    long newAvailable  = availableBooks - 1;
                    updateBookCount.setValue(newAvailable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
