package com.example.medimanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    // Database Info
    private static final String DATABASE_NAME = "medimanager.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_PATIENTS = "patients";
    public static final String TABLE_CONSULTATIONS = "consultations";
    public static final String TABLE_APPOINTMENTS = "appointments";

    // Common Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    // Patients Table Columns
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_BLOOD_GROUP = "blood_group";
    public static final String KEY_ALLERGIES = "allergies";
    public static final String KEY_LAST_VISIT = "last_visit";

    // Consultations Table Columns
    public static final String KEY_PATIENT_ID = "patient_id";
    public static final String KEY_CONSULTATION_DATE = "consultation_date";
    public static final String KEY_DIAGNOSIS = "diagnosis";
    public static final String KEY_TREATMENT = "treatment";
    public static final String KEY_PRESCRIPTION = "prescription";
    public static final String KEY_NOTES = "notes";

    // Appointments Table Columns
    public static final String KEY_APPOINTMENT_DATE = "appointment_date";
    public static final String KEY_APPOINTMENT_TIME = "appointment_time";
    public static final String KEY_REASON = "reason";
    public static final String KEY_STATUS = "status";

    // Create Tables SQL
    private static final String CREATE_TABLE_PATIENTS =
            "CREATE TABLE " + TABLE_PATIENTS + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FIRST_NAME + " TEXT NOT NULL, " +
                    KEY_LAST_NAME + " TEXT NOT NULL, " +
                    KEY_DATE_OF_BIRTH + " TEXT, " +
                    KEY_GENDER + " TEXT, " +
                    KEY_PHONE + " TEXT, " +
                    KEY_EMAIL + " TEXT, " +
                    KEY_ADDRESS + " TEXT, " +
                    KEY_BLOOD_GROUP + " TEXT, " +
                    KEY_ALLERGIES + " TEXT, " +
                    KEY_LAST_VISIT + " TEXT, " +
                    KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    private static final String CREATE_TABLE_CONSULTATIONS =
            "CREATE TABLE " + TABLE_CONSULTATIONS + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_PATIENT_ID + " INTEGER NOT NULL, " +
                    KEY_CONSULTATION_DATE + " TEXT NOT NULL, " +
                    KEY_DIAGNOSIS + " TEXT, " +
                    KEY_TREATMENT + " TEXT, " +
                    KEY_PRESCRIPTION + " TEXT, " +
                    KEY_NOTES + " TEXT, " +
                    KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + KEY_PATIENT_ID + ") REFERENCES " +
                    TABLE_PATIENTS + "(" + KEY_ID + ") ON DELETE CASCADE" +
                    ")";

    private static final String CREATE_TABLE_APPOINTMENTS =
            "CREATE TABLE " + TABLE_APPOINTMENTS + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_PATIENT_ID + " INTEGER NOT NULL, " +
                    KEY_APPOINTMENT_DATE + " TEXT NOT NULL, " +
                    KEY_APPOINTMENT_TIME + " TEXT NOT NULL, " +
                    KEY_REASON + " TEXT, " +
                    KEY_STATUS + " TEXT DEFAULT 'scheduled', " +
                    KEY_NOTES + " TEXT, " +
                    KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + KEY_PATIENT_ID + ") REFERENCES " +
                    TABLE_PATIENTS + "(" + KEY_ID + ") ON DELETE CASCADE" +
                    ")";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Create tables
        db.execSQL(CREATE_TABLE_PATIENTS);
        db.execSQL(CREATE_TABLE_CONSULTATIONS);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);

        // Insert sample data for testing
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSULTATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insert sample patients
        db.execSQL("INSERT INTO " + TABLE_PATIENTS + " (" +
                KEY_FIRST_NAME + ", " + KEY_LAST_NAME + ", " + KEY_DATE_OF_BIRTH + ", " +
                KEY_GENDER + ", " + KEY_PHONE + ", " + KEY_EMAIL + ", " +
                KEY_BLOOD_GROUP + ", " + KEY_LAST_VISIT + ") VALUES " +
                "('Sarah', 'Johnson', '1990-05-15', 'Female', '+1 234-567-8901', 'sarah.j@email.com', 'A+', '2025-11-10')");

        db.execSQL("INSERT INTO " + TABLE_PATIENTS + " (" +
                KEY_FIRST_NAME + ", " + KEY_LAST_NAME + ", " + KEY_DATE_OF_BIRTH + ", " +
                KEY_GENDER + ", " + KEY_PHONE + ", " + KEY_EMAIL + ", " +
                KEY_BLOOD_GROUP + ", " + KEY_LAST_VISIT + ") VALUES " +
                "('Michael', 'Chen', '1978-08-22', 'Male', '+1 234-567-8902', 'michael.c@email.com', 'O+', '2025-11-12')");

        db.execSQL("INSERT INTO " + TABLE_PATIENTS + " (" +
                KEY_FIRST_NAME + ", " + KEY_LAST_NAME + ", " + KEY_DATE_OF_BIRTH + ", " +
                KEY_GENDER + ", " + KEY_PHONE + ", " + KEY_EMAIL + ", " +
                KEY_BLOOD_GROUP + ", " + KEY_LAST_VISIT + ") VALUES " +
                "('Emma', 'Davis', '1997-03-08', 'Female', '+1 234-567-8903', 'emma.d@email.com', 'B+', '2025-11-08')");

        db.execSQL("INSERT INTO " + TABLE_PATIENTS + " (" +
                KEY_FIRST_NAME + ", " + KEY_LAST_NAME + ", " + KEY_DATE_OF_BIRTH + ", " +
                KEY_GENDER + ", " + KEY_PHONE + ", " + KEY_EMAIL + ", " +
                KEY_BLOOD_GROUP + ", " + KEY_LAST_VISIT + ") VALUES " +
                "('James', 'Wilson', '1973-11-30', 'Male', '+1 234-567-8904', 'james.w@email.com', 'AB+', '2025-11-09')");

        // Insert sample appointments
        db.execSQL("INSERT INTO " + TABLE_APPOINTMENTS + " (" +
                KEY_PATIENT_ID + ", " + KEY_APPOINTMENT_DATE + ", " + KEY_APPOINTMENT_TIME + ", " +
                KEY_REASON + ", " + KEY_STATUS + ") VALUES " +
                "(1, '2025-11-12', '09:00 AM', 'Check-up', 'completed')");

        db.execSQL("INSERT INTO " + TABLE_APPOINTMENTS + " (" +
                KEY_PATIENT_ID + ", " + KEY_APPOINTMENT_DATE + ", " + KEY_APPOINTMENT_TIME + ", " +
                KEY_REASON + ", " + KEY_STATUS + ") VALUES " +
                "(2, '2025-11-12', '10:30 AM', 'Follow-up', 'in_progress')");

        db.execSQL("INSERT INTO " + TABLE_APPOINTMENTS + " (" +
                KEY_PATIENT_ID + ", " + KEY_APPOINTMENT_DATE + ", " + KEY_APPOINTMENT_TIME + ", " +
                KEY_REASON + ", " + KEY_STATUS + ") VALUES " +
                "(3, '2025-11-12', '02:00 PM', 'Consultation', 'scheduled')");

        db.execSQL("INSERT INTO " + TABLE_APPOINTMENTS + " (" +
                KEY_PATIENT_ID + ", " + KEY_APPOINTMENT_DATE + ", " + KEY_APPOINTMENT_TIME + ", " +
                KEY_REASON + ", " + KEY_STATUS + ") VALUES " +
                "(4, '2025-11-12', '03:30 PM', 'Check-up', 'scheduled')");
    }
}
