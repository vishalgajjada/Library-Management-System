package com.velt.libraryapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.velt.libraryapplication.DataClasses.BookDetails;
import com.velt.libraryapplication.DataClasses.BorrowedBooks;
import com.velt.libraryapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.myViewHolder> {

    private Context context;
    ArrayList<BorrowedBooks> list;

    public ReturnAdapter(Context c, ArrayList<BorrowedBooks> b) {
        context = c;
        list = b;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.return_card, parent, false);
        return new ReturnAdapter.myViewHolder(view);
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

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, category, returnBook;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_two_book_title);
            author = itemView.findViewById(R.id.card_two_book_author);
            category = itemView.findViewById(R.id.card_two_book_category);
            returnBook = itemView.findViewById(R.id.return_book);
            final String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

            returnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String checkOutDate = list.get(getAdapterPosition()).getDate();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                    String currentDate = dateFormat.format(calendar.getTime());
                    calculateFine(currentDate, checkOutDate);
                    updateBookNumbers(title.getText().toString(), user[0]);
                }
            });
        }

        private void calculateFine(String currentDate, String checkOutDate) {
            String[] dateOne = currentDate.split("-");
            String[] dateTwo = checkOutDate.split("-");

            final int monthOne = Integer.parseInt(dateOne[0]);
            final int monthTwo = Integer.parseInt(dateTwo[0]);

            final int dayOne = Integer.parseInt(dateOne[1]);
            final int dayTwo = Integer.parseInt(dateTwo[1]);

            String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
            DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference("checkedOut")
                    .child(user[0]).child(title.getText().toString()).child("period");
            timeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String period = snapshot.getValue(String.class);
                    int periodInt = Integer.parseInt(period);
                    if (monthOne == monthTwo) {
                        if (dayOne - dayTwo > periodInt) {
                            int late = dayOne - dayTwo - periodInt;
                            int fine = late * 2;
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("The book was returned " + late + " days late. Pay a fine of " + fine)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
                                            list.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), list.size());

                                            DatabaseReference removeBook = FirebaseDatabase.getInstance().getReference("checkedOut")
                                                    .child(user[0]).child(title.getText().toString());
                                            removeBook.removeValue();
                                        }
                                    });
                        } else {
                            list.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), list.size());

                            String[] user = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
                            Toast.makeText(context, "The book was returned on time. No fine", Toast.LENGTH_SHORT).show();
                            DatabaseReference removeBook = FirebaseDatabase.getInstance().getReference("checkedOut")
                                    .child(user[0]).child(title.getText().toString());
                            removeBook.removeValue();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void updateBookNumbers(String titleStr, String userId) {
        final DatabaseReference updateUser = FirebaseDatabase.getInstance().getReference("users").child(userId).child("books");
        final DatabaseReference updateBookCount = FirebaseDatabase.getInstance().getReference("books").child(titleStr).child("copies");

        updateUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int checkedOutBooks = snapshot.getValue(Integer.class);
                    int newCheckedOut  = checkedOutBooks - 1;
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
                    long newAvailable  = availableBooks + 1;
                    updateBookCount.setValue(newAvailable);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}



