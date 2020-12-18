package com.velt.libraryadmin.Adapters;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryadmin.DataClasses.BorrowedBooks;
import com.velt.libraryadmin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.myViewHolder> {

    Context context;
    ArrayList<BorrowedBooks> list;

    public BorrowedBooksAdapter(Context c, ArrayList<BorrowedBooks> l) {
        context = c;
        list = l;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.books_card,parent, false);
        return new BorrowedBooksAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bookTitle.setText(list.get(position).getTitle());
        holder.bookAuthor.setText(list.get(position).getAuthor());
        holder.bookCategory.setText(list.get(position).getCategory());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        String currentDate = dateFormat.format(calendar.getTime());
        String checkOutDate = list.get(position).getDate();

        String[] dateOne = currentDate.split("-");
        String[] dateTwo = checkOutDate.split("-");

        int monthOne = Integer.parseInt(dateOne[0]);
        int monthTwo = Integer.parseInt(dateTwo[0]);

        int dayOne = Integer.parseInt(dateOne[1]);
        int dayTwo = Integer.parseInt(dateTwo[1]);

        if (monthOne == monthTwo) {
            if (dayOne - dayTwo > 14) {
                int late = dayOne - dayTwo - 14;
                holder.bookFine.setText(String.valueOf(late));
            } else {
                int late = dayOne - dayTwo;
                holder.bookFine.setText(String.valueOf(late));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookAuthor, bookCategory, bookFine;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            bookAuthor = itemView.findViewById(R.id.card_book_author);
            bookTitle = itemView.findViewById(R.id.card_book_title);
            bookCategory = itemView.findViewById(R.id.card_book_category);
            bookFine = itemView.findViewById(R.id.total_fine);

        }
    }
}
