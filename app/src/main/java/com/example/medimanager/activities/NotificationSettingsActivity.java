package com.example.medimanager.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medimanager.databinding.ActivityNotificationSettingsBinding;

public class NotificationSettingsActivity extends AppCompatActivity {

    private ActivityNotificationSettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        setupToolbar();
        loadSettings();
        setupListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadSettings() {
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        binding.switchNotifications.setChecked(notificationsEnabled);
    }

    private void setupListeners() {
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
        });
    }
}
