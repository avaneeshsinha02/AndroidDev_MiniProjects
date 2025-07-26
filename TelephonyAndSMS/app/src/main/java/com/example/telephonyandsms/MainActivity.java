package com.example.telephonyandsms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PHONE_STATE = 100;
    private static final int REQUEST_CODE_CALL_PHONE = 101;

    private TelephonyManager telephonyManager;
    private TextView simInfo, networkInfo;
    private Button callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simInfo = findViewById(R.id.sim_info);
        networkInfo = findViewById(R.id.network_info);
        callButton = findViewById(R.id.call_button);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Toast.makeText(this, "Telephony Supported", Toast.LENGTH_SHORT).show();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PHONE_STATE);
            } else {
                readSIMAndNetworkDetails();
                listenToPhoneStateChanges();
            }

        } else {
            Toast.makeText(this, "Telephony Not Supported", Toast.LENGTH_SHORT).show();
        }

        callButton.setOnClickListener(v -> initiatePhoneCall("+1234567890"));
    }

    private void readSIMAndNetworkDetails() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        String simOperatorName = telephonyManager.getSimOperatorName();
        String simCountry = telephonyManager.getSimCountryIso();
        int simState = telephonyManager.getSimState();
        String networkOperatorName = telephonyManager.getNetworkOperatorName();
        int networkType = telephonyManager.getNetworkType();

        String simSerial = "Restricted on Android 10+";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            simSerial = telephonyManager.getSimSerialNumber();
        }

        simInfo.setText("SIM Operator: " + simOperatorName +
                "\nSIM Country: " + simCountry +
                "\nSIM Serial: " + simSerial +
                "\nSIM State: " + simState);

        networkInfo.setText("Network Operator: " + networkOperatorName +
                "\nNetwork Type: " + networkType);
    }

    private void listenToPhoneStateChanges() {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("Call State", "Idle");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("Call State", "Ringing: " + phoneNumber);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d("Call State", "Off Hook");
                        break;
                }
            }

            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                Log.d("Signal Strength", "Signal Strength: " + signalStrength.getGsmSignalStrength());
            }

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                Log.d("Service State", "Service State Changed: " + serviceState.getState());
            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE |
                        PhoneStateListener.LISTEN_SIGNAL_STRENGTHS |
                        PhoneStateListener.LISTEN_SERVICE_STATE);
    }

    private void initiatePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
        }
    }

    private void checkNetworkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected) {
            Log.d("Network Connectivity", "Connected to: " + activeNetwork.getTypeName());
        } else {
            Log.d("Network Connectivity", "Not Connected");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSIMAndNetworkDetails();
                listenToPhoneStateChanges();
            } else {
                Toast.makeText(this, "Permission denied to read phone state", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiatePhoneCall("+1234567890");
            } else {
                Toast.makeText(this, "Permission denied to make calls", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
