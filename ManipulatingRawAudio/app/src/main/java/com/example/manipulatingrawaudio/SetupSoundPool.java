package com.example.manipulatingrawaudio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SetupSoundPool {

    private SoundPool soundPool;
    private int soundId;

    public void setup(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundId = soundPool.load(context, R.raw.sound_clip, 1);
    }

    public void play() {
        if (soundPool != null) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }
}