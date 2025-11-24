package com.example.medimanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medimanager.activities.EditProfileActivity;
import com.example.medimanager.activities.NotificationSettingsActivity;
import com.example.medimanager.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        loadUserData();

        binding.editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), EditProfileActivity.class));
        });

        binding.notificationsButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), NotificationSettingsActivity.class));
        });
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("name", "Dr. Ben Amor");
        String email = sharedPreferences.getString("email", "dr.benamor@medimanager.tn");

        binding.doctorName.setText(name);
        binding.doctorEmail.setText(email);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
