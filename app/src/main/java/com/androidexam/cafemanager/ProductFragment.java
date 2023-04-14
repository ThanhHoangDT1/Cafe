package com.androidexam.cafemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.androidexam.cafemanager.adapter.ProductAdapter;
import com.androidexam.cafemanager.databinding.FragmentProductBinding;
import com.androidexam.cafemanager.model.Product;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private ProductAdapter productAdapter;
    private List<Product> productList;



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
        binding.rcvProducts.setLayoutManager(new GridLayoutManager(getActivity(),2));
        productList= new ArrayList<>();
        productList.add(new Product("","Ca Phe","",20000,"",""));
        productList.add(new Product("","Ca Cao","",15000,"",""));
        productList.add(new Product("","Tra Sua","",30000,"",""));
        productList.add(new Product("","Bac Xiu","",30000,"",""));
        productList.add(new Product("","Ca Phe","",20000,"",""));
        productList.add(new Product("","Ca Cao","",15000,"",""));
        productList.add(new Product("","Tra Sua","",30000,"",""));
        productList.add(new Product("","Bac Xiu","",30000,"",""));
        productAdapter= new ProductAdapter(productList);
        binding.rcvProducts.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}