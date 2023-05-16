package com.androidexam.cafemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.androidexam.cafemanager.adapter.BillAdapter;
import com.androidexam.cafemanager.databinding.ActivityDetailStaffBinding;
import com.androidexam.cafemanager.model.Oder;
import com.androidexam.cafemanager.model.Product;
import com.androidexam.cafemanager.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailStaffActivity extends AppCompatActivity {

    private String idStaff;

    private List<Oder> oderList;

    private BillAdapter billAdapter;


    private ActivityDetailStaffBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailStaffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        idStaff = intent.getStringExtra("idStaff");
        System.out.println(idStaff +"abd");
        loadDataUser();

        binding.rcvBill.setLayoutManager(new LinearLayoutManager(this));
        oderList = new ArrayList<>();
        billAdapter = new BillAdapter(oderList);
        binding.rcvBill.setAdapter(billAdapter);

        getAllOrders();

        binding.btnCancel.setOnClickListener(v->finish());

        clickDelete();



        }
    private void loadDataUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        DatabaseReference userRef = usersRef.child(idStaff);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User staff = snapshot.getValue(User.class);
                    binding.txtNameNV.setText(staff.getName());
                    binding.txtGmail.setText(staff.getEmail());
                    binding.txtDt.setText(staff.getRole());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur
            }
        });
    }

    private void getAllOrders()  {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Bills");
        oderList.clear();
//        DatabaseReference orderRef = ordersRef.child(idStaff);


        ordersRef.orderByChild("idStaff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Oder bill = dataSnapshot.getValue(Oder.class);
                            if (bill.getIdStaff().equals(idStaff)) {
                                oderList.add(bill);
                            }
                        }
                        Collections.reverse(oderList);
                        billAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

    }

    private void clickDelete() {
        binding.btnDelete.setOnClickListener(view -> {
            DatabaseReference staffsRef = FirebaseDatabase.getInstance().getReference("users");
             // Thay YOUR_PRODUCT_ID bằng ID của sản phẩm bạn muốn xóa

            staffsRef.child(idStaff).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Xóa thành công
                        Toast.makeText(this, "Xóa Nhân Viên Thành Công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Xóa thất bại
                        Toast.makeText(this, "Xóa Nhân Viên Thất Bại", Toast.LENGTH_SHORT).show();
                    });
        });
    }





}


