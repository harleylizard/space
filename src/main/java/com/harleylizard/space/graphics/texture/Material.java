package com.harleylizard.space.graphics.texture;

public final class Material {
    private static final Material EMPTY = new Material(0, 0);

    private final int texture;
    private final int normal;

    public Material(int texture, int normal) {
        this.texture = texture;
        this.normal = normal;
    }

    public int getTexture() {
        return texture;
    }

    public int getNormal() {
        return normal;
    }

    public static Material getEmpty() {
        return EMPTY;
    }
}
