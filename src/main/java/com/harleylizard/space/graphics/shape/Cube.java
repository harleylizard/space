package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.VertexParameters;

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
    public void build(VertexParameters parameters) {
        parameters.vertex(toX, fromY, fromZ);
        parameters.vertex(fromX, fromY, fromZ);
        parameters.vertex(fromX, toY, fromZ);
        parameters.vertex(toX, toY, fromZ);

        parameters.vertex(toX, fromY, toZ);
        parameters.vertex(toX, fromY, fromZ);
        parameters.vertex(toX, toY, fromZ);
        parameters.vertex(toX, toY, toZ);

        parameters.vertex(fromX, fromY, toZ);
        parameters.vertex(toX, fromY, toZ);
        parameters.vertex(toX, toY, toZ);
        parameters.vertex(fromX, toY, toZ);

        parameters.vertex(fromX, fromY, fromZ);
        parameters.vertex(fromX, fromY, toZ);
        parameters.vertex(fromX, toY, toZ);
        parameters.vertex(fromX, toY, fromZ);

        parameters.vertex(toX, toY, fromZ);
        parameters.vertex(fromX, toY, fromZ);
        parameters.vertex(fromX, toY, toZ);
        parameters.vertex(toX, toY, toZ);

        parameters.vertex(fromX, fromY, fromZ);
        parameters.vertex(toX, fromY, fromZ);
        parameters.vertex(toX, fromY, toZ);
        parameters.vertex(fromX, fromY, toZ);
    }
}
