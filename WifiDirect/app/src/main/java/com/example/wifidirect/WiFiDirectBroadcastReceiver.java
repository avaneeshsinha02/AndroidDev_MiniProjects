package com.example.wifidirect;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.*;
import androidx.core.app.ActivityCompat;
import java.util.List;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private MainActivity activity;
    private Context WiFiDirectBroadcastReceiver;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity, Context wiFiDirectBroadcastReceiver) {
        this.wifiP2pManager = manager;
        this.channel = channel;
        this.activity = activity;
        WiFiDirectBroadcastReceiver = wiFiDirectBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (ActivityCompat.checkSelfPermission(this.WiFiDirectBroadcastReceiver, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this.WiFiDirectBroadcastReceiver,
                    Manifest.permission.NEARBY_WIFI_DEVICES)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            wifiP2pManager.requestPeers(channel, peers -> {
                List<WifiP2pDevice> deviceList = (List<WifiP2pDevice>) peers.getDeviceList();
                for (WifiP2pDevice device : deviceList) {
                    activity.connectWithPeer(device);
                }
            });
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            wifiP2pManager.requestConnectionInfo(channel, activity::onConnectionInfoAvailable);
        }
    }
}
