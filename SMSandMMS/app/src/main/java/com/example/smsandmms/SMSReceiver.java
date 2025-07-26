package com.example.smsandmms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageReceived = "";
        String phoneNumber = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    phoneNumber = msgs[i].getOriginatingAddress();
                    messageReceived += msgs[i].getMessageBody();
                }
                Log.d("SmsReceiver", "Message received from: " + phoneNumber + " - " + messageReceived);

                if (messageReceived.contains("HELP")) {
                    sendAutomatedResponse(context, phoneNumber);
                }
            }
        }
    }

    private void sendAutomatedResponse(Context context, String phoneNumber) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String responseMessage = "Emergency detected! Help is on the way.";
            smsManager.sendTextMessage(phoneNumber, null, responseMessage, null, null);
            Toast.makeText(context, "Automated response sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to send automated response", Toast.LENGTH_SHORT).show();
        }
    }
}
