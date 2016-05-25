package com.gpetuhov.android.circlepop;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class PopSound {
    public static final int MAX_SOUNDS = 1;

    private SoundPool mSoundPool;

    private int greenSoundId;
    private int redSoundId;

    public PopSound(Context context) {
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        greenSoundId = mSoundPool.load(context, R.raw.green_pop, 1);
        redSoundId = mSoundPool.load(context, R.raw.red_pop, 1);
    }

    public void redPop() {
        mSoundPool.play(redSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void greenPop() {
        mSoundPool.play(greenSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
