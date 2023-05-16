package com.androidexam.cafemanager;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidexam.cafemanager.adapter.staffAdapter;
import com.androidexam.cafemanager.databinding.FragmentStaffBinding;
import com.androidexam.cafemanager.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class staffFragment extends Fragment {

    private FragmentStaffBinding binding;

    private staffAdapter staffAdapter;

    private List<User> staffList;

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

        binding = FragmentStaffBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.rvStaff.setLayoutManager(new LinearLayoutManager(getActivity()));

        staffList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        staffAdapter = new staffAdapter(staffList, uid);
        binding.rvStaff.setAdapter(staffAdapter);

        viewListStaff();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void viewListStaff() {

        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staffList.clear();
                for (DataSnapshot staffSnapshot : dataSnapshot.getChildren()) {
                    User staff = staffSnapshot.getValue(User.class);
                    staffList.add(staff);
                }
                staffAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}