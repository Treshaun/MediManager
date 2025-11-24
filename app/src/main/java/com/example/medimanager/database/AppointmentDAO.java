package com.example.medimanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.medimanager.models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private final DatabaseHelper dbHelper;

    public AppointmentDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Create
    public long insertAppointment(Appointment appointment) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PATIENT_ID, appointment.getPatientId());
        values.put(DatabaseHelper.KEY_APPOINTMENT_DATE, appointment.getAppointmentDate());
        values.put(DatabaseHelper.KEY_APPOINTMENT_TIME, appointment.getAppointmentTime());
        values.put(DatabaseHelper.KEY_REASON, appointment.getReason());
        values.put(DatabaseHelper.KEY_STATUS, appointment.getStatus());
        values.put(DatabaseHelper.KEY_NOTES, appointment.getNotes());

        return database.insert(DatabaseHelper.TABLE_APPOINTMENTS, null, values);
    }

    // Read - Get by ID
    public Appointment getAppointmentById(int id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, p." + DatabaseHelper.KEY_FIRST_NAME + " || ' ' || p." +
                DatabaseHelper.KEY_LAST_NAME + " as patient_name FROM " +
                DatabaseHelper.TABLE_APPOINTMENTS + " a " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PATIENTS + " p ON a." +
                DatabaseHelper.KEY_PATIENT_ID + " = p." + DatabaseHelper.KEY_ID +
                " WHERE a." + DatabaseHelper.KEY_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});

        Appointment appointment = null;
        if (cursor != null && cursor.moveToFirst()) {
            appointment = cursorToAppointment(cursor);
            cursor.close();
        }

        return appointment;
    }

    // Read - Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, p." + DatabaseHelper.KEY_FIRST_NAME + " || ' ' || p." +
                DatabaseHelper.KEY_LAST_NAME + " as patient_name FROM " +
                DatabaseHelper.TABLE_APPOINTMENTS + " a " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PATIENTS + " p ON a." +
                DatabaseHelper.KEY_PATIENT_ID + " = p." + DatabaseHelper.KEY_ID +
                " ORDER BY a." + DatabaseHelper.KEY_APPOINTMENT_DATE + " DESC, a." +
                DatabaseHelper.KEY_APPOINTMENT_TIME + " DESC";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return appointments;
    }

    // Read - Get appointments by patient
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, p." + DatabaseHelper.KEY_FIRST_NAME + " || ' ' || p." +
                DatabaseHelper.KEY_LAST_NAME + " as patient_name FROM " +
                DatabaseHelper.TABLE_APPOINTMENTS + " a " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PATIENTS + " p ON a." +
                DatabaseHelper.KEY_PATIENT_ID + " = p." + DatabaseHelper.KEY_ID +
                " WHERE a." + DatabaseHelper.KEY_PATIENT_ID + " = ?" +
                " ORDER BY a." + DatabaseHelper.KEY_APPOINTMENT_DATE + " DESC";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(patientId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return appointments;
    }

    // Read - Get today's appointments
    public List<Appointment> getTodayAppointments(String today) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, p." + DatabaseHelper.KEY_FIRST_NAME + " || ' ' || p." +
                DatabaseHelper.KEY_LAST_NAME + " as patient_name FROM " +
                DatabaseHelper.TABLE_APPOINTMENTS + " a " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PATIENTS + " p ON a." +
                DatabaseHelper.KEY_PATIENT_ID + " = p." + DatabaseHelper.KEY_ID +
                " WHERE a." + DatabaseHelper.KEY_APPOINTMENT_DATE + " = ?" +
                " ORDER BY a." + DatabaseHelper.KEY_APPOINTMENT_TIME + " ASC";

        Cursor cursor = database.rawQuery(query, new String[]{today});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return appointments;
    }

    // Read - Get appointments by status
    public List<Appointment> getAppointmentsByStatus(String status) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, p." + DatabaseHelper.KEY_FIRST_NAME + " || ' ' || p." +
                DatabaseHelper.KEY_LAST_NAME + " as patient_name FROM " +
                DatabaseHelper.TABLE_APPOINTMENTS + " a " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PATIENTS + " p ON a." +
                DatabaseHelper.KEY_PATIENT_ID + " = p." + DatabaseHelper.KEY_ID +
                " WHERE a." + DatabaseHelper.KEY_STATUS + " = ?" +
                " ORDER BY a." + DatabaseHelper.KEY_APPOINTMENT_DATE + " DESC";

        Cursor cursor = database.rawQuery(query, new String[]{status});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return appointments;
    }

    // Update
    public int updateAppointment(Appointment appointment) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PATIENT_ID, appointment.getPatientId());
        values.put(DatabaseHelper.KEY_APPOINTMENT_DATE, appointment.getAppointmentDate());
        values.put(DatabaseHelper.KEY_APPOINTMENT_TIME, appointment.getAppointmentTime());
        values.put(DatabaseHelper.KEY_REASON, appointment.getReason());
        values.put(DatabaseHelper.KEY_STATUS, appointment.getStatus());
        values.put(DatabaseHelper.KEY_NOTES, appointment.getNotes());

        return database.update(
                DatabaseHelper.TABLE_APPOINTMENTS,
                values,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(appointment.getId())}
        );
    }

    // Update status only
    public int updateAppointmentStatus(int id, String status) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_STATUS, status);

        return database.update(
                DatabaseHelper.TABLE_APPOINTMENTS,
                values,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Delete
    public int deleteAppointment(int id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(
                DatabaseHelper.TABLE_APPOINTMENTS,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Statistics
    public int getTodayAppointmentsCount(String today) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                        " WHERE " + DatabaseHelper.KEY_APPOINTMENT_DATE + " = ?",
                new String[]{today}
        );

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }

    public int getUpcomingAppointmentsCount() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                        " WHERE " + DatabaseHelper.KEY_STATUS + " = 'scheduled' OR " +
                        DatabaseHelper.KEY_STATUS + " = 'in_progress'",
                null
        );

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }

    // Helper method
    private Appointment cursorToAppointment(Cursor cursor) {
        Appointment appointment = new Appointment();

        appointment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)));
        appointment.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PATIENT_ID)));
        appointment.setAppointmentDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_APPOINTMENT_DATE)));
        appointment.setAppointmentTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_APPOINTMENT_TIME)));
        appointment.setReason(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REASON)));
        appointment.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_STATUS)));
        appointment.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_NOTES)));
        appointment.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CREATED_AT)));

        // Get patient name from JOIN
        int nameIndex = cursor.getColumnIndex("patient_name");
        if (nameIndex != -1) {
            appointment.setPatientName(cursor.getString(nameIndex));
        }

        return appointment;
    }
}
