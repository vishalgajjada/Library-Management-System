package com.velt.libraryadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryadmin.CheckedOutBooks;
import com.velt.libraryadmin.DataClasses.UserData;
import com.velt.libraryadmin.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.myViewHolder>{

    private Context context;
    ArrayList<UserData> list;


    public UsersAdapter(Context c, ArrayList<UserData> u) {
        context = c;
        list = u;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.users_card,parent,false);
        return new UsersAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        holder.userName.setText(list.get(position).getUserEmail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView userName, deleteUser;

        public myViewHolder(@NonNull final View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_card_user_name);
            deleteUser = itemView.findViewById(R.id.delete_user);

            deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String[] email = userName.getText().toString().split("@");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete User")
                            .setMessage("Are you sure tou want to delete this user and all their data?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(email[0]);
                            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("checkedOut").child(email[0]);

                            userRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "User deleted successfully" + email[0], Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(context, "Failed try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dataRef.removeValue();
                            list.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), list.size());
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        }
    }
}

