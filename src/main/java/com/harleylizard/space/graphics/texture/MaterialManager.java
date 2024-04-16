package com.harleylizard.space.graphics.texture;

import java.util.*;

public final class MaterialManager {
    private static final MaterialManager INSTANCE = new MaterialManager();

    private final List<TexturePath> paths = new ArrayList<>();
    private final Map<TexturePath, Material> map = new HashMap<>();

    private MaterialManager() {}

    public Material getMaterial(TexturePath path) {
        if (map.containsKey(path)) {
            return map.get(path);
        }
        if (!paths.contains(path)) {
            paths.add(path);
        }
        var indexOf = 3 * paths.indexOf(path);

        var material = new Material(indexOf, indexOf + 1, indexOf + 2);
        map.put(path, material);
        return material;
    }

    public List<TexturePath> getPaths() {
        return Collections.unmodifiableList(paths);
    }

    public static MaterialManager getInstance() {
        return INSTANCE;
    }
}
