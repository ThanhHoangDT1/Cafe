package com.androidexam.cafemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    EditText editName, editEmail, editUsername, editPassword,editRole;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser,roleUser;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editRole = findViewById(R.id.editRole);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.btnSave);


        showData();



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged() || isRoleChanged()) {
                    Toast.makeText(EditProfileActivity.this, "Đã lưu", Toast.LENGTH_SHORT).show();

                    passDataProfile();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Không tìm thấy thay đổi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isNameChanged(){
        if (!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isEmailChanged(){
        if (!emailUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else{
            return false;
        }
    }
    public boolean isRoleChanged(){
        if (!roleUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("role").setValue(editRole.getText().toString());
            roleUser = editRole.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isPasswordChanged(){
        if (!passwordUser.equals(editPassword.getText().toString())){
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public void showData(){
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        roleUser = intent.getStringExtra("role");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editRole.setText(roleUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }
    private void passDataProfile()
    {
        String userUsername = editUsername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    editUsername.setError(null);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String roleFromDB = snapshot.child(userUsername).child("role").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    //Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);

//                    intent.putExtra("name", nameFromDB);
//                    intent.putExtra("email", emailFromDB);
//                    intent.putExtra("role", roleFromDB);
//                    intent.putExtra("username", usernameFromDB);
//                    intent.putExtra("password", passwordFromDB);
//
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}