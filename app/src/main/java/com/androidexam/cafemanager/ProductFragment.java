package com.androidexam.cafemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recview;
    myadapterProduct myadapterProduct;

    public ProductFragment() {

    }


    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recview =(RecyclerView)view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<modelProduct> options =
                new FirebaseRecyclerOptions.Builder<modelProduct>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"), modelProduct.class)
                        .build();

        myadapterProduct= new myadapterProduct(options);
        recview.setAdapter(myadapterProduct);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myadapterProduct.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myadapterProduct.stopListening();
    }
}