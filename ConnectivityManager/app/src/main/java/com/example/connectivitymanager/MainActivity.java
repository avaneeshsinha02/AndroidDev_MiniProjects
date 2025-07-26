package com.example.connectivitymanager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView connectionStatusTextView;
    private TextView wifiInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionStatusTextView = findViewById(R.id.connection_status_text);
        wifiInfoTextView = findViewById(R.id.wifi_info_text);

        Button checkConnectivityButton = findViewById(R.id.check_connectivity_button);
        checkConnectivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkConnectivity();
            }
        });

        Button manageWiFiButton = findViewById(R.id.manage_wifi_button);
        manageWiFiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageWiFi();
            }
        });
    }

    private void checkNetworkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                connectionStatusTextView.setText("Connected to Wi-Fi");
                getWiFiConnectionDetails();
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                connectionStatusTextView.setText("Connected to Mobile Data");
            } else {
                connectionStatusTextView.setText("Connected to some other network");
            }
        } else {
            connectionStatusTextView.setText("No network connection");
        }
    }

    private void getWiFiConnectionDetails() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo != null) {
            String ssid = wifiInfo.getSSID();
            int linkSpeed = wifiInfo.getLinkSpeed();
            int signalStrength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100);

            String wifiDetails = "Connected to SSID: " + ssid + "\n" +
                    "Link Speed: " + linkSpeed + " Mbps\n" +
                    "Signal Strength: " + signalStrength + "%";

            wifiInfoTextView.setText(wifiDetails);
        } else {
            wifiInfoTextView.setText("No active Wi-Fi connection");
        }
    }

    private void manageWiFi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(this, "Wi-Fi Disabled", Toast.LENGTH_SHORT).show();
        } else {
            wifiManager.setWifiEnabled(true);
            Toast.makeText(this, "Wi-Fi Enabled", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    }

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                checkNetworkConnectivity();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReceiver);
    }

    private void scanForHotspots() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<ScanResult> results = wifiManager.getScanResults();
        for (ScanResult result : results) {
            Log.d("WiFi", "SSID: " + result.SSID + ", Signal Strength: " + result.level);
        }
    }

    private void addWiFiNetwork(String ssid, String password) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.preSharedKey = "\"" + password + "\"";

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    private void removeWiFiNetwork(String ssid) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();

        for (WifiConfiguration config : wifiConfigurations) {
            if (config.SSID != null && config.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.removeNetwork(config.networkId);
                wifiManager.saveConfiguration();
                Toast.makeText(this, "Removed Wi-Fi network: " + ssid, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private boolean isBackgroundDataRestricted() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.isActiveNetworkMetered();
    }
}