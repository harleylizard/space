package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.CullGetter;
import com.harleylizard.space.graphics.vertex.VertexParameters;
import com.harleylizard.space.math.ImmutableVector3i;
import org.joml.Matrix4fStack;

import java.util.Map;

public final class Plane implements Shape {
    private final float fromX;
    private final float fromY;
    private final float toX;
    private final float toY;
    private final float x;
    private final float y;
    private final float z;

    private final Map<Direction, Face> faces;

    public Plane(float fromX, float fromY, float toX, float toY, float x, float y, float z, Map<Direction, Face> faces) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.x = x;
        this.y = y;
        this.z = z;
        this.faces = faces;
    }

    @Override
    public void build(CullGetter cullGetter, VertexParameters parameters, Matrix4fStack stack, int x, int y, int z, boolean ambient, int i) {
        stack.pushMatrix();
        stack.translate(0.5F, 0.5F, 0.5F);
        stack.rotate(this.x, 1.0F, 0.0F, 0.0F);
        stack.rotate(this.y, 0.0F, 1.0F, 0.0F);
        stack.rotate(this.z, 0.0F, 0.0F, 1.0F);
        stack.translate(-0.5F, -0.5F, -0.5F);

        if (faces.containsKey(Direction.FRONT)) {
            var face = faces.get(Direction.FRONT);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();

            var normal = ambient ? new ImmutableVector3i(0, 0, 0) : new ImmutableVector3i(0, 0, -1);
            parameters.vertex(stack, fromX, fromY, 0.5F, minU, maxV, t, normal, i);
            parameters.vertex(stack, toX, fromY, 0.5F, maxU, maxV, t, normal, i);
            parameters.vertex(stack, toX, toY, 0.5F, maxU, minV, t, normal, i);
            parameters.vertex(stack, fromX, toY, 0.5F, minU, minV, t, normal, i);
        }
        if (faces.containsKey(Direction.BACK)) {
            var face = faces.get(Direction.BACK);
            var minU = face.getMinU();
            var minV = face.getMinV();
            var maxU = face.getMaxU();
            var maxV = face.getMaxV();
            var t = face.getT();

            var normal = ambient ? new ImmutableVector3i(0, 0, 0) : new ImmutableVector3i(0, 0, 1);
            parameters.vertex(stack, toX, fromY, 0.5F, minU, maxV, t, normal, i);
            parameters.vertex(stack, fromX, fromY, 0.5F, maxU, maxV, t, normal, i);
            parameters.vertex(stack, fromX, toY, 0.5F, maxU, minV, t, normal, i);
            parameters.vertex(stack, toX, toY, 0.5F, minU, minV, t, normal, i);
        }
        stack.popMatrix();
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
