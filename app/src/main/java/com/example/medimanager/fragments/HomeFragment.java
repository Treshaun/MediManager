package com.example.medimanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medimanager.R;
import com.example.medimanager.activities.AddAppointmentActivity;
import com.example.medimanager.activities.AddPatientActivity;
import com.example.medimanager.activities.MainActivity;
import com.example.medimanager.activities.PatientDetailsActivity;
import com.example.medimanager.adapters.AppointmentAdapter;
import com.example.medimanager.database.AppointmentDAO;
import com.example.medimanager.database.PatientDAO;
import com.example.medimanager.databinding.FragmentHomeBinding;
import com.example.medimanager.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    // Database
    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;

    // Adapters
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> todayAppointments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize DAOs
        patientDAO = new PatientDAO(requireContext());
        appointmentDAO = new AppointmentDAO(requireContext());

        // Initialize UI
        setupRecyclerView();
        setupClickListeners();

        // Load data
        loadStatistics();
        loadTodayAppointments();
        updateDate();
    }

    private void setupRecyclerView() {
        todayAppointments = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(requireContext(), todayAppointments);

        binding.rvTodayAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTodayAppointments.setAdapter(appointmentAdapter);
        binding.rvTodayAppointments.setNestedScrollingEnabled(false);

        // Set item click listener
        appointmentAdapter.setOnItemClickListener(new AppointmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Appointment appointment) {
                // Navigate to appointment details or patient details
                Intent intent = new Intent(requireContext(), PatientDetailsActivity.class);
                intent.putExtra("PATIENT_ID", appointment.getPatientId());
                startActivity(intent);
            }

            @Override
            public void onStatusClick(Appointment appointment) {
                // Update appointment status
                updateAppointmentStatus(appointment);
            }
        });
    }

    private void setupClickListeners() {
        // View All button
        binding.tvViewAll.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToAppointments();
            }
        });

        // New Appointment button
        binding.btnNewAppointment.setOnClickListener(v -> navigateToAddAppointment());

        // Add Patient button
        binding.btnAddPatient.setOnClickListener(v -> navigateToAddPatient());
    }

    private void loadStatistics() {
        // Get today's date
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Load statistics
        int totalPatients = patientDAO.getTotalPatientsCount();
        int todayAppointmentsCount = appointmentDAO.getTodayAppointmentsCount(today);
        int upcomingAppointments = appointmentDAO.getUpcomingAppointmentsCount();

        // Update UI
        binding.tvTotalPatients.setText(String.valueOf(totalPatients));
        binding.tvTodayAppointments.setText(String.valueOf(todayAppointmentsCount));
        binding.tvPendingTasks.setText(String.valueOf(upcomingAppointments));
        binding.tvWeeklyVisits.setText("42"); // Mock data for now
    }

    private void loadTodayAppointments() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        List<Appointment> appointments = appointmentDAO.getTodayAppointments(today);

        todayAppointments.clear();
        todayAppointments.addAll(appointments);
        appointmentAdapter.notifyDataSetChanged();
    }

    private void updateAppointmentStatus(Appointment appointment) {
        String newStatus;
        if (appointment.isScheduled()) {
            newStatus = "in_progress";
        } else if (appointment.isInProgress()) {
            newStatus = "completed";
        } else {
            newStatus = "scheduled";
        }

        int result = appointmentDAO.updateAppointmentStatus(appointment.getId(), newStatus);

        if (result > 0) {
            appointment.setStatus(newStatus);
            appointmentAdapter.notifyDataSetChanged();
            Toast.makeText(requireContext(), "Status updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        binding.tvDate.setText(currentDate);
    }

    private void navigateToAddPatient() {
        Intent intent = new Intent(requireContext(), AddPatientActivity.class);
        startActivity(intent);
    }

    private void navigateToAddAppointment() {
        Intent intent = new Intent(requireContext(), AddAppointmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this fragment
        loadStatistics();
        loadTodayAppointments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
