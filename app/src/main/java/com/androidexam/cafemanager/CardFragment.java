package com.androidexam.cafemanager;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidexam.cafemanager.adapter.CartAdapter;
import com.androidexam.cafemanager.databinding.FragmentCardBinding;
import com.androidexam.cafemanager.model.Oder;
import com.androidexam.cafemanager.model.OderDetail;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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


    private void createBillAndSaveToDatabase(List<OderDetail> cartItems) {
        DatabaseReference billsRef = FirebaseDatabase.getInstance().getReference("Bills");
        String billId = billsRef.push().getKey(); // Tạo key ngẫu nhiên cho bill mới
        long total = 0;
        for (OderDetail item : cartItems) {
            total += item.getQuantity() * item.getPrice();
            billsRef.child(billId).child("Pro").child(item.getIdProduct()).setValue(item);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        Oder bill = new Oder(billId, userId, total, currentDateAndTime);
        billsRef.child(billId).setValue(bill);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Carts");
        userRef.child(userId).removeValue(); // Xóa giỏ hàng của người dùng sau khi đã thanh toán
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
                long total=0;
                for (OderDetail i:cartItemList
                ) {
                    total +=i.getQuantity()*i.getPrice();

                }
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                String totalprice = (numberFormat.format(total) + " đ").replace(',', '.');
                binding.sum.setText(totalprice);
                binding.payment.setOnClickListener(v -> createBillAndSaveToDatabase(cartItemList));

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
