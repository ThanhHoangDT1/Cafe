package com.androidexam.cafemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.User;

import java.util.List;

//import de.hdodenhof.circleimageview.CircleImageView;

public class staffAdapter extends RecyclerView.Adapter<staffAdapter.ViewHolder> {

    private List<User> staffList;

    private String userId;

    public staffAdapter(List<User> staffList, String userId) {
        this.staffList = staffList;
        this.userId = userId;
    }


    @NonNull
    @Override
    public staffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull staffAdapter.ViewHolder holder, int position) {

        User staff =  staffList.get(position);

        holder.tvNameStaff.setText(staff.getName());
        holder.tvGmailStaff.setText(staff.getEmail());
        holder.tvRolesStaff.setText(staff.getRole());







    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameStaff;

        TextView tvGmailStaff;

        TextView tvRolesStaff;

       // CircleImageView imgStaff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameStaff = itemView.findViewById(R.id.tv_name_staff);
            tvGmailStaff = itemView.findViewById(R.id.tv_gmail_staff);
            tvRolesStaff = itemView.findViewById(R.id.tv_roles_staff);
           // imgStaff = (CircleImageView)itemView.findViewById(R.id.img);
            }
    }
}
