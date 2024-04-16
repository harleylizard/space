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
import java.util.*;

public final class Model implements Iterable<Shape> {
    public static final JsonDeserializer<Model> DESERIALIZER = Model::fromJson;

    private static final Model EMPTY = new Model(List.of(), Layer.SOLID, 0, false);

    private final List<Shape> shapes;
    private final Layer layer;
    private final int light;
    private final boolean ambient;

    private Model(List<Shape> shapes, Layer layer, int light, boolean ambient) {
        this.shapes = shapes;
        this.layer = layer;
        this.light = light;
        this.ambient = ambient;
    }

    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    public boolean isAmbient() {
        return ambient;
    }

    public Layer getLayer() {
        return layer;
    }

    public int getLight() {
        return light;
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    private static Model fromJson(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
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
            return EMPTY;
        }
        var shapes = new ArrayList<Shape>(size);

        for (JsonElement element : jsonArray) {
            shapes.add(createShape(lookup, element.getAsJsonObject()));
        }
        return new Model(Collections.unmodifiableList(shapes), layer, light, ambient);
    }

    private static Shape createShape(TextureLookup lookup, JsonObject jsonObject) {
        var type = jsonObject.getAsJsonPrimitive("type").getAsString().toLowerCase();
        switch (type) {
            case "cube" -> {
                var from = jsonObject.getAsJsonArray("from");
                var to = jsonObject.getAsJsonArray("to");
                var faces = createFaces(lookup, jsonObject.getAsJsonObject("face"));
                return new Cube(
                        from.get(0).getAsFloat() / 16.0F,
                        from.get(1).getAsFloat() / 16.0F,
                        from.get(2).getAsFloat() / 16.0F,
                        to.get(0).getAsFloat() / 16.0F,
                        to.get(1).getAsFloat() / 16.0F,
                        to.get(2).getAsFloat() / 16.0F,
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
                var material = lookup.getMaterial(texture);

                map.put(direction, new Face(0,
                        uv.get(0).getAsFloat() / 16.0F,
                        uv.get(1).getAsFloat() / 16.0F,
                        uv.get(2).getAsFloat() / 16.0F,
                        uv.get(3).getAsFloat() / 16.0F, material));
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
                var material = lookup.getMaterial(texture);

                map.put(direction, new Face(0,
                        uv.get(0).getAsFloat() / 16.0F,
                        uv.get(1).getAsFloat() / 16.0F,
                        uv.get(2).getAsFloat() / 16.0F,
                        uv.get(3).getAsFloat() / 16.0F, material));
            }
        }
        return Collections.unmodifiableMap(map);
    }
}
