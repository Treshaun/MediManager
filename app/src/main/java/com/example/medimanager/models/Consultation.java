package com.example.medimanager.models;

import java.io.Serializable;

public class Consultation implements Serializable {
    private int id;
    private int patientId;
    private String consultationDate;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
    private String createdAt;

    // Constructors
    public Consultation() {
    }

    public Consultation(int id, int patientId, String consultationDate,
                        String diagnosis, String treatment, String prescription,
                        String notes, String createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.consultationDate = consultationDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(String consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", date='" + consultationDate + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }
}