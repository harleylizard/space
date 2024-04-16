package com.harleylizard.space.graphics.light;

import com.harleylizard.space.math.Color;

public final class Light {
    private final int color;
    private final float range;
    private final float intensity;

    private float x;
    private float y;
    private float z;

    private Light(int color, float range, float intensity) {
        this.color = color;
        this.range = range;
        this.intensity = intensity;
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

    public float getRange() {
        return range;
    }

    public float getIntensity() {
        return intensity;
    }

    public static Light of(float r, float g, float b, float range, float intensity) {
        return new Light(Color.pack(r, g, b, 1.0F), range, intensity);
    }
}
