package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.texture.Material;
import com.harleylizard.space.graphics.vertex.VertexParameters;
import org.joml.Matrix4fStack;

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
    public void build(VertexParameters parameters, Matrix4fStack stack) {
        parameters.vertex(stack, fromX, fromY, 0.0F, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, toX, fromY, 0.0F, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, toX, toY, 0.0F, 0.0F, 0.0F, Material.getEmpty());
        parameters.vertex(stack, fromX, toY, 0.0F, 0.0F, 0.0F, Material.getEmpty());
    }
}
