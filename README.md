# MediManager

**Authors:** Iyed Kadri, Rani Charradi, Youssef Zaghouni (TP3)

## Description

MediManager is a native Android application developed to facilitate the daily management of patients in a medical practice. It allows doctors to efficiently manage patient information, consultations, and appointments via a modern and intuitive interface.

## Objectives

- Centralize patient medical information.
- Track consultation history.
- Manage appointments with a notification system.
- Offer a simple and fast user interface.
- Guarantee data persistence via SQLite.

## Functional Requirements

### Patient Management (Full CRUD)

- **Features:** Add, Edit, Delete (with confirmation), Search by name, View details.
- **Patient Info:** Name, Date of Birth (auto age calculation), Gender, Phone, Email, Address, Blood Group, Allergies, Creation Date.

### Consultation Management

- **Features:** Add, View History, Edit, Delete, Filter by date.
- **Consultation Info:** Patient link, Date, Diagnosis, Treatment, Prescription, Notes.

### Appointment Management

- **Features:** Schedule, Edit, Cancel, Mark as Completed, View (Day/Week/All), Notifications.
- **Appointment Info:** Patient link, Date, Time, Reason, Status (Scheduled/Completed/Cancelled), Notes.

### Dashboard & Statistics

- Total number of registered patients.
- Number of consultations in the current month.
- Number of upcoming appointments.
- List of recently added patients.

## Technical Specifications

- **Language:** Java 21
- **SDK:** Min API 23 (Target API 36)
- **Database:** SQLite (Manual implementation, no ORM)
- **Architecture:** MVC (Model-View-Controller)
- **UI:** Material Design 3, ViewBinding

## Database Schema

**Table `patients`**

```sql
CREATE TABLE patients (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    date_of_birth TEXT,
    gender TEXT,
    phone TEXT,
    email TEXT,
    address TEXT,
    blood_group TEXT,
    allergies TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);
```

**Table `consultations`**

```sql
CREATE TABLE consultations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    patient_id INTEGER NOT NULL,
    consultation_date TEXT NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    prescription TEXT,
    notes TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);
```

**Table `appointments`**

```sql
CREATE TABLE appointments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    patient_id INTEGER NOT NULL,
    appointment_date TEXT NOT NULL,
    appointment_time TEXT NOT NULL,
    reason TEXT,
    status TEXT DEFAULT 'scheduled',
    notes TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);
```

## Project Structure

```text
app/
  src/main/java/com/example/medimanager/
    activities/           # Main screens (MainActivity, AddPatientActivity, etc.)
    fragments/            # Dashboard and list fragments
    adapters/             # RecyclerView adapters
    database/             # SQLite helper and DAO classes
    models/               # Data models (Patient, Consultation, Appointment)
    utils/                # Constants and utilities
  src/main/res/           # Layouts, drawables, values
```

## Getting Started

**Requirements**: Android Studio, Java 21 JDK, Android SDK 23+.

**Clone the repo:**

```bash
git clone https://github.com/Treshaun/MediManager.git
```

**Build:**

```bash
./gradlew assembleDebug
```

`local.properties` should point to your SDK path; Android Studio will generate it automatically when you open the project.

## Database Notes

- `DatabaseHelper` seeds sample patients/appointments on first run and enforces foreign keys.
- Schema changes require updating table constants, DAO CRUD operations, and corresponding model fields/forms.
- Appointment status values must remain lowercase (`scheduled`, `in_progress`, `completed`, `cancelled`) to keep chips and adapters in sync.

## Contributing

1. Create a feature branch from `master`.
2. Follow the existing patterns (view binding, DAO filtering, `Constants` extras) when adding screens or fields.
3. Run `./gradlew lint` / builds before opening a pull request.
4. Submit a PR describing changes and any database migrations performed.
