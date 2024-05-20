package com.example.hobbyt;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hobbyt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onStart() {
        super.onStart();

        //GoToAuthorization();
    }

    private void GoToAuthorization() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }

    private void GoToEditor() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationViewListener();
        binding.addPostButton.setOnClickListener(v -> {
            GoToEditor();
        });
    }

    private void NavigationViewListener() {
        setFragment(new RecordsFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_recommendations){
                setFragment(new RecordsFragment());
            }
            if(item.getItemId() == R.id.navigation_user){
                setFragment(new UserFragment());
            }

            return true;
        });
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}