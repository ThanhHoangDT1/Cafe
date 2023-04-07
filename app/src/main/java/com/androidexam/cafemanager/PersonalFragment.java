package com.androidexam.cafemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {
    private SharedPreferences sharedPreferences;

    EditText Email ,Name;
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_personal, container, false);

        
        // Lấy ID của người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userId = sharedPreferences.getString("userId", "");

        // Tham chiếu đến nút chứa thông tin của người dùng
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Lắng nghe sự kiện khi dữ liệu từ cơ sở dữ liệu được trả về
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy thông tin người dùng từ snapshot
                String userName = dataSnapshot.child("name").getValue(String.class);
                String userEmail = dataSnapshot.child("email").getValue(String.class);

                // Hiển thị thông tin người dùng lên giao diện
                Name = view.findViewById(R.id.PersonName);
                Email = view.findViewById(R.id.PersonEmail);
                Name.setText(userName);
                Email.setText(userEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
        return view;

//        inflater.inflate(R.layout.fragment_personal, container, false);

//        // Lấy SharedPreferences và các giá trị của nó
//        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//
//        String name = sharedPreferences.getString("name", "");
//        String username = sharedPreferences.getString("username", "");
//        String role = sharedPreferences.getString("role", "");
//        String email = sharedPreferences.getString("email", "");
//        String password = sharedPreferences.getString("password", "");
//
//        // Đặt các giá trị vào các EditText trong fragment_personal.xml
//        EditText profileName = view.findViewById(R.id.profileName);
//        profileName.setText(name);
//
//        EditText profileUser = view.findViewById(R.id.profileUser);
//        profileUser.setText(username);
//
//        EditText profileRole = view.findViewById(R.id.profileRole);
//        profileRole.setText(role);
//
//        EditText profileEmail = view.findViewById(R.id.profileEmail);
//        profileEmail.setText(email);
//
//        EditText profilePassword = view.findViewById(R.id.profilePassword);
//        profileEmail.setText(password);
//
//        return view;
    }




}