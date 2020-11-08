package com.pastel.dalpook.Utils;

import android.graphics.Color;

public class ColorUtils {
    /**
     * Ensure this class is only used as a utility.
     */
    private ColorUtils() {
        throw new AssertionError();
    }

    public final static int[] mColors = buildColorArray();

    private static int[] buildColorArray() {
        return new int[]{
                Color.rgb(159, 225, 231), // #9fe1e7
                Color.rgb(220, 39, 39), // #dc2727
                Color.rgb(219, 173, 255), // #dbadff
                Color.rgb(164, 189, 252), // #a4bdfc
                Color.rgb(84, 132, 237), // #5484ed
                Color.rgb(70, 214, 219), // #46d6db
                Color.rgb(122, 231, 191), // #7ae7bf
                Color.rgb(81, 183, 73), // #51b749
                Color.rgb(251, 215, 91), // #fbd85b
                Color.rgb(255, 184, 120), // #ffb878
                Color.rgb(255, 136, 124), // #ff877c
                Color.rgb(225, 225, 225) // #e1e1e1
        };
    }
}
