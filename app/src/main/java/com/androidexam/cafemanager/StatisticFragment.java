package com.androidexam.cafemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidexam.cafemanager.adapter.StatisticAdapter;
import com.androidexam.cafemanager.databinding.FragmentStatisticsBinding;
import com.androidexam.cafemanager.model.Oder;
import com.androidexam.cafemanager.model.OderDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class StatisticFragment extends Fragment {

    private Spinner monthSpinner;
    private DatabaseReference billsRef;
    private List<Oder> oderList;
    private StatisticAdapter statisticAdapter;

    private List<OderDetail> oderDetailList = new ArrayList<>(); // Thay thế ArrayList bằng danh sách chi tiết đơn hàng của bạn

    private List<OderDetail> listsp = new ArrayList<>();
// Khởi tạo Adapter và thiết lập danh sách chi tiết đơn hàng

    public static final ArrayList<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    ));
    private FragmentStatisticsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        billsRef = FirebaseDatabase.getInstance().getReference().child("Bills");

        monthSpinner = binding.btnFilter;
        setDataSpinner();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvList.setLayoutManager(layoutManager);
        statisticAdapter = new StatisticAdapter(listsp);
        binding.rcvList.setAdapter(statisticAdapter);


        return binding.getRoot();
    }

    private void setDataSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, CATEGORY_LIST);
        monthSpinner.setAdapter(adapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = (String) parent.getItemAtPosition(position);
                long totalRevenue = calculateTotalRevenue(selectedMonth);
                binding.txtmonth.setText(selectedMonth);
                TextView revenueTextView = binding.txtrevenue;
                revenueTextView.setText(String.valueOf(totalRevenue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý khi không có tháng nào được chọn
            }
        });
    }

    private long calculateTotalRevenue(String selectedMonth) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Bills");
        AtomicLong totalRevenue = new AtomicLong(0); // Sử dụng AtomicInteger để lưu trữ giá trị totalRevenue
        oderList = new ArrayList<>();
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Oder bill = dataSnapshot.getValue(Oder.class);
                    String billDateCreate = bill.getCreateAt();
                    try {
                        Date billDate = format.parse(billDateCreate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(billDate);
                        int billMonth = calendar.get(Calendar.MONTH) + 1;
                        int selectedMonthValue = Integer.parseInt(selectedMonth);

                        if (billMonth == selectedMonthValue) {
                            totalRevenue.addAndGet(bill.getTotalBillAmount()); // Sử dụng addAndGet() của AtomicInteger để tăng giá trị totalRevenue
                            oderList.add(bill);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                binding.txtrevenue.setText(String.valueOf(totalRevenue.get())); // Sử dụng get() của AtomicInteger để lấy giá trị totalRevenue

                DatabaseReference oderDetailRef = FirebaseDatabase.getInstance().getReference().child("OderDetails");
                oderDetailRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        oderDetailList.clear();
                        for (DataSnapshot oderDetailSnapshot : snapshot.getChildren()) {
                            String idOder = oderDetailSnapshot.getKey();
                            boolean containsOderDetail = oderList.stream()
                                    .anyMatch(oder -> oder.getId().equals(idOder));

                            if (containsOderDetail) {
                                for (DataSnapshot oderDetailSnapshotChild : oderDetailSnapshot.getChildren()) {
                                    OderDetail oderDetail = oderDetailSnapshotChild.getValue(OderDetail.class);
                                    oderDetailList.add(oderDetail);
                                }
                            }
                        }
                        for (OderDetail item : oderDetailList) {
                                boolean found = false;
                                if(listsp.isEmpty()){
                                    listsp.add(item);
                                }
                                else{

                                    for (OderDetail existingItem : listsp) {
                                        if (item.getIdProduct().equals(existingItem.getIdProduct())) {
                                       //  Nếu item đã tồn tại trong listsp, cộng thêm số lượng
                                            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                    // Nếu item chưa tồn tại trong listsp, thêm nó vào
                                        listsp.add(item);
                                    }
                                }


                        }
                        Collections.sort(listsp, new Comparator<OderDetail>() {
                            @Override
                            public int compare(OderDetail item1, OderDetail item2) {
                                // So sánh thuộc tính quality của hai item
                                // Sắp xếp giảm dần: item2.getQuality() - item1.getQuality()
                                // Sắp xếp tăng dần: item1.getQuality() - item2.getQuality()
                                return Integer.compare(item2.getQuantity(), item1.getQuantity());
                            }
                        });


                        statisticAdapter.notifyDataSetChanged();

                      }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
            }
        });

        return totalRevenue.get(); // Sử dụng get() của AtomicInteger để lấy giá trị totalRevenue
    }


}

