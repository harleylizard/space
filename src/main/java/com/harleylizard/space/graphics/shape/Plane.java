package com.harleylizard.space.graphics.shape;

public final class Plane implements Shape {
    private final float fromX;
    private final float fromY;
    private final float toX;
    private final float toY;

    public Plane(float fromX, float fromY, float toX, float toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }
}
