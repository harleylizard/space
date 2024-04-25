package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.CullGetter;
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
    private final float pivotX;
    private final float pivotY;
    private final float pivotZ;
    private final float rotationX;
    private final float rotationY;
    private final float rotationZ;

    private final Map<Direction, Face> faces;

    public Cube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, float pivotX, float pivotY, float pivotZ, float rotationX, float rotationY, float rotationZ, Map<Direction, Face> faces) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.pivotZ = pivotZ;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.faces = faces;
    }

    @Override
    public void build(CullGetter cullGetter, VertexParameters parameters, Matrix4fStack stack, int x, int y, int z, boolean ambient, int i) {
        stack.pushMatrix();
        stack.translate(pivotX, pivotY, pivotZ);
        stack.rotate(rotationX, 1.0F, 0.0F, 0.0F);
        stack.rotate(rotationY, 0.0F, 1.0F, 0.0F);
        stack.rotate(rotationX, 0.0F, 0.0F, 1.0F);
        stack.translate(-pivotX, -pivotY, -pivotZ);

        if (hasFace(ambient, cullGetter, Direction.NORTH, x, y, z)) {
            var face = faces.get(Direction.NORTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();
            parameters.vertex(stack, toX, fromY, fromZ, minU, maxV, t, 0, 0, -1, i, ambient);
            parameters.vertex(stack, fromX, fromY, fromZ, maxU, maxV, t, 0, 0, -1, i, ambient);
            parameters.vertex(stack, fromX, toY, fromZ, maxU, minV, t, 0, 0, -1, i, ambient);
            parameters.vertex(stack, toX, toY, fromZ, minU, minV, t, 0, 0, -1, i, ambient);
        }
        if (hasFace(ambient, cullGetter, Direction.EAST, x, y, z)) {
            var face = faces.get(Direction.EAST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();
            parameters.vertex(stack, toX, fromY, toZ, minU, maxV, t, 1, 0, 0, i, ambient);
            parameters.vertex(stack, toX, fromY, fromZ, maxU, maxV, t, 1, 0, 0, i, ambient);
            parameters.vertex(stack, toX, toY, fromZ, maxU, minV, t, 1, 0, 0, i, ambient);
            parameters.vertex(stack, toX, toY, toZ, minU, minV, t, 1, 0, 0, i, ambient);
        }
        if (hasFace(ambient, cullGetter, Direction.SOUTH, x, y, z)) {
            var face = faces.get(Direction.SOUTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();
            parameters.vertex(stack, fromX, fromY, toZ, minU, maxV, t, 0, 0, 1, i, ambient);
            parameters.vertex(stack, toX, fromY, toZ, maxU, maxV, t, 0, 0, 1, i, ambient);
            parameters.vertex(stack, toX, toY, toZ, maxU, minV, t, 0, 0, 1, i, ambient);
            parameters.vertex(stack, fromX, toY, toZ, minU, minV, t, 0, 0, 1, i, ambient);
        }
        if (hasFace(ambient, cullGetter, Direction.WEST, x, y, z)) {
            var face = faces.get(Direction.WEST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();
            parameters.vertex(stack, fromX, fromY, fromZ, minU, maxV, t, -1, 0, 0, i, ambient);
            parameters.vertex(stack, fromX, fromY, toZ, maxU, maxV, t, -1, 0, 0, i, ambient);
            parameters.vertex(stack, fromX, toY, toZ, maxU, minV, t, -1, 0, 0, i, ambient);
            parameters.vertex(stack, fromX, toY, fromZ, minU, minV, t, -1, 0, 0, i, ambient);
        }
        if (hasFace(ambient, cullGetter, Direction.UP, x, y, z)) {
            var face = faces.get(Direction.UP);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();
            parameters.vertex(stack, toX, toY, fromZ, minU, maxV, t, 0, 1, 0, i, ambient);
            parameters.vertex(stack, fromX, toY, fromZ, maxU, maxV, t, 0, 1, 0, i, ambient);
            parameters.vertex(stack, fromX, toY, toZ, maxU, minV, t, 0, 1, 0, i, ambient);
            parameters.vertex(stack, toX, toY, toZ, minU, minV, t, 0, 1, 0, i, ambient);
        }
        if (hasFace(ambient, cullGetter, Direction.DOWN, x, y, z)) {
            var face = faces.get(Direction.DOWN);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();
            parameters.vertex(stack, fromX, fromY, fromZ, minU, maxV, t, 0, -1, 0, i, ambient);
            parameters.vertex(stack, toX, fromY, fromZ, maxU, maxV, t, 0, -1, 0, i, ambient);
            parameters.vertex(stack, toX, fromY, toZ, maxU, minV, t, 0, -1, 0, i, ambient);
            parameters.vertex(stack, fromX, fromY, toZ, minU, minV, t, 0, -1, 0, i, ambient);
        }
        stack.popMatrix();
    }

    private boolean hasFace(boolean ambient, CullGetter cullGetter, Direction direction, int x, int y, int z) {
        return (!cullGetter.shouldCull(x, y, z, direction) || ambient) && faces.containsKey(direction);
    }
}
