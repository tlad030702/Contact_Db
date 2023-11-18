package com.example.contact_db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<byte[]> contact_image;
    private Context context;
    private ArrayList contact_id, contact_name, contact_dob, contact_email;

    CustomAdapter(Context context, ArrayList contact_id, ArrayList contact_name, ArrayList contact_dob, ArrayList contact_email, ArrayList<byte[]> contact_image){
        this.context = context;
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_dob = contact_dob;
        this.contact_email = contact_email;
        this.contact_image = contact_image;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.data_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, int position) {
        holder.contact_name.setText(String.valueOf(contact_name.get(position)));
        holder.contact_dob.setText(String.valueOf(contact_dob.get(position)));
        holder.contact_email.setText(String.valueOf(contact_email.get(position)));
        byte[] imageBytes = contact_image.get(position);
        if (imageBytes != null) {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.contact_image.setImageBitmap(imageBitmap);
        } else {
            holder.contact_image.setImageResource(R.drawable.avatar1);
        }

    }

    @Override
    public int getItemCount() {
        return contact_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView contact_image;
        TextView contact_id, contact_name, contact_dob, contact_email;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            contact_name = itemView.findViewById(R.id.contact_name_txt);
            contact_dob = itemView.findViewById(R.id.contact_dob_txt);
            contact_email = itemView.findViewById(R.id.contact_email_txt);
            contact_image = itemView.findViewById(R.id.imageView2);
        }
    }

    public void updateData(ArrayList<String> contact_id, ArrayList<String> contact_name, ArrayList<String> contact_dob, ArrayList<String> contact_email, ArrayList<byte[]> contact_image) {
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_dob = contact_dob;
        this.contact_email = contact_email;
        this.contact_image = contact_image;
        notifyDataSetChanged();
    }
}
