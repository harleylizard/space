package com.harleylizard.space.graphics.texture;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TextureLookup {
    private final Map<String, TexturePath> map;

    private TextureLookup(Map<String, TexturePath> map) {
        this.map = map;
    }

    public TexturePath getPath(String name) {
        var result = map.get(name);
        var color = result.getColor();
        if (map.containsKey(color)) {
            return map.get(color);
        }
        return result;
    }

    public Material getMaterial(String name) {
        return !map.containsKey(name) ? Material.getEmpty() : MaterialManager.getInstance().getMaterial(getPath(name));
    }

    public static TextureLookup fromJson(JsonObject jsonObject) {
        var textures = jsonObject.getAsJsonObject("textures");
        var map = new HashMap<String, TexturePath>(textures.size());

        for (var entry : textures.entrySet()) {
            TexturePath path;
            var value = entry.getValue();
            if (value.isJsonObject()) {
                var jsonObject1 = value.getAsJsonObject();

                var color = jsonObject1.getAsJsonPrimitive("color").getAsString();
                var emissive = jsonObject1.getAsJsonPrimitive("emissive").getAsString();
                path = new TexturePath(color, emissive);
            } else {
                path = TexturePath.justColor(value.getAsString());
            }
            map.put(entry.getKey(), path);
        }
        return new TextureLookup(Collections.unmodifiableMap(map));
    }
}
