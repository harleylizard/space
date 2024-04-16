package com.harleylizard.space.graphics.model;

import com.harleylizard.space.graphics.shape.Shape;
import com.harleylizard.space.graphics.vertex.Layer;

import java.util.Iterator;
import java.util.List;

public final class Model implements Iterable<Shape> {
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

    public static Model getEmpty() {
        return EMPTY;
    }

    public static Model of(List<Shape> shapes, Layer layer, int light, boolean ambient) {
        return new Model(shapes, layer, light, ambient);
    }
}
