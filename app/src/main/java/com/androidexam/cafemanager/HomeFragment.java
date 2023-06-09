package com.androidexam.cafemanager;

import static android.content.Context.MODE_PRIVATE;
import static com.androidexam.cafemanager.ProductFragment.ROLE_ADMIN;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidexam.cafemanager.adapter.BillAdapter;
import com.androidexam.cafemanager.databinding.FragmentHomeBinding;
import com.androidexam.cafemanager.model.Oder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private List<Oder> oderList;
    private BillAdapter billAdapter;
    private String role;
    private String uid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.rcvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        oderList = new ArrayList<>();
        billAdapter = new BillAdapter(oderList);
        binding.rcvOrders.setAdapter(billAdapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER", MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        uid = sharedPreferences.getString("uid", "");
        setDateTv();
        try {
            getAllOrders();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.dateStart.setOnClickListener(v -> showDatePickerStart(v));
        binding.dateEnd.setOnClickListener(v -> showDatePickerEnd(v));

        return view;
    }

    private void setDateTv() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.dateEnd.setText(day + "/" + (month + 1) + "/" + year);
        binding.dateStart.setText(1 + "/" + (month + 1) + "/" + year);
    }

    private void getAllOrders() throws ParseException {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Bills");
        oderList.clear();

        String startDate = binding.dateStart.getText().toString() + " 00:00:00";
        String endDate = binding.dateEnd.getText().toString() + " 23:59:59";

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Oder bill = dataSnapshot.getValue(Oder.class);
                    String billDateCreate = bill.getCreateAt();

                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    try {
                        Date billCreate = format.parse(billDateCreate);
                        Date dateStart = format.parse(startDate);
                        Date dateEnd = format.parse(endDate);

                        if (billCreate.after(dateStart) && billCreate.before(dateEnd)) {
                            if (role.equals(ROLE_ADMIN))
                                oderList.add(bill);
                            else if (bill.getIdStaff().equals(uid))
                                oderList.add(bill);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
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

    public void showDatePickerStart(View v) {
        String dateString = binding.dateStart.getText().toString(); // Replace with your date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.dateStart.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                try {
                    getAllOrders();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    public void showDatePickerEnd(View v) {
        String dateString = binding.dateEnd.getText().toString(); // Replace with your date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    binding.dateEnd.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    try {
                        getAllOrders();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, year, month, day);

            datePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}