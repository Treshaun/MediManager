package com.example.medimanager.utils;

import android.util.Patterns;

import com.example.medimanager.models.Patient;

import java.util.regex.Pattern;

public class ValidationUtils {

    /**
     * Check if string is empty or null
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate phone number (Tunisian format)
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }

        // Remove spaces, dashes, and plus sign
        String cleanPhone = phone.replaceAll("[\\s\\-\\+]", "");

        // Check for Tunisian format:
        // 1. Starts with 216 (country code) and has 11 digits total (216 + 8 digits)
        // 2. Or just 8 digits (local format)
        if (cleanPhone.startsWith("216")) {
            return cleanPhone.length() == 11;
        } else {
            return cleanPhone.length() == 8;
        }
    }

    /**
     * Validate date format (yyyy-MM-dd)
     */
    public static boolean isValidDate(String date) {
        if (isEmpty(date)) {
            return false;
        }
        return DateUtils.isValidDate(date);
    }

    /**
     * Validate patient data before saving
     */
    public static ValidationResult validatePatientData(Patient patient) {
        // Validate first name
        if (isEmpty(patient.getFirstName())) {
            return new ValidationResult(false, "First name is required");
        }

        // Validate last name
        if (isEmpty(patient.getLastName())) {
            return new ValidationResult(false, "Last name is required");
        }

        // Validate phone (optional but check format if provided)
        if (!isEmpty(patient.getPhone()) && !isValidPhone(patient.getPhone())) {
            return new ValidationResult(false, "Invalid phone number format");
        }

        // Validate email (optional but check format if provided)
        if (!isEmpty(patient.getEmail()) && !isValidEmail(patient.getEmail())) {
            return new ValidationResult(false, "Invalid email address");
        }

        // Validate date of birth (optional but check format if provided)
        if (!isEmpty(patient.getDateOfBirth()) && !isValidDate(patient.getDateOfBirth())) {
            return new ValidationResult(false, "Invalid date format");
        }

        return new ValidationResult(true, "Validation successful");
    }

    /**
     * Validate name (letters, spaces, hyphens only)
     */
    public static boolean isValidName(String name) {
        if (isEmpty(name)) {
            return false;
        }

        // Allow letters, spaces, hyphens, and apostrophes
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s'-]+$");
        return pattern.matcher(name).matches();
    }

    /**
     * Validate blood group
     */
    public static boolean isValidBloodGroup(String bloodGroup) {
        if (isEmpty(bloodGroup)) {
            return true; // Optional field
        }

        String[] validGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String group : validGroups) {
            if (group.equals(bloodGroup)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate age (must be positive)
     */
    public static boolean isValidAge(int age) {
        return age > 0 && age < 150;
    }

    /**
     * Clean phone number (remove formatting)
     */
    public static String cleanPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return "";
        }
        return phone.replaceAll("[^0-9+]", "");
    }

    /**
     * Format phone number for display
     */
    public static String formatPhoneNumber(String phone) {
        String clean = cleanPhoneNumber(phone);
        if (clean.length() == 10) {
            return String.format("(%s) %s-%s",
                    clean.substring(0, 3),
                    clean.substring(3, 6),
                    clean.substring(6));
        }
        return phone;
    }

    /**
     * Validation Result class
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}