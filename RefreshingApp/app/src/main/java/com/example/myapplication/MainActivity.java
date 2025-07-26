package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });

        findViewById(R.id.btnShowAlertDialog).setOnClickListener(view -> showAlertDialog());

        findViewById(R.id.btnShowInputDialog).setOnClickListener(view -> showInputDialog());

        findViewById(R.id.btnShowDialogFragment).setOnClickListener(view -> {
            DialogFragment newFragment = new CustomDialogFragment();
            newFragment.show(getSupportFragmentManager(), "dialog");
        });

        findViewById(R.id.btnShowActivityDialog).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DialogActivity.class);
            startActivity(intent);
        });
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Alert Dialog")
                .setMessage("This is a basic alert dialog.")
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showInputDialog() {
        CustomInputDialogFragment inputDialog = new CustomInputDialogFragment();
        inputDialog.show(getSupportFragmentManager(), "inputDialog");
    }
}
