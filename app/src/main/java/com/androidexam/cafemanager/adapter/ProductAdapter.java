package com.androidexam.cafemanager.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.AddProductActivity;
import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.OderDetail;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private String userId;

    public ProductAdapter(List<Product> productList, String userId) {
        this.productList = productList;
        this.userId = userId;
    }


    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String price = (numberFormat.format(product.getPrice()) + " đ").replace(',', '.');

        holder.tvNamePro.setText(product.getName());
        holder.tvPricePro.setText(price);
        if (!TextUtils.isEmpty(product.getUrlImage())) {
            Picasso.get().load(product.getUrlImage()).error(R.drawable.img).into(holder.imgPro);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPro;
        TextView tvNamePro;
        TextView tvPricePro;
        Button btnBuy;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPro = itemView.findViewById(R.id.img_product_item);
            tvNamePro = itemView.findViewById(R.id.tv_name_pro_item);
            tvPricePro = itemView.findViewById(R.id.tv_price_pro_item);
            btnBuy = itemView.findViewById(R.id.btn_buy);


            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), AddProductActivity.class);
                String id = productList.get(getAbsoluteAdapterPosition()).getId();
                intent.putExtra("idProduct", id);
                itemView.getContext().startActivity(intent);
            });

            btnBuy.setOnClickListener(v -> {
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Carts").child(userId);
                OderDetail oderDetail = new OderDetail();
                Product product = productList.get(getAbsoluteAdapterPosition());

                DatabaseReference cartProductRef = cartRef.child(product.getId());
                oderDetail.setIdProduct(product.getId());
                oderDetail.setQuantity(1);
                oderDetail.setPrice(product.getPrice());

                // Use a transaction to update the cart item quantity if it already exists
                cartProductRef.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        OderDetail existingCartItem = mutableData.getValue(OderDetail.class);

                        if (existingCartItem == null) {
                            // If the cart item doesn't exist, create a new one
                            mutableData.setValue(oderDetail);
                        } else {
                            // If the cart item already exists, update the quantity
                            int quantity = existingCartItem.getQuantity() + 1;
                            existingCartItem.setQuantity(quantity);
                            existingCartItem.setPrice(oderDetail.getPrice());
                            mutableData.setValue(existingCartItem);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {
                        if (error != null) {
                            Toast.makeText(itemView.getContext(), "Error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "Sản Phẩm Đã Thêm Vào Giỏ Hàng", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            });


        }

    }


}

