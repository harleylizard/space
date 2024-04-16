package com.harleylizard.space.graphics.texture;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.*;

public final class TextureManager {
    private static final TextureManager INSTANCE = new TextureManager();

    private final List<TexturePath> paths = new ArrayList<>();
    private final Object2IntMap<TexturePath> map = new Object2IntArrayMap<>();

    private TextureManager() {}

    public int getTexture(TexturePath path) {
        if (map.containsKey(path)) {
            return map.get(path);
        }
        if (!paths.contains(path)) {
            paths.add(path);
        }
        var indexOf = 3 * paths.indexOf(path);

        map.put(path, indexOf);
        return indexOf;
    }

    public List<TexturePath> getPaths() {
        return Collections.unmodifiableList(paths);
    }

    public static TextureManager getInstance() {
        return INSTANCE;
    }
}
