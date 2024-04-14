package com.harleylizard.space.graphics.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.harleylizard.space.graphics.shape.Cube;
import com.harleylizard.space.graphics.shape.Plane;
import com.harleylizard.space.graphics.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Model implements Iterable<Shape> {
    private static final Model EMPTY = new Model(List.of());

    private final List<Shape> shapes;

    private Model(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    public static Model fromJson(JsonElement jsonElement) {
        var jsonObject = jsonElement.getAsJsonObject();

        var jsonArray = jsonObject.getAsJsonArray("shapes");
        var size = jsonArray.size();
        if (size == 0) {
            return EMPTY;
        }
        var shapes = new ArrayList<Shape>(size);

        for (JsonElement element : jsonArray) {
            shapes.add(createShape(element.getAsJsonObject()));
        }
        return new Model(Collections.unmodifiableList(shapes));
    }

    private static Shape createShape(JsonObject jsonObject) {
        var type = jsonObject.getAsJsonPrimitive("type").getAsString().toLowerCase();
        switch (type) {
            case "cube" -> {
                var from = jsonObject.getAsJsonArray("from");
                var to = jsonObject.getAsJsonArray("to");
                return new Cube(
                        from.get(0).getAsFloat() / 16.0F,
                        from.get(1).getAsFloat() / 16.0F,
                        from.get(2).getAsFloat() / 16.0F,
                        to.get(0).getAsFloat() / 16.0F,
                        to.get(1).getAsFloat() / 16.0F,
                        to.get(2).getAsFloat() / 16.0F
                );
            }
            case "plane" -> {
                var from = jsonObject.getAsJsonArray("from");
                var to = jsonObject.getAsJsonArray("to");
                return new Plane(
                        from.get(0).getAsFloat() / 16.0F,
                        from.get(1).getAsFloat() / 16.0F,
                        to.get(0).getAsFloat() / 16.0F,
                        to.get(1).getAsFloat() / 16.0F
                );
            }
            default -> throw new RuntimeException("Unknown shape %s".formatted(type));
        }
    }
}
