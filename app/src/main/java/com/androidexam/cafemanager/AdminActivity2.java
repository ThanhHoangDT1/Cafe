package com.androidexam.cafemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.androidexam.cafemanager.databinding.ActivityAdmin2Binding;

public class AdminActivity2 extends AppCompatActivity {
    ActivityAdmin2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdmin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new staffFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.staff:
                    replaceFragment(new staffFragment());
                    break;
                case R.id.bill:
                    replaceFragment(new managerbillFragment());
                    break;
                case R.id.statistics:
                    replaceFragment(new statisticsFragment());
                    break;
                case R.id.personal:
                    replaceFragment(new PersonalFragment());

                    break;
            }


            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_admin,fragment);
        fragmentTransaction.commit();

    }
}