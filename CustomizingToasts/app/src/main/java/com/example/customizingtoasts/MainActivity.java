package com.example.customizingtoasts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "my_channel_01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        Button btnSimpleToast = findViewById(R.id.btnSimpleToast);
        Button btnCustomToast = findViewById(R.id.btnCustomToast);
        Button btnTriggerNotification = findViewById(R.id.btnNotification);

        btnSimpleToast.setOnClickListener(v -> Toast.makeText(this, "This is a simple toast", Toast.LENGTH_SHORT).show());

        btnCustomToast.setOnClickListener(v -> showCustomToast());

        btnTriggerNotification.setOnClickListener(v -> triggerNotification());

        new Thread(() -> runOnUiThread(() -> Toast.makeText(this, "Toast from Worker Thread", Toast.LENGTH_SHORT).show())).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel for notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showCustomToast() {
        // Inflate the custom layout for the toast
        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_LONG);
        customToast.setView(getLayoutInflater().inflate(R.layout.custom_toast_layout, null));
        customToast.show();
    }

    private void triggerNotification() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrationPattern = {1000, 1000, 1000, 1000};

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New Notification")
                .setContentText("This is a custom notification with sound, vibration, and lights.")
                .setSound(soundUri)
                .setVibrate(vibrationPattern)
                .setLights(0xFF00FF00, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)  // Tap action
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}