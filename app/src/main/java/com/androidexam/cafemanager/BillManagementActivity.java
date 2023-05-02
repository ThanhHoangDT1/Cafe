package com.androidexam.cafemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.androidexam.cafemanager.databinding.ActivityAddProductBinding;
import com.androidexam.cafemanager.databinding.ActivityBillManagementBinding;
import com.androidexam.cafemanager.model.Oder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BillManagementActivity extends AppCompatActivity {

    private ActivityBillManagementBinding binding;
    private String idBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBillManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        idBill = intent.getStringExtra("idOrder");

        getData();
    }

    private void getData() {
        DatabaseReference billRef= FirebaseDatabase.getInstance().getReference("Bills").child(idBill);
        billRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Oder oder=snapshot.getValue(Oder.class);
                    binding.totalValue.setText(String.valueOf(oder.getTotalBillAmount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}