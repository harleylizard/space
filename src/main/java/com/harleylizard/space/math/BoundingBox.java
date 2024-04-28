package com.harleylizard.space.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class BoundingBox {
    private final float minX;
    private final float minY;
    private final float minZ;
    private final float maxX;
    private final float maxY;
    private final float maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox move(float x, float y, float z) {
        return new BoundingBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
    }

    public BoundingBox transform(Matrix4f matrix4f) {
        var min = new Vector3f();
        var max = new Vector3f();
        matrix4f.transformAab(minX, minY, minZ, maxX, maxY, maxZ, min, max);
        return new BoundingBox(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public boolean intersects(BoundingBox boundingBox) {
        var x = maxX > boundingBox.minX && minX < boundingBox.maxX;
        var y = maxY > boundingBox.minY && minY < boundingBox.maxY;
        var z = maxZ > boundingBox.minZ && minZ < boundingBox.maxZ;
        return x && y && z;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMaxZ() {
        return maxZ;
    }
}
