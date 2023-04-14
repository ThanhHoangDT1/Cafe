package com.androidexam.cafemanager.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPro = itemView.findViewById(R.id.img_product_item);
            tvNamePro = itemView.findViewById(R.id.tv_name_pro_item);
            tvPricePro = itemView.findViewById(R.id.tv_price_pro_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), AddProductActivity.class);
                    String id=productList.get(getAbsoluteAdapterPosition()).getId();
                    intent.putExtra("idProduct",id);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
