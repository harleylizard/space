package com.harleylizard.space.graphics.text;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMaps;

public final class English implements Font {
    private static final int WIDTH = 64;
    private static final int HEIGHT = 96;

    private static final FontCoordinates EMPTY = FontCoordinates.of(WIDTH, HEIGHT, 0, 0);

    private final Char2ObjectMap<FontCoordinates> map;

    {
        var map = new Char2ObjectArrayMap<FontCoordinates>();
        map.put('A', FontCoordinates.of(WIDTH, HEIGHT, 0, 0));
        map.put('B', FontCoordinates.of(WIDTH, HEIGHT, 1, 0));
        map.put('C', FontCoordinates.of(WIDTH, HEIGHT, 1, 0));

        map.put('s', FontCoordinates.of(WIDTH, HEIGHT, 2, 6));

        this.map = Char2ObjectMaps.unmodifiable(map);
    }

    @Override
    public FontCoordinates getCoordinates(char c) {
        return map.getOrDefault(c, EMPTY);
    }
}
