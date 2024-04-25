package com.harleylizard.space.math;

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

    public boolean intersects(BoundingBox boundingBox) {
        var x = maxX >= boundingBox.minX && minX <= boundingBox.maxX;
        var y = maxY >= boundingBox.minY && minY <= boundingBox.maxY;
        var z = maxZ >= boundingBox.minZ && minZ <= boundingBox.maxZ;
        return x && y && z;
    }
}
