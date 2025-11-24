package com.example.medimanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.medimanager.models.Consultation;

import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    private final DatabaseHelper dbHelper;

    public ConsultationDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Create - Insert new consultation
    public long insertConsultation(Consultation consultation) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PATIENT_ID, consultation.getPatientId());
        values.put(DatabaseHelper.KEY_CONSULTATION_DATE, consultation.getConsultationDate());
        values.put(DatabaseHelper.KEY_DIAGNOSIS, consultation.getDiagnosis());
        values.put(DatabaseHelper.KEY_TREATMENT, consultation.getTreatment());
        values.put(DatabaseHelper.KEY_PRESCRIPTION, consultation.getPrescription());
        values.put(DatabaseHelper.KEY_NOTES, consultation.getNotes());

        return database.insert(DatabaseHelper.TABLE_CONSULTATIONS, null, values);
    }

    // Read - Get consultation by ID
    public Consultation getConsultationById(int id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONSULTATIONS,
                null,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Consultation consultation = null;
        if (cursor != null && cursor.moveToFirst()) {
            consultation = cursorToConsultation(cursor);
            cursor.close();
        }

        return consultation;
    }

    // Read - Get all consultations by patient ID
    public List<Consultation> getConsultationsByPatient(int patientId) {
        List<Consultation> consultations = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONSULTATIONS,
                null,
                DatabaseHelper.KEY_PATIENT_ID + " = ?",
                new String[]{String.valueOf(patientId)},
                null, null,
                DatabaseHelper.KEY_CONSULTATION_DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                consultations.add(cursorToConsultation(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return consultations;
    }

    // Read - Get all consultations
    public List<Consultation> getAllConsultations() {
        List<Consultation> consultations = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONSULTATIONS,
                null, null, null, null, null,
                DatabaseHelper.KEY_CONSULTATION_DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                consultations.add(cursorToConsultation(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return consultations;
    }

    // Read - Get consultations by date
    public List<Consultation> getConsultationsByDate(String date) {
        List<Consultation> consultations = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONSULTATIONS,
                null,
                DatabaseHelper.KEY_CONSULTATION_DATE + " = ?",
                new String[]{date},
                null, null,
                DatabaseHelper.KEY_CREATED_AT + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                consultations.add(cursorToConsultation(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return consultations;
    }

    // Update - Update existing consultation
    public int updateConsultation(Consultation consultation) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PATIENT_ID, consultation.getPatientId());
        values.put(DatabaseHelper.KEY_CONSULTATION_DATE, consultation.getConsultationDate());
        values.put(DatabaseHelper.KEY_DIAGNOSIS, consultation.getDiagnosis());
        values.put(DatabaseHelper.KEY_TREATMENT, consultation.getTreatment());
        values.put(DatabaseHelper.KEY_PRESCRIPTION, consultation.getPrescription());
        values.put(DatabaseHelper.KEY_NOTES, consultation.getNotes());

        return database.update(
                DatabaseHelper.TABLE_CONSULTATIONS,
                values,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(consultation.getId())}
        );
    }

    // Delete - Delete consultation by ID
    public int deleteConsultation(int id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(
                DatabaseHelper.TABLE_CONSULTATIONS,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Statistics - Get monthly consultations count
    public int getMonthlyConsultationsCount() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_CONSULTATIONS +
                " WHERE strftime('%Y-%m', " + DatabaseHelper.KEY_CONSULTATION_DATE +
                ") = strftime('%Y-%m', 'now')";

        Cursor cursor = database.rawQuery(query, null);

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }

    // Statistics - Get total consultations count
    public int getTotalConsultationsCount() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_CONSULTATIONS,
                null
        );

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }

    // Get recent consultations (limit)
    public List<Consultation> getRecentConsultations(int limit) {
        List<Consultation> consultations = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONSULTATIONS,
                null, null, null, null, null,
                DatabaseHelper.KEY_CONSULTATION_DATE + " DESC",
                String.valueOf(limit)
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                consultations.add(cursorToConsultation(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return consultations;
    }

    // Search consultations by diagnosis
    public List<Consultation> searchByDiagnosis(String query) {
        List<Consultation> consultations = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.KEY_DIAGNOSIS + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONSULTATIONS,
                null, selection, selectionArgs, null, null,
                DatabaseHelper.KEY_CONSULTATION_DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                consultations.add(cursorToConsultation(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return consultations;
    }

    // Helper method - Convert cursor to Consultation object
    private Consultation cursorToConsultation(Cursor cursor) {
        Consultation consultation = new Consultation();

        int idIndex = cursor.getColumnIndex(DatabaseHelper.KEY_ID);
        int patientIdIndex = cursor.getColumnIndex(DatabaseHelper.KEY_PATIENT_ID);
        int dateIndex = cursor.getColumnIndex(DatabaseHelper.KEY_CONSULTATION_DATE);
        int diagnosisIndex = cursor.getColumnIndex(DatabaseHelper.KEY_DIAGNOSIS);
        int treatmentIndex = cursor.getColumnIndex(DatabaseHelper.KEY_TREATMENT);
        int prescriptionIndex = cursor.getColumnIndex(DatabaseHelper.KEY_PRESCRIPTION);
        int notesIndex = cursor.getColumnIndex(DatabaseHelper.KEY_NOTES);
        int createdAtIndex = cursor.getColumnIndex(DatabaseHelper.KEY_CREATED_AT);

        if (idIndex != -1) {
            consultation.setId(cursor.getInt(idIndex));
        }
        if (patientIdIndex != -1) {
            consultation.setPatientId(cursor.getInt(patientIdIndex));
        }
        if (dateIndex != -1) {
            consultation.setConsultationDate(cursor.getString(dateIndex));
        }
        if (diagnosisIndex != -1) {
            consultation.setDiagnosis(cursor.getString(diagnosisIndex));
        }
        if (treatmentIndex != -1) {
            consultation.setTreatment(cursor.getString(treatmentIndex));
        }
        if (prescriptionIndex != -1) {
            consultation.setPrescription(cursor.getString(prescriptionIndex));
        }
        if (notesIndex != -1) {
            consultation.setNotes(cursor.getString(notesIndex));
        }
        if (createdAtIndex != -1) {
            consultation.setCreatedAt(cursor.getString(createdAtIndex));
        }

        return consultation;
    }
}
