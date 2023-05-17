package com.androidexam.cafemanager;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidexam.cafemanager.databinding.FragmentStatisticsBinding;
import com.androidexam.cafemanager.model.Oder;
import com.androidexam.cafemanager.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticFragment extends Fragment {

    private Spinner monthSpinner;
    private DatabaseReference databaseRef;
    public static final ArrayList<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList(
            "1","2","3","4","5","6","7","8","9","10","11","12"
    ));
    private FragmentStatisticsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(getLayoutInflater());
        monthSpinner = binding.btnFilter;

        return binding.getRoot();
    }
    private void setDataSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, CATEGORY_LIST);
        binding.btnFilter.setAdapter(adapter);

        // Lắng nghe sự kiện khi người dùng chọn một phần tử trên Spinner
        binding.btnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tháng được chọn
                String selectedMonth = (String) parent.getItemAtPosition(position);

                // Tính tổng doanh thu cho tháng được chọn
                int totalRevenue = calculateTotalRevenue(selectedMonth);

                // Hiển thị tổng doanh thu trong TextView
                TextView revenueTextView = view.findViewById(R.id.txtrevenue);
                revenueTextView.setText(String.valueOf(totalRevenue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý khi không có tháng nào được chọn
            }
        });

    }
    private int calculateTotalRevenue(String selectedMonth) {
        int totalRevenue = 0;

        // Duyệt qua danh sách dữ liệu thống kê
//        for (Oder item : statisticsList) {
//            if (item.getCreateAt().equals(selectedMonth)) {
//                // Nếu tháng trong dữ liệu thống kê trùng với tháng được chọn, thì cộng vào tổng doanh thu
//                totalRevenue += item.getTotalBillAmount();
//            }
//        }

        return totalRevenue;
    }


}