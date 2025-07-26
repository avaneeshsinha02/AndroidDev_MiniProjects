package com.example.enhancingyourviews;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CustomSurfaceView customSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customSurfaceView = new CustomSurfaceView(this);
        setContentView(customSurfaceView);

        customSurfaceView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Toast.makeText(MainActivity.this, "Touch event detected", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            Toast.makeText(this, "D-Pad Center Pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        Toast.makeText(this, "Trackball event detected", Toast.LENGTH_SHORT).show();
        return true;
    }
}
