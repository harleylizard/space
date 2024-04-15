package com.harleylizard.space.graphics.shape;

public final class Face {
    private final int texture;
    private final float minU;
    private final float minV;
    private final float maxU;
    private final float maxV;

    public Face(int texture, float minU, float minV, float maxU, float maxV) {
        this.texture = texture;
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    public int getTexture() {
        return texture;
    }

    public float getMinU() {
        return minU;
    }

    public float getMinV() {
        return minV;
    }

    public float getMaxU() {
        return maxU;
    }

    public float getMaxV() {
        return maxV;
    }
}
