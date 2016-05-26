package com.gpetuhov.android.circlepop;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class PopSound {
    public static final int MAX_SOUNDS = 1; // Number of sounds played simultaneously

    private SoundPool mSoundPool;

    private int greenSoundId;   // Id of green pop sound in SoundPool
    private int redSoundId;     // Id of red pop sound in SoundPool

    public PopSound(Context context) {
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);

        // Sounds are located in raw folder
        greenSoundId = mSoundPool.load(context, R.raw.green_pop, 1);    // Load green pop sound
        redSoundId = mSoundPool.load(context, R.raw.red_pop, 1);        // Load green red sound
    }

    // Play red pop sound
    public void redPop() {
        mSoundPool.play(redSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    // Play green pop sound
    public void greenPop() {
        mSoundPool.play(greenSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
