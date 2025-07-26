package com.example.invadinghomescreen;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;

public class WidgetConfigurationActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);

        editText = findViewById(R.id.edit_text);

        findViewById(R.id.button_save).setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                int appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);
                views.setTextViewText(R.id.widget_text, text);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                finish();
            } else {
                Toast.makeText(this, "Enter valid text", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
