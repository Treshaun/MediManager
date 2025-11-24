package com.example.medimanager.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medimanager.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        setupToolbar();
        loadUserData();
        setupListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("name", "Dr. Ben Amor");
        String email = sharedPreferences.getString("email", "dr.benamor@medimanager.tn");

        binding.etName.setText(name);
        binding.etEmail.setText(email);
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.apply();

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
