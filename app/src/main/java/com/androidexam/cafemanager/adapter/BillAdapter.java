package com.androidexam.cafemanager.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.BillManagementActivity;
import com.androidexam.cafemanager.DetailStaffActivity;
import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.Oder;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<Oder> oderList;

    public BillAdapter(List<Oder> oderList) {
        this.oderList = oderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_item, parent, false);
        return new BillAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Oder oder=oderList.get(position);
        holder.tvIdBill.setText(oder.getId());
        holder.tvDateCreate.setText(oder.getCreateAt());
        holder.tvCreateBy.setText(oder.getIdStaff());
        holder.tvTotalBill.setText(String.valueOf(oder.getTotalBillAmount()));
    }

    @Override
    public int getItemCount() {
        return oderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIdBill,tvCreateBy,tvDateCreate,tvTotalBill;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdBill=itemView.findViewById(R.id.tv_bill_id);
            tvDateCreate=itemView.findViewById(R.id.tv_order_date_value);
            tvCreateBy=itemView.findViewById(R.id.tv_create_by);
            tvTotalBill=itemView.findViewById(R.id.tv_order_total_value);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), BillManagementActivity.class);
//                Intent intent1 = new Intent(itemView.getContext(), DetailStaffActivity.class);
                String id = oderList.get(getAbsoluteAdapterPosition()).getId();
//                String id1= oderList.get(getAbsoluteAdapterPosition()).getIdStaff();
                intent.putExtra("idOrder", id);
//                intent1.putExtra("idStaffofbill", id1);
                itemView.getContext().startActivity(intent);
//                itemView.getContext().startActivity(intent1);
            });
        }
    }
}
