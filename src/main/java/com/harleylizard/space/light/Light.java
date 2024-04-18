package com.harleylizard.space.light;

import com.harleylizard.space.math.Color;

public final class Light {
    private final int color;
    private float x;
    private float y;
    private float z;

    private Light(int color) {
        this.color = color;
    }

    public void move(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getR() {
        return ((color >> 16) & 0xFF) / 255.0F;
    }

    public float getG() {
        return ((color >> 8) & 0xFF) / 255.0F;
    }

    public float getB() {
        return (color & 0xFF) / 255.0F;
    }

    public float getA() {
        return ((color >> 24) & 0xFF) / 255.0F;
    }

    public static Light of(int color) {
        return new Light(color);
    }

    public static Light of(float r, float g, float b, float a) {
        return new Light(Color.pack(r, g, b, a));
    }
}
