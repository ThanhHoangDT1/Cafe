package com.androidexam.cafemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.OderDetail;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<OderDetail> cartItems;
    private String userId;

    public CartAdapter(List<OderDetail> cartItems, String userId) {
        this.cartItems = cartItems;
        this.userId = userId;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_cart, parent, false);
        return new CartAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String idProduct = cartItems.get(position).getIdProduct();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

        DatabaseReference oderRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Carts")
                .child(userId)
                .child(idProduct);
        oderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    OderDetail oderDetail = snapshot.getValue(OderDetail.class);
                    holder.tvQuantityPro.setText(String.valueOf(oderDetail.getQuantity()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference productRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Products")
                .child(idProduct);

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    holder.tvNamePro.setText(product.getName());
                    String price = (numberFormat.format(product.getPrice()) + " đ").replace(',', '.');
                    holder.tvPricePro.setText(price);
                    Picasso.get().load(product.getUrlImage()).into(holder.imgPro);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPro;
        ImageView plusImageView;
        ImageView minusImageView;
        TextView tvNamePro;
        TextView tvPricePro;
        TextView tvQuantityPro;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPro = itemView.findViewById(R.id.imgCoffeeImageCart);
            tvNamePro = itemView.findViewById(R.id.txtCoffeeNameCart);
            tvPricePro = itemView.findViewById(R.id.txtPriceCart);
            tvQuantityPro = itemView.findViewById(R.id.txtQuantityCart);
            plusImageView = itemView.findViewById(R.id.btnPlusQuantity);
            minusImageView = itemView.findViewById(R.id.btnMinusQuantity);

            plusImageView.setOnClickListener(view -> {
                setQuantity(1);
            });

            minusImageView.setOnClickListener(view1 -> {
                setQuantity(2);
            });


        }

        private void setQuantity(int type) {
            OderDetail oderDetail = cartItems.get(getAbsoluteAdapterPosition());
            DatabaseReference cartProductRef = FirebaseDatabase.getInstance().getReference()
                    .child("Carts")
                    .child(userId)
                    .child(oderDetail.getIdProduct());
            cartProductRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    OderDetail existingCartItem = mutableData.getValue(OderDetail.class);
                    if (existingCartItem == null) {
                        mutableData.setValue(oderDetail);
                    } else {
                        int quantity = existingCartItem.getQuantity();
                        if (type == 1) {
                            quantity++;
                        } else if (type == 2 && quantity > 0) {
                            quantity--;
                        }
                        existingCartItem.setQuantity(quantity);
                        mutableData.setValue(existingCartItem);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {
                }
            });
        }
    }
}