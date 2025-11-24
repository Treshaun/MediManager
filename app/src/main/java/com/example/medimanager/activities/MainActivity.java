package com.example.medimanager.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.medimanager.R;
import com.example.medimanager.databinding.ActivityMainBinding;
import com.example.medimanager.fragments.AppointmentsFragment;
import com.example.medimanager.fragments.HomeFragment;
import com.example.medimanager.fragments.PatientsFragment;
import com.example.medimanager.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Set the initial fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.fab.setVisibility(View.GONE);
        }

        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                binding.fab.setVisibility(View.GONE);
            } else if (itemId == R.id.nav_appointments) {
                selectedFragment = new AppointmentsFragment();
                binding.fab.setVisibility(View.VISIBLE);
                binding.fab.setOnClickListener(v -> {
                    startActivity(new Intent(this, AddAppointmentActivity.class));
                });
            } else if (itemId == R.id.nav_patients) {
                selectedFragment = new PatientsFragment();
                binding.fab.setVisibility(View.VISIBLE);
                binding.fab.setOnClickListener(v -> {
                    startActivity(new Intent(this, AddPatientActivity.class));
                });
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                binding.fab.setVisibility(View.GONE);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    public void navigateToAppointments() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_appointments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        MenuItem themeIcon = menu.findItem(R.id.action_theme);
        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeIcon.setIcon(R.drawable.ic_light_mode);
        } else {
            themeIcon.setIcon(R.drawable.ic_dark_mode);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
