package com.androidexam.cafemanager.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.AddProductActivity;
import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> cartItems;

    private OnQuantityChangeListener onQuantityChangeListener;
    private Context mContext;
    private DatabaseReference mDatabase;
    private String userId;
    private SharedPreferences sharedPreferences;
    private OnQuantityChangeListener listener;



    // Khởi tạo DatabaseReference cho Firebase Realtime Database
   // DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // Lấy SharedPreferences để lấy userId đã được lưu trữ khi đăng nhập
//    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
//            .child("users")
//            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//            .child("cart");



    public CartAdapter(Context context, List<Product> cartItems) {
        mContext = context;
        this.cartItems = cartItems;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("USER", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "");
        mDatabase = mDatabase.child("users").child(userId).child("cart");
    }
    public void setOnQuantityChangeListener(OnQuantityChangeListener onQuantityChangeListener) {
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_cart, parent, false);




        return new CartAdapter.ViewHolder(itemView);
    }
    public void setCartItems(List<Product> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }


    @SuppressLint("RecyclerView")
    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Product product = cartItems.get(position);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String price = (numberFormat.format(product.getSum()) + " đ").replace(',', '.');

        String quantity = String.valueOf(product.getquantity());
        holder.tvNamePro.setText(product.getName());
        holder.tvQuantityPro.setText(quantity);
        holder.tvPricePro.setText(price);
        if (!TextUtils.isEmpty(product.getUrlImage())) {
            Picasso.get().load(product.getUrlImage()).error(R.drawable.img).into(holder.imgPro);
        }



        holder.plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = cartItems.get(position);
                product.increaseQuantity();
                mDatabase.child(product.getId()).child("quantity").setValue(product.getquantity());
                if (onQuantityChangeListener != null) {
                    onQuantityChangeListener.onIncreaseQuantity(position);

                }

                notifyDataSetChanged();
            }
        });

        holder.minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = cartItems.get(position);
                product.decreaseQuantity();
                mDatabase.child(product.getId()).child("quantity").setValue(product.getquantity());
                if (onQuantityChangeListener != null) {
                    onQuantityChangeListener.onDecreaseQuantity(position);
                }
                notifyDataSetChanged();
            }
        });


    }

    public interface OnQuantityChangeListener {
        void onIncreaseQuantity(int position);
        void onDecreaseQuantity(int position);
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

            plusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onIncreaseQuantity(getAdapterPosition());
                    }
                }
            });

            minusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onDecreaseQuantity(getAdapterPosition());
                    }
                }
            });
        }



    }




}