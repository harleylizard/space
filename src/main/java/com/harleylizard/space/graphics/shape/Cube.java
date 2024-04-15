package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.texture.Material;
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
            parameters.vertex(stack, toX, fromY, fromZ, minU, maxV, material);
            parameters.vertex(stack, fromX, fromY, fromZ, maxU, maxV, material);
            parameters.vertex(stack, fromX, toY, fromZ, maxU, minV, material);
            parameters.vertex(stack, toX, toY, fromZ, minU, minV, material);
        }
        if (faces.containsKey(Direction.EAST)) {
            var face = faces.get(Direction.EAST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, toX, fromY, toZ, minU, maxV, material);
            parameters.vertex(stack, toX, fromY, fromZ, maxU, maxV, material);
            parameters.vertex(stack, toX, toY, fromZ, maxU, minV, material);
            parameters.vertex(stack, toX, toY, toZ, minU, minV, material);
        }
        if (faces.containsKey(Direction.SOUTH)) {
            var face = faces.get(Direction.SOUTH);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, fromX, fromY, toZ, minU, maxV, material);
            parameters.vertex(stack, toX, fromY, toZ, maxU, maxV, material);
            parameters.vertex(stack, toX, toY, toZ, maxU, minV, material);
            parameters.vertex(stack, fromX, toY, toZ, minU, minV, material);
        }
        if (faces.containsKey(Direction.WEST)) {
            var face = faces.get(Direction.WEST);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, fromX, fromY, fromZ, minU, maxV, material);
            parameters.vertex(stack, fromX, fromY, toZ, maxU, maxV, material);
            parameters.vertex(stack, fromX, toY, toZ, maxU, minV, material);
            parameters.vertex(stack, fromX, toY, fromZ, minU, minV, material);
        }

        parameters.vertex(stack, toX, toY, fromZ, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, fromX, toY, fromZ, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, fromX, toY, toZ, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, toX, toY, toZ, 0.0F, 0.0F, Material.getEmpty());

        parameters.vertex(stack, fromX, fromY, fromZ, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, toX, fromY, fromZ, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, toX, fromY, toZ, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, fromX, fromY, toZ, 0.0F, 0.0F, Material.getEmpty());
    }
}
