package com.androidexam.cafemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText Email ,Name,Role,User,Password, ConfirmPassword;
    private DatabaseReference mDatabase;
    Button btnSave;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Email = findViewById(R.id.editEmail);
        Name = findViewById(R.id.editName);
        Role = findViewById(R.id.editRole);
        User = findViewById(R.id.editUsername);
        Password = findViewById(R.id.editPassword);
        ConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSave = findViewById(R.id.btnSave);

        // Lấy uid của người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        // Truy xuất dữ liệu người dùng từ Firebase Realtime Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy các thông tin người dùng từ snapshot và đặt giá trị vào các thành phần UI tương ứng
                String username = snapshot.child("username").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);

                User.setText(username);
                Email.setText(email);
                Name.setText(name);
                Role.setText(role);
                Password.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Lấy giá trị mới từ các trường thông tin người dùng trên giao diện
                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String role = Role.getText().toString().trim();
                String username = User.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String confirmpassword = ConfirmPassword.getText().toString().trim();

                // Truy xuất dữ liệu người dùng từ Firebase Realtime Database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Lấy các thông tin người dùng từ snapshot
                        String usernameOld = snapshot.child("username").getValue(String.class);
                        String emailOld = snapshot.child("email").getValue(String.class);
                        String nameOld = snapshot.child("name").getValue(String.class);
                        String roleOld = snapshot.child("role").getValue(String.class);
                        String passwordOld = snapshot.child("password").getValue(String.class);

                        // Kiểm tra xem các giá trị mới có khác với các giá trị ban đầu không
                        if (!username.equals(usernameOld)) {
                            reference.child("username").setValue(username);
                        }
                        if (!email.equals(emailOld)) {
                            reference.child("email").setValue(email);
                        }
                        if (!name.equals(nameOld)) {
                            reference.child("name").setValue(name);
                        }
                        if (!role.equals(roleOld)) {
                            reference.child("role").setValue(role);
                        }
                        if (!confirmpassword.equals("")) {
                                if(!confirmpassword.equals(passwordOld)){
                                    reference.child("password").setValue(confirmpassword);
                                    // Hiển thị thông báo "Đã lưu"
                                    Toast.makeText(EditProfileActivity.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(EditProfileActivity.this, "Mật khẩu mới khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
                                }

                        }else{
                            Toast.makeText(EditProfileActivity.this, "Mật khẩu mới không được để trống", Toast.LENGTH_SHORT).show();Toast.makeText(EditProfileActivity.this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();

                        }



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu cần
                    }
                });

            }
        });
    }



}
