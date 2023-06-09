package com.androidexam.cafemanager;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class PersonalFragment extends Fragment {


    private EditText Email ,Name,Role,User;
    private DatabaseReference mDatabase;
    private Button btnEdit,btnLogout;
    private ImageView pfImage;


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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        Email = view.findViewById(R.id.pfEmail);
        Name = view.findViewById(R.id.pfName);
        Role = view.findViewById(R.id.pfRole);
        User = view.findViewById(R.id.pfUser);
        pfImage = view.findViewById(R.id.pfimg);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnLogout = view.findViewById(R.id.btnlogout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditProfileActivity.class));
            }
        });

        // Get user information from database using uid
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);

                User.setText(username);
                Email.setText(email);
                Name.setText(name);
                Role.setText(role);


                // Download and display user's profile image
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                storageRef.child("images/" + uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity())
                                .load(uri)
                                .into(pfImage);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }



}