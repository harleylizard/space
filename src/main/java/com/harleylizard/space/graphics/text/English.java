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
        map.put(' ', FontCoordinates.of(WIDTH, HEIGHT, 2, 3));
        map.put('A', FontCoordinates.of(WIDTH, HEIGHT, 0, 0));
        map.put('B', FontCoordinates.of(WIDTH, HEIGHT, 1, 0));
        map.put('C', FontCoordinates.of(WIDTH, HEIGHT, 2, 0));
        map.put('D', FontCoordinates.of(WIDTH, HEIGHT, 3, 0));
        map.put('E', FontCoordinates.of(WIDTH, HEIGHT, 4, 0));
        map.put('F', FontCoordinates.of(WIDTH, HEIGHT, 5, 0));
        map.put('G', FontCoordinates.of(WIDTH, HEIGHT, 6, 0));
        map.put('H', FontCoordinates.of(WIDTH, HEIGHT, 7, 0));
        map.put('I', FontCoordinates.of(WIDTH, HEIGHT, 0, 1));
        map.put('J', FontCoordinates.of(WIDTH, HEIGHT, 1, 1));
        map.put('K', FontCoordinates.of(WIDTH, HEIGHT, 2, 1));
        map.put('L', FontCoordinates.of(WIDTH, HEIGHT, 3, 1));
        map.put('M', FontCoordinates.of(WIDTH, HEIGHT, 4, 1));
        map.put('N', FontCoordinates.of(WIDTH, HEIGHT, 5, 1));
        map.put('O', FontCoordinates.of(WIDTH, HEIGHT, 6, 1));
        map.put('P', FontCoordinates.of(WIDTH, HEIGHT, 7, 1));
        map.put('Q', FontCoordinates.of(WIDTH, HEIGHT, 0, 2));
        map.put('R', FontCoordinates.of(WIDTH, HEIGHT, 1, 2));
        map.put('S', FontCoordinates.of(WIDTH, HEIGHT, 2, 2));
        map.put('T', FontCoordinates.of(WIDTH, HEIGHT, 3, 2));
        map.put('U', FontCoordinates.of(WIDTH, HEIGHT, 4, 2));
        map.put('V', FontCoordinates.of(WIDTH, HEIGHT, 5, 2));
        map.put('W', FontCoordinates.of(WIDTH, HEIGHT, 6, 2));
        map.put('X', FontCoordinates.of(WIDTH, HEIGHT, 7, 2));
        map.put('Y', FontCoordinates.of(WIDTH, HEIGHT, 0, 3));
        map.put('Z', FontCoordinates.of(WIDTH, HEIGHT, 1, 3));

        map.put('s', FontCoordinates.of(WIDTH, HEIGHT, 2, 6));

        map.put('0', FontCoordinates.of(WIDTH, HEIGHT, 0, 8));
        map.put('1', FontCoordinates.of(WIDTH, HEIGHT, 1, 8));
        map.put('2', FontCoordinates.of(WIDTH, HEIGHT, 2, 8));
        map.put('3', FontCoordinates.of(WIDTH, HEIGHT, 3, 8));
        map.put('4', FontCoordinates.of(WIDTH, HEIGHT, 4, 8));
        map.put('5', FontCoordinates.of(WIDTH, HEIGHT, 5, 8));
        map.put('6', FontCoordinates.of(WIDTH, HEIGHT, 6, 8));
        map.put('7', FontCoordinates.of(WIDTH, HEIGHT, 7, 8));
        map.put('8', FontCoordinates.of(WIDTH, HEIGHT, 0, 9));
        map.put('9', FontCoordinates.of(WIDTH, HEIGHT, 1, 9));

        this.map = Char2ObjectMaps.unmodifiable(map);
    }

    @Override
    public FontCoordinates getCoordinates(char c) {
        return map.getOrDefault(c, EMPTY);
    }
}
