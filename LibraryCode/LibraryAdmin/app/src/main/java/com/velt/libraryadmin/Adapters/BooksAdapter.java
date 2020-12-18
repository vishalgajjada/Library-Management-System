package com.velt.libraryadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryadmin.BooksActivity;
import com.velt.libraryadmin.DataClasses.BookDetails;
import com.velt.libraryadmin.DataClasses.BorrowedBooks;
import com.velt.libraryadmin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.myViewHolder> {

    Context context;
    ArrayList<BookDetails> list;

    public BooksAdapter(Context c, ArrayList<BookDetails> l) {
        context = c;
        list = l;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.books_card_two,parent, false);
        return new BooksAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bookTitle.setText(list.get(position).getTitle());
        holder.bookAuthor.setText(list.get(position).getAuthor());
        holder.bookCategory.setText(list.get(position).getCategory());
        Toast.makeText(context, "Data Available" + list.size(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookAuthor, bookCategory;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            bookAuthor = itemView.findViewById(R.id.card_book_author);
            bookTitle = itemView.findViewById(R.id.card_book_title);
            bookCategory = itemView.findViewById(R.id.card_book_category);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view1 = inflater.inflate(R.layout.changes,null);
                    final TextView category, author, copies,saveChange, changeTxt;
                    copies = view1.findViewById(R.id.card_number_of_books);
                    author = view1.findViewById(R.id.card_author_change);
                    category = view1.findViewById(R.id.card_category_changes);
                    saveChange = view1.findViewById(R.id.save_changes);
                    changeTxt = view1.findViewById(R.id.change_txt);
                    final EditText changeEditText = view1.findViewById(R.id.changes_edit_text);

                    copies.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            copies.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_green));
                            author.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_white));
                            category.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_white));
                            String text = "Copies";
                            changeTxt.setText(text);
                        }
                    });
                    author.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            copies.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_white));
                            author.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_green));
                            category.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_white));
                            String text = "Author";
                            changeTxt.setText(text);
                        }
                    });
                    category.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            copies.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_white));
                            author.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_white));
                            category.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_edges_green));
                            String text = "Category";
                            changeTxt.setText(text);
                        }
                    });


                    builder.setView(view1);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    saveChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String pathOne = changeTxt.getText().toString();
                            if (pathOne.toLowerCase().equals("author") ||
                                    pathOne.toLowerCase().equals("category")
                                    ){
                                DatabaseReference changeRef = FirebaseDatabase.getInstance().getReference("books")
                                        .child(bookTitle.getText().toString()).child(pathOne.toLowerCase());
                                if (changeEditText.getText().toString().length() != 0){
                                    String change = changeEditText.getText().toString();
                                    changeRef.setValue(change);
                                    notifyDataSetChanged();
                                    alertDialog.dismiss();
                                }
                                else{
                                    Toast.makeText(context , "Enter the new value and try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(pathOne.toLowerCase().equals("copies")) {
                                DatabaseReference changeRef = FirebaseDatabase.getInstance().getReference("books")
                                        .child(bookTitle.getText().toString()).child(pathOne.toLowerCase());
                                if (changeEditText.getText().toString().length() != 0) {
                                    String change = changeEditText.getText().toString();
                                    changeRef.setValue(Integer.valueOf(change));
                                    notifyDataSetChanged();
                                    alertDialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(context, "Select a value to change first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
            });

        }
    }
}
