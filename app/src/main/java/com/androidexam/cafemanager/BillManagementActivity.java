package com.androidexam.cafemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidexam.cafemanager.adapter.BillDetailAdapter;
import com.androidexam.cafemanager.databinding.ActivityBillManagementBinding;
import com.androidexam.cafemanager.model.Oder;
import com.androidexam.cafemanager.model.OderDetail;
import com.androidexam.cafemanager.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BillManagementActivity extends AppCompatActivity {

    ActivityBillManagementBinding binding;
    private String idBill;
    private BillDetailAdapter billDetailAdapter;
    private List<OderDetail> oderDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        oderDetailList = new ArrayList<>();
        billDetailAdapter = new BillDetailAdapter(oderDetailList);

        binding.rcvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvOrderDetail.setAdapter(billDetailAdapter);

        idBill = getIntent().getStringExtra("idOrder");
        showBill();
    }

    private void showBill() {
        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("Bills").child(idBill);

        DatabaseReference oderDetailRef = FirebaseDatabase.getInstance().getReference().child("OderDetails").child(idBill);

        oderDetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                oderDetailList.clear();
                for (DataSnapshot oderDetailSnapshot : snapshot.getChildren()) {
                    OderDetail oderDetail = oderDetailSnapshot.getValue(OderDetail.class);
                    oderDetailList.add(oderDetail);
                }
                billDetailAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        billRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Oder oder = snapshot.getValue(Oder.class);

                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                    String totalBill = (numberFormat.format(oder.getTotalBillAmount()) + " đ").replace(',', '.');

                    binding.totalValue.setText(totalBill);
                    binding.tvTimeCreate.setText(oder.getCreateAt());
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(oder.getIdStaff());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                binding.tvStaffCreate.setText(user.getName() + " ( " + user.getUsername() + " )");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}