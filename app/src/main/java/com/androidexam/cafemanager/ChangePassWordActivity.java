package com.androidexam.cafemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.ValueEventListener;

public class ChangePassWordActivity extends AppCompatActivity {

    private EditText passOld,passNew,passNewConfirm;
    private Button btnSave;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);

        passOld = findViewById(R.id.cpPassOld);
        passNew = findViewById(R.id.cpPassNew);
        passNewConfirm = findViewById(R.id.cpPassNewConfirm);
        btnSave = findViewById(R.id.btnSavePass);

        // Lấy uid của người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PassOld = passOld.getText().toString().trim();
                String PassNew = passNew.getText().toString().trim();
                String PassNewConfirm = passNewConfirm.getText().toString().trim();

                // Kiểm tra ô có trống hay không
                if(PassOld.isEmpty() || PassNew.isEmpty() || PassNewConfirm.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra ô PassNew có ít nhất 6 kí tự hay không
                if(PassNew.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String passwordOld = snapshot.child("password").getValue(String.class);


                        // Kiểm tra ô PassOld có khớp với mật khẩu cũ hay không
                        if(!passwordOld.equals(PassOld)) {
                            Toast.makeText(getApplicationContext(), "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Kiểm tra ô PassNew và ô PassNewConfirm có giống nhau hay không
                        if(!PassNew.equals(PassNewConfirm)) {
                            Toast.makeText(getApplicationContext(), "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Cập nhật mật khẩu mới vào database
                        reference.child("password").setValue(PassNew);
                        Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();

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