package com.harleylizard.space.math;

public final class Color {
    private static final int BIT_MASK = 0xFF;

    private Color() {}

    public static int pack(int r, int g, int b, int a) {
        return ((a & BIT_MASK) << 24) | ((r & BIT_MASK) << 16) | ((g & BIT_MASK) << 8) | (b & BIT_MASK);
    }

    public static int pack(float r, float g, float b, float a) {
        r *= 255;
        g *= 255;
        b *= 255;
        a *= 255;
        return pack((int) r, (int) g, (int) b, (int) a);
    }
}
