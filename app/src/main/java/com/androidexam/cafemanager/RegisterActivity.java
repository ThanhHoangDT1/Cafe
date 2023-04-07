package com.androidexam.cafemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.cafemanager.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword , signupRole;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_Email);
        signupRole = findViewById(R.id.signup_role);
        signupUsername = findViewById(R.id.signup_user);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.btnSignup);
        loginRedirectText = findViewById(R.id.btnSignin);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String role = signupRole.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                User user = new User(name, email,role, username, password);
                reference.child(username).setValue(user);

                Toast.makeText(RegisterActivity.this, "Bạn đã đăng kí thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}