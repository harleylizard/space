package com.harleylizard.space.graphics.texture;

public final class Material {
    private static final Material EMPTY = new Material(0, 0, 0);

    private final int texture;
    private final int normal;
    private final int emissive;

    public Material(int texture, int normal, int emissive) {
        this.texture = texture;
        this.normal = normal;
        this.emissive = emissive;
    }

    public int getTexture() {
        return texture;
    }

    public int getNormal() {
        return normal;
    }

    public int getEmissive() {
        return emissive;
    }

    public static Material getEmpty() {
        return EMPTY;
    }
}
