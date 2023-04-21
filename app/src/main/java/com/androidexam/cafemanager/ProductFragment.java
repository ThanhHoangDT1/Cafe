package com.androidexam.cafemanager;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.androidexam.cafemanager.adapter.ProductAdapter;
import com.androidexam.cafemanager.databinding.FragmentProductBinding;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference databaseRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.rcvProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        productList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        productAdapter = new ProductAdapter(productList,uid);
        binding.rcvProducts.setAdapter(productAdapter);


        viewListProduct();

        viewAddProduct();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void viewAddProduct(){
        binding.btnAddProduct.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), AddProductActivity.class);
            startActivity(intent);
        });
    }
    public void viewListProduct(){
        databaseRef = FirebaseDatabase.getInstance().getReference("Products");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}