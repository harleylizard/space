package com.harleylizard.space.graphics.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harleylizard.space.Resources;
import com.harleylizard.space.block.Block;
import com.harleylizard.space.registry.Registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class ModelReader {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Model.class, ModelDeserializer.JSON_DESERIALIZER).create();

    private static final Map<String, Model> MAP = new HashMap<>();

    private ModelReader() {}

    public static Model getModel(Registry<Block> registry, Block block) throws IOException {
        requireNonNull(block, "Block is null");
        var path = "models/%s.json".formatted(registry.get(block));
        if (MAP.containsKey(path)) {
            return MAP.get(path);
        }
        try (var reader = new BufferedReader(new InputStreamReader(Resources.get(path)))) {
            var model = GSON.fromJson(reader, Model.class);
            MAP.put(path, model);
            return model;
        }
    }
}
