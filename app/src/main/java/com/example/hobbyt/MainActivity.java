package com.example.hobbyt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hobbyt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_home){
                setFragment(new HomeFragment());
            }
            if(item.getItemId() == R.id.navigation_recommendations){
                setFragment(new RecordsFragment());
            }
            if(item.getItemId() == R.id.navigation_chat){
                setFragment(new ChatFragment());
            }
            if(item.getItemId() == R.id.navigation_user){
                setFragment(new UserFragment());
            }

            return true;
        });
   }

    private void setFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}