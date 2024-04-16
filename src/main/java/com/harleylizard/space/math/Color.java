package com.harleylizard.space.math;

public final class Color {
    private static final int BIT_MASK = 0xFF;

    private Color() {}

    public static int pack(int r, int g, int b, int a) {
        r &= BIT_MASK;
        g &= BIT_MASK;
        b &= BIT_MASK;
        a &= BIT_MASK;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int pack(float r, float g, float b, float a) {
        r *= 255;
        g *= 255;
        b *= 255;
        a *= 255;
        return pack((int) r, (int) g, (int) b, (int) a);
    }
}
