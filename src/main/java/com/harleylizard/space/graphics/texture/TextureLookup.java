package com.harleylizard.space.graphics.texture;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TextureLookup {
    private final Map<String, String> map;

    private TextureLookup(Map<String, String> map) {
        this.map = map;
    }

    public String getPath(String name) {
        var result = map.get(name);
        if (map.containsKey(result)) {
            return getPath(result);
        }
        return result;
    }

    public Material getMaterial(String name) {
        var path = getPath(name);

        return Material.empty();
    }

    public static TextureLookup fromJson(JsonObject jsonObject) {
        var textures = jsonObject.getAsJsonObject("textures");
        var map = new HashMap<String, String>(textures.size());

        for (var entry : textures.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }
        return new TextureLookup(Collections.unmodifiableMap(map));
    }
}
