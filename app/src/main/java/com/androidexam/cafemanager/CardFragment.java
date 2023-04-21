package com.androidexam.cafemanager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidexam.cafemanager.adapter.CartAdapter;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    private CartAdapter adapter;

    private RecyclerView recyclerView;
    private List<Product> cartItemList;
    private ValueEventListener cartListener;
    private DatabaseReference mDatabase;
    private String userId;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo DatabaseReference cho Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Lấy SharedPreferences để lấy userId đã được lưu trữ khi đăng nhập
        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "");
        mDatabase = mDatabase.child("users").child(userId).child("cart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        recyclerView = view.findViewById(R.id.list_pro_in_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CartAdapter(requireContext(),new ArrayList<>());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItemList = new ArrayList<>();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    Product item = productSnapshot.getValue(Product.class);
                    cartItemList.add(item);
                }
                adapter.setCartItems(cartItemList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        };
        mDatabase.addValueEventListener(cartListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (cartListener != null) {
            mDatabase.removeEventListener(cartListener);
        }
    }
}
