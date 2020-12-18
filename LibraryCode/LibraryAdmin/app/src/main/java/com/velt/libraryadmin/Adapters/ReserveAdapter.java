package com.velt.libraryadmin.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.velt.libraryadmin.DataClasses.ReserveData;
import com.velt.libraryadmin.MainActivity;
import com.velt.libraryadmin.R;

import java.util.ArrayList;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.myViewHolder> {

    Context context;
    ArrayList<ReserveData> list;

    public ReserveAdapter(Context c, ArrayList<ReserveData> l) {
        context = c;
        list = l;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserves_card,parent,false);
        return new ReserveAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.title.setText(list.get(position).getBookTitle());
        holder.phone.setText(list.get(position).getUserPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView title, phone;
        ImageView call;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reserved_book_title);
            phone = itemView.findViewById(R.id.reserved_phone_number);
            call = itemView.findViewById(R.id.call_user);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    String phoneStr = phone.getText().toString();
                    String phoneUri = "tel:" + phoneStr;
                    callIntent.setData(Uri.parse(phoneUri));

                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(callIntent);
                }
            });
        }
    }
}
