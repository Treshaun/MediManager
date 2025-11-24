# MediManager – AI Guide

## Big Picture
- Single-module Android app (`app/`) targeting SDK 36, min 23, Java 17, Material 1.13, and view binding enabled (`app/build.gradle.kts`).
- `MainActivity` (`activities/MainActivity.java`) hosts `HomeFragment`, `AppointmentsFragment`, `PatientsFragment`, `ProfileFragment` inside `fragment_container` with a bottom nav and FAB whose action swaps between Add Patient/Appointment screens.
- Fragments are mostly read-only dashboards; creation/edit flows live in dedicated activities under `activities/` and return to fragments via `ActivityResultLauncher` refresh hooks.

## Data Layer
- Local persistence is manual SQLite via `database/DatabaseHelper` and DAO classes (`AppointmentDAO`, `PatientDAO`, `ConsultationDAO`). No Room/ORM—every schema change requires updating the CREATE_TABLE strings, downgrades (drop + recreate) and the corresponding `cursorTo*` mappers.
- Tables: `patients`, `consultations`, `appointments`. Foreign keys enforced in `onCreate/onOpen`. Sample data is seeded once; tests relying on empty DB must purge rows manually.
- Shared field names live in `DatabaseHelper`; keep them in sync with `models/*` POJOs used for binding/serialization.

## UI & Interaction Patterns
- Activities/fragments rely on generated bindings (`ActivityAddPatientBinding`, `FragmentAppointmentsBinding`, etc.); never call `findViewById` in new code.
- Lists use RecyclerView adapters under `adapters/` (e.g., `AppointmentAdapter`) that expose explicit listener interfaces for row taps vs status toggles—re-use these instead of wiring new click handlers in fragments.
- `AppointmentsFragment` demonstrates the canonical filtering pattern: load via DAO, keep `appointmentList` + `filteredList`, drive UI state (empty view vs list) manually.
- `HomeFragment` aggregates metrics (`PatientDAO.getTotalPatientsCount()`, `AppointmentDAO.getTodayAppointmentsCount()`, etc.) and mirrors the “load on `onResume`” rule—follow the same lifecycle hook when adding data-backed fragments.
- Screen-to-screen data passes through `utils/Constants` intent extras (e.g., `EXTRA_PATIENT_ID`, `EXTRA_IS_EDIT_MODE`); never hardcode strings.

## Scheduling & Notifications
- `AddAppointmentActivity` schedules reminders an hour before the visit using `AlarmManager` + `AppointmentNotificationReceiver`. When editing appointment time you must cancel/reschedule by reusing the same `PendingIntent` key (`appointmentId`).
- Status chips/text map to the lowercase constants in `Constants` while adapters display friendly labels via `Appointment.getStatusDisplayName()`—always persist the lowercase value.

## Developer Workflow
- Build APK: `pwsh> .\gradlew.bat assembleDebug`. Clean with `pwsh> .\gradlew.bat clean` if bindings/desugaring glitch.
- Instrumented tests (none yet) run via `pwsh> .\gradlew.bat connectedDebugAndroidTest`; unit tests via `pwsh> .\gradlew.bat testDebugUnitTest`.
- View binding classes regenerate on each build; if IDE warnings appear, rerun `assembleDebug` instead of manually editing `build/generated/` sources.

## Extending the App
- Adding new patient/appointment fields requires: column + default in `DatabaseHelper`, DAO CRUD updates, model getters/setters, existing forms (layouts + activities) and adapters/bindings.
- New list UIs should live in fragments and reuse the DAO filtering patterns plus binding-driven empty states (`tvEmptyState`).
- Before introducing libraries (Room, Retrofit, etc.), ensure Gradle version catalogs (`gradle/libs.versions.toml`) include versions and wire aliases to `app/build.gradle.kts`.

## Gotchas
- `AppointmentNotificationReceiver` references `R.drawable.ic_notification`; confirm the asset exists or update when renaming drawables.
- Database upgrade currently wipes tables—if persistence matters, add migrations before raising `DATABASE_VERSION`.
- Many buttons currently show placeholder toasts (“coming soon”); keep UX consistent by reusing the same message or fully implementing the feature.
