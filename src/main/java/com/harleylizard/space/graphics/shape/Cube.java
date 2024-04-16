package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.VertexParameters;
import com.harleylizard.space.math.Direction;
import org.joml.Matrix4fStack;

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
    public void build(VertexParameters parameters, Matrix4fStack stack) {
        if (faces.containsKey(Direction.NORTH)) {
            var face = faces.get(Direction.NORTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, toX, fromY, fromZ, minU, maxV, material, 0, 0, -1);
            parameters.vertex(stack, fromX, fromY, fromZ, maxU, maxV, material, 0, 0, -1);
            parameters.vertex(stack, fromX, toY, fromZ, maxU, minV, material, 0, 0, -1);
            parameters.vertex(stack, toX, toY, fromZ, minU, minV, material, 0, 0, -1);
        }
        if (faces.containsKey(Direction.EAST)) {
            var face = faces.get(Direction.EAST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, toX, fromY, toZ, minU, maxV, material, 1, 0, 0);
            parameters.vertex(stack, toX, fromY, fromZ, maxU, maxV, material, 1, 0, 0);
            parameters.vertex(stack, toX, toY, fromZ, maxU, minV, material, 1, 0, 0);
            parameters.vertex(stack, toX, toY, toZ, minU, minV, material, 1, 0, 0);
        }
        if (faces.containsKey(Direction.SOUTH)) {
            var face = faces.get(Direction.SOUTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, fromX, fromY, toZ, minU, maxV, material, 0, 0, 1);
            parameters.vertex(stack, toX, fromY, toZ, maxU, maxV, material, 0, 0, 1);
            parameters.vertex(stack, toX, toY, toZ, maxU, minV, material, 0, 0, 1);
            parameters.vertex(stack, fromX, toY, toZ, minU, minV, material, 0, 0, 1);
        }
        if (faces.containsKey(Direction.WEST)) {
            var face = faces.get(Direction.WEST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, fromX, fromY, fromZ, minU, maxV, material, -1, 0, 0);
            parameters.vertex(stack, fromX, fromY, toZ, maxU, maxV, material, -1, 0, 0);
            parameters.vertex(stack, fromX, toY, toZ, maxU, minV, material, -1, 0, 0);
            parameters.vertex(stack, fromX, toY, fromZ, minU, minV, material, -1, 0, 0);
        }
        if (faces.containsKey(Direction.UP)) {
            var face = faces.get(Direction.UP);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, toX, toY, fromZ, minU, maxV, material, 0, 1, 0);
            parameters.vertex(stack, fromX, toY, fromZ, maxU, maxV, material, 0, 1, 0);
            parameters.vertex(stack, fromX, toY, toZ, maxU, minV, material, 0, 1, 0);
            parameters.vertex(stack, toX, toY, toZ, minU, minV, material, 0, 1, 0);
        }
        if (faces.containsKey(Direction.DOWN)) {
            var face = faces.get(Direction.DOWN);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, fromX, fromY, fromZ, minU, maxV, material, 0, -1, 0);
            parameters.vertex(stack, toX, fromY, fromZ, maxU, maxV, material, 0, -1, 0);
            parameters.vertex(stack, toX, fromY, toZ, maxU, minV, material, 0, -1, 0);
            parameters.vertex(stack, fromX, fromY, toZ, minU, minV, material, 0, -1, 0);
        }
    }
}
