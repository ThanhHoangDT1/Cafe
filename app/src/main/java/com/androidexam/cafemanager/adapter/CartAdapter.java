package com.androidexam.cafemanager.adapter;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> cartItems;

    public CartAdapter(List<Product> cartItems) {
        this.cartItems = cartItems;
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


    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = cartItems.get(position);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String price = (numberFormat.format(product.getPrice()) + " đ").replace(',', '.');

        String quantity = String.valueOf(product.getquantity());
        holder.tvNamePro.setText(product.getName());
        holder.tvQuantityPro.setText(quantity);
        holder.tvPricePro.setText(price);
        Picasso.get().load(product.getUrlImage()).into(holder.imgPro);

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPro;
        TextView tvNamePro;
        TextView tvPricePro;
        TextView tvQuantityPro;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPro = itemView.findViewById(R.id.imgCoffeeImageCart);
            tvNamePro = itemView.findViewById(R.id.txtCoffeeNameCart);
            tvPricePro = itemView.findViewById(R.id.txtPriceCart);
            tvQuantityPro = itemView.findViewById(R.id.txtQuantityCart);


        }



    }
}