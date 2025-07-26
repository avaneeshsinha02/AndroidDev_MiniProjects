package com.example.ensuringaccessibility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 100;
    private TextToSpeech textToSpeech;
    private EditText editText;
    private Button speakButton, animateButton, searchButton, vibrateButton;
    private ImageView imageView;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        speakButton = findViewById(R.id.speakButton);
        searchButton = findViewById(R.id.searchButton);
        animateButton = findViewById(R.id.animateButton);
        vibrateButton = findViewById(R.id.vibrateButton);
        imageView = findViewById(R.id.imageView);

        editText.setContentDescription("Text input field");
        speakButton.setContentDescription("Tap to read text aloud");
        searchButton.setContentDescription("Tap to perform voice search");
        animateButton.setContentDescription("Tap to start animation");
        vibrateButton.setContentDescription("Tap to trigger vibration feedback");

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }});

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        speakButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(MainActivity.this, "Please enter some text", Toast.LENGTH_SHORT).show();
            }});

        searchButton.setOnClickListener(v -> promptSpeechInput());

        animateButton.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.tween_animation);
            imageView.startAnimation(animation);
        });

        vibrateButton.setOnClickListener(v -> vibrator.vibrate(500)); // Vibrate for 500ms

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivitiyForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition is not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void startActivitiyForResult(Intent intent, int speechRequestCode) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                editText.setText(result.get(0));
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
