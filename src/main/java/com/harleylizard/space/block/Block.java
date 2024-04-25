package com.harleylizard.space.block;

import com.harleylizard.space.math.BoundingBox;

public final class Block {
    private final BoundingBox boundingBox;

    public Block(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public static BoundingBox createBlock() {
        return new BoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
