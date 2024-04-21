package com.harleylizard.space.graphics.text;

public final class FontCoordinates {
    private final float minX;
    private final float minY;
    private final float maxX;
    private final float maxY;

    private FontCoordinates(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public static FontCoordinates of(int width, int height, int x, int y) {
        var sizeX = 8.0F / (float) width;
        var sizeY = 8.0F / (float) height;
        return new FontCoordinates(0.0F, 0.0F, sizeX, sizeY);
    }
}
