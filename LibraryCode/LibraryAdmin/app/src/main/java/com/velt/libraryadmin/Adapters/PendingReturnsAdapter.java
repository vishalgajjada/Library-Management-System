package com.velt.libraryadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryadmin.CheckedOutBooks;
import com.velt.libraryadmin.DataClasses.BookDetails;
import com.velt.libraryadmin.DataClasses.BorrowedBooks;
import com.velt.libraryadmin.DataClasses.UserData;
import com.velt.libraryadmin.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PendingReturnsAdapter extends RecyclerView.Adapter<PendingReturnsAdapter.myViewHolder>{

    private Context context;
    ArrayList<UserData> list;


    public PendingReturnsAdapter(Context c, ArrayList<UserData> u) {
        context = c;
        list = u;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pending_returns_card,parent,false);
        return new PendingReturnsAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {

        holder.userEmail.setText(list.get(position).getUserEmail());
        holder.userName.setText(list.get(position).getUserName());
        holder.numberOfBooks.setText(String.valueOf(list.get(position).getBooks()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView userEmail, userName, numberOfBooks;

        public myViewHolder(@NonNull final View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.user_email);
            userName = itemView.findViewById(R.id.user_name);
            numberOfBooks = itemView.findViewById(R.id.number_of_books);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(new Intent(context, CheckedOutBooks.class));
                    intent.putExtra("email", userEmail.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }
}

