package com.example.medimanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.medimanager.activities.MainActivity;

public class AppointmentNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "appointment_reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if notifications are enabled
        android.content.SharedPreferences prefs = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        boolean areNotificationsEnabled = prefs.getBoolean("notifications_enabled", true);

        if (!areNotificationsEnabled) {
            return;
        }

        int appointmentId = intent.getIntExtra("appointment_id", -1);
        String patientName = intent.getStringExtra("patient_name");
        String appointmentTime = intent.getStringExtra("appointment_time");

        if (appointmentId != -1) {
            createNotificationChannel(context);
            showNotification(context, appointmentId, patientName, appointmentTime);
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Appointment Reminders";
            String description = "Channel for appointment reminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Context context, int appointmentId, String patientName, String appointmentTime) {
        // Intent to open the app when notification is clicked
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Appointment Reminder")
                .setContentText("You have an appointment with " + patientName + " at " + appointmentTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(appointmentId, builder.build());
    }
}
