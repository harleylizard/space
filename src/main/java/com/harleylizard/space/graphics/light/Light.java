package com.harleylizard.space.graphics.light;

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
        return 1.0F;
    }

    public float getG() {
        return 1.0F;
    }

    public float getB() {
        return 1.0F;
    }

    public static Light of(float r, float g, float b) {
        return new Light(Color.pack(r, g, b, 1.0F));
    }
}
