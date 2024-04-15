package com.harleylizard.space.graphics.texture;

import java.util.*;

public final class MaterialManager {
    private static final MaterialManager INSTANCE = new MaterialManager();

    private final List<String> paths = new ArrayList<>();
    private final Map<String, Material> map = new HashMap<>();

    private MaterialManager() {}

    public Material getMaterial(String path) {
        if (map.containsKey(path)) {
            return map.get(path);
        }
        if (!paths.contains(path)) {
            paths.add(path);
        }
        var indexOf = 2 * paths.indexOf(path);

        var material = new Material(indexOf, indexOf + 1);
        map.put(path, material);
        return material;
    }

    public List<String> getPaths() {
        return Collections.unmodifiableList(paths);
    }

    public static MaterialManager getInstance() {
        return INSTANCE;
    }
}
