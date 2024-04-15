package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.texture.Material;
import com.harleylizard.space.graphics.vertex.VertexParameters;
import com.harleylizard.space.math.Direction;
import org.joml.Matrix4fStack;

import java.util.Map;

public final class Plane implements Shape {
    private final float fromX;
    private final float fromY;
    private final float toX;
    private final float toY;

    private final Map<Direction, Face> faces;

    public Plane(float fromX, float fromY, float toX, float toY, Map<Direction, Face> faces) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.faces = faces;
    }

    @Override
    public void build(VertexParameters parameters, Matrix4fStack stack) {
        if (faces.containsKey(Direction.FRONT)) {
            var face = faces.get(Direction.FRONT);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, fromX, fromY, 0.0F, minU, maxV, material, 0, 0, -1);
            parameters.vertex(stack, toX, fromY, 0.0F, maxU, maxV, material, 0, 0, -1);
            parameters.vertex(stack, toX, toY, 0.0F, maxU, minV, material, 0, 0, -1);
            parameters.vertex(stack, fromX, toY, 0.0F, minU, minV, material, 0, 0, -1);
        }
        if (faces.containsKey(Direction.BACK)) {
            var face = faces.get(Direction.BACK);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var material = face.getMaterial();
            parameters.vertex(stack, toX, fromY, 0.0F, minU, maxV, material, 0, 0, 1);
            parameters.vertex(stack, fromX, fromY, 0.0F, maxU, maxV, material, 0, 0, 1);
            parameters.vertex(stack, fromX, toY, 0.0F, maxU, minV, material, 0, 0, 1);
            parameters.vertex(stack, toX, toY, 0.0F, minU, minV, material, 0, 0, 1);
        }
    }

    public enum Direction {
        FRONT("front"),
        BACK("back");

        private final String name;

        Direction(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
