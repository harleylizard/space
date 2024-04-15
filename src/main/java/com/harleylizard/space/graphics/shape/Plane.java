package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.VertexParameters;

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

    @Override
    public void build(VertexParameters parameters) {
        parameters.vertex(fromX, fromY, 0.0F, 0.0F, 0.0F);
        parameters.vertex(toX, fromY, 0.0F, 0.0F, 0.0F);
        parameters.vertex(toX, toY, 0.0F, 0.0F, 0.0F);
        parameters.vertex(fromX, toY, 0.0F, 0.0F, 0.0F);
    }
}
