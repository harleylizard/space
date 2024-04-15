package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.VertexParameters;
import com.harleylizard.space.math.Direction;

import java.util.Map;

public final class Cube implements Shape {
    private final float fromX;
    private final float fromY;
    private final float fromZ;
    private final float toX;
    private final float toY;
    private final float toZ;

    private final Map<Direction, Face> faces;

    public Cube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, Map<Direction, Face> faces) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
        this.faces = faces;
    }

    @Override
    public void build(VertexParameters parameters) {
        if (faces.containsKey(Direction.NORTH)) {
            var face = faces.get(Direction.NORTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            parameters.vertex(toX, fromY, fromZ, minU, maxV);
            parameters.vertex(fromX, fromY, fromZ, maxU, maxV);
            parameters.vertex(fromX, toY, fromZ, maxU, minV);
            parameters.vertex(toX, toY, fromZ, minU, minV);
        }
        if (faces.containsKey(Direction.EAST)) {
            var face = faces.get(Direction.EAST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            parameters.vertex(toX, fromY, toZ, minU, maxV);
            parameters.vertex(toX, fromY, fromZ, maxU, maxV);
            parameters.vertex(toX, toY, fromZ, maxU, minV);
            parameters.vertex(toX, toY, toZ, minU, minV);
        }
        if (faces.containsKey(Direction.SOUTH)) {
            var face = faces.get(Direction.SOUTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            parameters.vertex(fromX, fromY, toZ, minU, maxV);
            parameters.vertex(toX, fromY, toZ, maxU, maxV);
            parameters.vertex(toX, toY, toZ, maxU, minV);
            parameters.vertex(fromX, toY, toZ, minU, minV);
        }
        if (faces.containsKey(Direction.WEST)) {
            var face = faces.get(Direction.WEST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            parameters.vertex(fromX, fromY, fromZ, minU, maxV);
            parameters.vertex(fromX, fromY, toZ, maxU, maxV);
            parameters.vertex(fromX, toY, toZ, maxU, minV);
            parameters.vertex(fromX, toY, fromZ, minU, minV);
        }

        parameters.vertex(toX, toY, fromZ, 0.0F, 0.0F);
        parameters.vertex(fromX, toY, fromZ, 0.0F, 0.0F);
        parameters.vertex(fromX, toY, toZ, 0.0F, 0.0F);
        parameters.vertex(toX, toY, toZ, 0.0F, 0.0F);

        parameters.vertex(fromX, fromY, fromZ, 0.0F, 0.0F);
        parameters.vertex(toX, fromY, fromZ, 0.0F, 0.0F);
        parameters.vertex(toX, fromY, toZ, 0.0F, 0.0F);
        parameters.vertex(fromX, fromY, toZ, 0.0F, 0.0F);
    }
}
