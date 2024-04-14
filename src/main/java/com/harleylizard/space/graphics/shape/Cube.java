package com.harleylizard.space.graphics.shape;

public final class Cube implements Shape {
    private final float fromX;
    private final float fromY;
    private final float fromZ;
    private final float toX;
    private final float toY;
    private final float toZ;

    public Cube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
    }

    @Override
    public void build(Builder builder) {

        builder.vertex(0.0F, 0.0F, 0.0F);
        builder.vertex(0.0F, 0.0F, 0.0F);
        builder.vertex(0.0F, 0.0F, 0.0F);
        builder.vertex(0.0F, 0.0F, 0.0F);
    }
}
