package com.harleylizard.space.graphics.texture;

public final class Material {
    private static final Material EMPTY = new Material(0, 0);

    private final int texture;
    private final int normal;

    public Material(int texture, int normal) {
        this.texture = texture;
        this.normal = normal;
    }

    public static Material empty() {
        return EMPTY;
    }
}
