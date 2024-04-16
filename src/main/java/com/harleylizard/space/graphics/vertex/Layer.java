package com.harleylizard.space.graphics.vertex;

public enum Layer {
    SOLID,
    TRANSPARENT;

    public static Layer fromString(String name) {
        return switch (name) {
            case "solid" -> SOLID;
            case "transparent" -> TRANSPARENT;
            default -> null;
        };
    }
}
