package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.texture.Material;

public final class Face {
    private final int texture;
    private final float minU;
    private final float minV;
    private final float maxU;
    private final float maxV;

    private final Material material;

    public Face(int texture, float minU, float minV, float maxU, float maxV, Material material) {
        this.texture = texture;
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
        this.material = material;
    }

    public int getTexture() {
        return texture;
    }

    public float getMinU() {
        return minU;
    }

    public float getMinV() {
        return minV;
    }

    public float getMaxU() {
        return maxU;
    }

    public float getMaxV() {
        return maxV;
    }

    public Material getMaterial() {
        return material;
    }
}
