package com.harleylizard.space.graphics.texture;

public final class TexturePath {
    private static final String SHOULD_IGNORE = "";

    private final String color;
    private final String emissive;

    public TexturePath(String color, String emissive) {
        this.color = color;
        this.emissive = emissive;
    }

    public String getColor() {
        return color;
    }

    public String getEmissive() {
        return emissive;
    }

    public boolean hasEmissive() {
        return !emissive.isBlank();
    }

    public static TexturePath justColor(String color) {
        return new TexturePath(color, SHOULD_IGNORE);
    }
}
