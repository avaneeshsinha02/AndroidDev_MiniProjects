package com.example.manipulatingrawaudio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class PlayAudio {

    private AudioTrack audioTrack;

    public void playSound() {
        int sampleRate = MainActivity.SAMPLE_RATE;

        int bufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);

        audioTrack.play();

        byte[] buffer = new byte[bufferSize];
        audioTrack.write(buffer, 0, buffer.length);
    }

    public void stop() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }

    public AudioTrack getAudioTrack() {
        return audioTrack;
    }
}