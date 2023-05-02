package com.androidexam.cafemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    private EditText Email ,Name,Role,User;
    private ImageView img;
    private TextView btnChangePwd;
    Button btnSave;
    private String uid;


    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Email = findViewById(R.id.editEmail);
        Name = findViewById(R.id.editName);
        Role = findViewById(R.id.editRole);
        User = findViewById(R.id.editUsername);
        img = findViewById(R.id.editimg);
        btnChangePwd = findViewById(R.id.btnChangePassword);
        btnSave = findViewById(R.id.btnSave);


        // Lấy uid của người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
         uid = sharedPreferences.getString("uid", "");

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo intent để chọn ảnh từ bộ nhớ
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });

        // Hiển thị ảnh hiện tại của người dùng
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EditProfileActivity.this)
                        .load(uri)
                        .into(img);
            }
        });

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ChangePassWordActivity.class));

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

                Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                Matcher matcher = pattern.matcher(email);
                boolean isEmailValid = matcher.matches();

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

                        // Kiểm tra xem các giá trị mới có khác với các giá trị ban đầu không
                        if (!username.equals(usernameOld) || !name.equals(nameOld) || !role.equals(roleOld) && !email.equals(emailOld)) {
                            if(isEmailValid){
                                reference.child("username").setValue(username);
                                reference.child("name").setValue(name);
                                reference.child("role").setValue(role);
                                reference.child("email").setValue(email);
                                Toast.makeText(EditProfileActivity.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đúng định dạng email!", Toast.LENGTH_SHORT).show();
                            }
                        }else if(!username.equals(usernameOld) || !name.equals(nameOld) || !role.equals(roleOld) && email.equals(emailOld)){
                            reference.child("username").setValue(username);
                            reference.child("name").setValue(name);
                            reference.child("role").setValue(role);
                            reference.child("email").setValue(email);
                            Toast.makeText(EditProfileActivity.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                            builder.setMessage("Bạn chưa thay đổi thông tin cá nhân!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Đóng hộp thoại
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            // Lấy Uri của ảnh được chọn
            Uri uri = data.getData();

            // Hiển thị ảnh được chọn bằng Glide
            Glide.with(EditProfileActivity.this)
                    .load(uri)
                    .into(img);

            // Upload ảnh lên Firebase Storage và lưu đường dẫn vào Realtime Database
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("images/" + uid);
            imagesRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Lấy đường dẫn URL của ảnh đã upload
                    imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Lưu đường dẫn URL của ảnh vào Realtime Database
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
                            reference.child("image_url").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }

}
