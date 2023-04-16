package com.androidexam.cafemanager.adapter;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import android.content.Context;


import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private DatabaseReference mDatabase;

    private List<Product> productList;
    private static List<String> selectedItems; // danh sách các ID của các món nước được chọn
    private String userId; // id của nhân viên đang đăng nhập hiện tại

    public ProductAdapter(List<Product> productList, String userId) {
        this.productList = productList;
        selectedItems = new ArrayList<>();
        this.userId = userId;
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        holder.bind(product, selectedItems.contains(product.getId())); // truyền vào giá trị boolean để kiểm tra xem món nước đó đã được chọn hay chưa
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

        public void bind(final Product item, boolean isSelected) {
            tvNamePro.setText(item.getName());
            // categoryTextView.setText(item.getCategory());
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            String price = (numberFormat.format(item.getPrice()) + " đ").replace(',', '.');

            tvPricePro.setText(price);
            Picasso.get().load(item.getUrlImage()).into(imgPro); // sử dụng thư viện Picasso để load hình ảnh từ URL

            btnBuy.setSelected(isSelected); // set trạng thái của button chọn sản phẩm

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnBuy.isSelected()) {
                        btnBuy.setSelected(false);
                        selectedItems.remove(item.getId());

                        // Xóa sản phẩm khỏi giỏ hàng của người dùng đó
                        mDatabase.child("users").child(userId).child("cart").child(item.getId()).removeValue();
                    } else {
                        btnBuy.setSelected(true);
                        selectedItems.add(item.getId());

                        // Thêm sản phẩm vào giỏ hàng của người dùng đó
                        DatabaseReference cartRef = mDatabase.child("users").child(userId).child("cart").child(item.getId());
                        cartRef.child("name").setValue(item.getName());
                        cartRef.child("quantity").setValue(item.getquantity()+1);
                        cartRef.child("price").setValue(item.getPrice());
                        cartRef.child("imageUrl").setValue(item.getUrlImage());
                    }
                }
            });


        }


    }
}
