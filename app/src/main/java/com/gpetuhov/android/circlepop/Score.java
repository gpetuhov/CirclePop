package com.gpetuhov.android.circlepop;

import android.content.Context;

public class Score {
    private static Score sScore;

    private int greenNum; // Number of green circles hit
    private int redNum;   // Number of red circles hit

    public static Score get(Context context) {
        if (sScore == null) {
            sScore = new Score(context);
        }
        return sScore;
    }

    private Score(Context context) {
        greenNum = 0;
        redNum = 0;
    }

    public int getGreenNum() {
        return greenNum;
    }

    public int getRedNum() {
        return redNum;
    }

    public void addRed() {
        redNum++;
    }

    public void addGreen() {
        greenNum++;
    }
}
