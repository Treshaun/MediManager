# Development Change Log

## [Unreleased] - 2025-11-24

### Added

- **Profile Features**:
  - Implemented `EditProfileActivity` to allow users to update their name and email.
  - Implemented `NotificationSettingsActivity` to toggle app notifications.
  - Wired up "Edit Profile" and "Notifications" buttons in `ProfileFragment`.
- **Localization (Tunisia)**:
  - Added Tunisian phone number validation in `ValidationUtils.java` (supports 8-digit local and +216 international formats).
  - Updated `DatabaseHelper` to seed the database with Tunisian sample data (Names: Fatma Trabelsi, Mohamed Ben Ali, etc.; Phones: +216...).
- **Resources**:
  - Added new string resources for profile and settings screens in `strings.xml`.

### Changed

- **Database**:
  - Incremented `DATABASE_VERSION` to 2 to trigger a schema update and data re-seeding.
- **UI/UX**:
  - Updated default user profile to "Dr. Ben Amor" across `ProfileFragment`, `EditProfileActivity`, and `strings.xml`.
  - Replaced hardcoded strings in `activity_edit_profile.xml` and `activity_notification_settings.xml` with string resources.
- **Logic**:
  - Updated `AppointmentNotificationReceiver` to respect the user's "Enable Notifications" preference from SharedPreferences.
- **Fixes**:
  - Resolved duplicate string resource errors (`save`, `edit_profile`) in `strings.xml`.
  - Fixed build errors related to resource merging.

### Technical

- Verified no `findViewById` usage (project uses ViewBinding).
- Confirmed app stability via log analysis (ignored harmless `ashmem` warnings).
