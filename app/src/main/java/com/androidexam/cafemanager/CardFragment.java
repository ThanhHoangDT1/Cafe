package com.androidexam.cafemanager;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidexam.cafemanager.adapter.CartAdapter;
import com.androidexam.cafemanager.databinding.FragmentCardBinding;
import com.androidexam.cafemanager.model.OderDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    private FragmentCardBinding binding;
    private CartAdapter cartAdapter;

    private List<OderDetail> cartItemList;
    private String userId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER", MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.rcvProInCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartItemList= new ArrayList<>();
        cartAdapter= new CartAdapter(cartItemList,userId);
        binding.rcvProInCart.setAdapter(cartAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference cartsRef = FirebaseDatabase.getInstance().getReference("Carts").child(userId);
        cartsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItemList.clear();
                for (DataSnapshot oderDetailSnapshot : dataSnapshot.getChildren()) {
                    OderDetail oderDetail = oderDetailSnapshot.getValue(OderDetail.class);
                    cartItemList.add(oderDetail);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
