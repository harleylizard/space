package com.harleylizard.space.graphics.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.harleylizard.space.graphics.shape.Cube;
import com.harleylizard.space.graphics.shape.Face;
import com.harleylizard.space.graphics.shape.Plane;
import com.harleylizard.space.graphics.shape.Shape;
import com.harleylizard.space.graphics.texture.TextureLookup;
import com.harleylizard.space.graphics.vertex.Layer;
import com.harleylizard.space.math.Color;
import com.harleylizard.space.math.Direction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ModelDeserializer {
    public static final JsonDeserializer<Model> JSON_DESERIALIZER = ModelDeserializer::deserialiseJson;

    private ModelDeserializer() {}

    private static Model deserialiseJson(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        var jsonObject = jsonElement.getAsJsonObject();

        var lookup = TextureLookup.fromJson(jsonObject);

        var layer = Layer.SOLID;
        if (jsonObject.has("layer")) {
            layer = Layer.fromString(jsonObject.getAsJsonPrimitive("layer").getAsString());
        }

        var light = 0;
        if (jsonObject.has("light")) {
            var lightArray = jsonObject.getAsJsonArray("light");
            var r = lightArray.get(0).getAsFloat();
            var g = lightArray.get(1).getAsFloat();
            var b = lightArray.get(2).getAsFloat();
            var a = lightArray.get(3).getAsFloat();
            light = Color.pack(r, g, b, a);
        }

        var ambient = false;
        if (jsonObject.has("ambient")) {
            ambient = jsonObject.getAsJsonPrimitive("ambient").getAsBoolean();
        }

        var jsonArray = jsonObject.getAsJsonArray("shapes");
        var size = jsonArray.size();
        if (size == 0) {
            return Model.getEmpty();
        }
        var shapes = new ArrayList<Shape>(size);

        for (JsonElement element : jsonArray) {
            shapes.add(createShape(lookup, element.getAsJsonObject()));
        }
        return Model.of(Collections.unmodifiableList(shapes), layer, light, ambient);
    }

    private static Shape createShape(TextureLookup lookup, JsonObject jsonObject) {
        var type = jsonObject.getAsJsonPrimitive("type").getAsString().toLowerCase();
        switch (type) {
            case "cube" -> {
                var from = jsonObject.getAsJsonArray("from");
                var to = jsonObject.getAsJsonArray("to");
                var faces = createFaces(lookup, jsonObject.getAsJsonObject("face"));

                var pivotX = 0.0F;
                var pivotY = 0.0F;
                var pivotZ = 0.0F;
                var rotationX = 0.0F;
                var rotationY = 0.0F;
                var rotationZ = 0.0F;
                if (jsonObject.has("rotation")) {
                    var rotation = jsonObject.getAsJsonObject("rotation");
                    var pivot = rotation.getAsJsonArray("pivot");
                    pivotX = pivot.get(0).getAsFloat() / 16.0F;
                    pivotY = pivot.get(1).getAsFloat() / 16.0F;
                    pivotZ = pivot.get(2).getAsFloat() / 16.0F;

                    var angle = rotation.getAsJsonArray("angle");
                    rotationX = (float) Math.toRadians(angle.get(0).getAsFloat());
                    rotationY = (float) Math.toRadians(angle.get(1).getAsFloat());
                    rotationZ = (float) Math.toRadians(angle.get(2).getAsFloat());
                }

                return new Cube(
                        from.get(0).getAsFloat() / 16.0F,
                        from.get(1).getAsFloat() / 16.0F,
                        from.get(2).getAsFloat() / 16.0F,
                        to.get(0).getAsFloat() / 16.0F,
                        to.get(1).getAsFloat() / 16.0F,
                        to.get(2).getAsFloat() / 16.0F,
                        pivotX, pivotY, pivotZ,
                        rotationX, rotationY, rotationZ,
                        faces
                );
            }
            case "plane" -> {
                var from = jsonObject.getAsJsonArray("from");
                var to = jsonObject.getAsJsonArray("to");

                var x = 0.0F;
                var y = 0.0F;
                var z = 0.0F;
                if (jsonObject.has("rotation")) {
                    var rotation = jsonObject.getAsJsonArray("rotation");
                    x = (float) Math.toRadians(rotation.get(0).getAsFloat());
                    y = (float) Math.toRadians(rotation.get(1).getAsFloat());
                    z = (float) Math.toRadians(rotation.get(2).getAsFloat());
                }

                var faces = createPlaneFaces(lookup, jsonObject.getAsJsonObject("face"));
                return new Plane(
                        from.get(0).getAsFloat() / 16.0F,
                        from.get(1).getAsFloat() / 16.0F,
                        to.get(0).getAsFloat() / 16.0F,
                        to.get(1).getAsFloat() / 16.0F,
                        x, y, z,
                        faces
                );
            }
            default -> throw new RuntimeException("Unknown shape %s".formatted(type));
        }
    }

    private static Map<Direction, Face> createFaces(TextureLookup lookup, JsonObject jsonObject) {
        var map = new EnumMap<Direction, Face>(Direction.class);

        for (var direction : Direction.values()) {
            var name = direction.stringify();
            if (jsonObject.has(name)) {
                var jsonObject1 = jsonObject.getAsJsonObject(name);
                var uv = jsonObject1.getAsJsonArray("uv");

                var texture = jsonObject1.getAsJsonPrimitive("texture").getAsString();
                var t = lookup.getTexture(texture);

                map.put(direction, new Face(0,
                        uv.get(0).getAsFloat() / 16.0F,
                        uv.get(1).getAsFloat() / 16.0F,
                        uv.get(2).getAsFloat() / 16.0F,
                        uv.get(3).getAsFloat() / 16.0F, t));
            }
        }
        return Collections.unmodifiableMap(map);
    }

    private static Map<Plane.Direction, Face> createPlaneFaces(TextureLookup lookup, JsonObject jsonObject) {
        var map = new EnumMap<Plane.Direction, Face>(Plane.Direction.class);

        for (var direction : Plane.Direction.values()) {
            var name = direction.getName();
            if (jsonObject.has(name)) {
                var jsonObject1 = jsonObject.getAsJsonObject(name);
                var uv = jsonObject1.getAsJsonArray("uv");

                var texture = jsonObject1.getAsJsonPrimitive("texture").getAsString();
                var t = lookup.getTexture(texture);

                map.put(direction, new Face(0,
                        uv.get(0).getAsFloat() / 16.0F,
                        uv.get(1).getAsFloat() / 16.0F,
                        uv.get(2).getAsFloat() / 16.0F,
                        uv.get(3).getAsFloat() / 16.0F, t));
            }
        }
        return Collections.unmodifiableMap(map);
    }
}
