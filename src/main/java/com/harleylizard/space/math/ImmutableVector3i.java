package com.harleylizard.space.math;

public final class ImmutableVector3i {
    private final int x;
    private final int y;
    private final int z;

    public ImmutableVector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
