package com.androidexam.cafemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.OderDetail;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.ViewHolder> {

    private List<OderDetail> oderDetailList;

    public BillDetailAdapter(List<OderDetail> oderDetailList) {
        this.oderDetailList = oderDetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new BillDetailAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OderDetail oderDetail=oderDetailList.get(position);

        long totalPrice= oderDetail.getQuantity()*oderDetail.getPrice();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String price=(numberFormat.format(totalPrice) + " đ").replace(',', '.');
        holder.priceProduct.setText(price);
        holder.quantityProduct.setText(String.valueOf(oderDetail.getQuantity()));

        DatabaseReference productRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Products")
                .child(oderDetail.getIdProduct());
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    holder.nameProduct.setText(product.getName());
                    Picasso.get().load(product.getUrlImage()).into(holder.imageProduct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return oderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView nameProduct;
        TextView priceProduct;
        TextView quantityProduct;
        ImageView imageProduct;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.img_product_order);
            nameProduct = itemView.findViewById(R.id.tv_pro_name_order);
            priceProduct = itemView.findViewById(R.id.tv_pro_price_order);
            quantityProduct = itemView.findViewById(R.id.tv_pro_quantity_order);

        }
    }
}
