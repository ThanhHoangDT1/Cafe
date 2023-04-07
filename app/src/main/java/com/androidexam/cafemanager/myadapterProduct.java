package com.androidexam.cafemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.ktx.Firebase;

public class myadapterProduct extends FirebaseRecyclerAdapter<modelProduct,myadapterProduct.myviewholder> {


    public myadapterProduct(@NonNull FirebaseRecyclerOptions<modelProduct> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull modelProduct model) {
        holder.matext.setText(model.getMaSP());
        holder.tentext.setText(model.getTenSP());
        holder.giatext.setText(model.getGiaSP());
        Glide.with(holder.img1.getContext()).load(model.getPurl()).into(holder.img1);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
   {
       ImageView img1;
       TextView matext,tentext,giatext;


       public myviewholder(@NonNull View itemView) {
           super(itemView);

           img1 =itemView.findViewById(R.id.img1);
           tentext=itemView.findViewById(R.id.tentext);
           matext=itemView.findViewById(R.id.matext);
           giatext=itemView.findViewById(R.id.giatext);
       }
   }
}
