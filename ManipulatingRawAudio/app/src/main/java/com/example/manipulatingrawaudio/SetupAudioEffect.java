package com.example.manipulatingrawaudio;

import android.media.audiofx.Equalizer;
import android.media.AudioTrack;

public class SetupAudioEffect {

    private Equalizer equalizer;

    public void applyEffect(AudioTrack audioTrack) {
        int sessionId = audioTrack.getAudioSessionId();

        equalizer = new Equalizer(0, sessionId);
        equalizer.setEnabled(true);

        short bands = equalizer.getNumberOfBands();
        for (short i = 0; i < bands; i++) {
            equalizer.setBandLevel(i, equalizer.getBandLevelRange()[1]);
        }
    }

    public void release() {
        if (equalizer != null) {
            equalizer.release();
        }
    }
}