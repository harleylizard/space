package com.harleylizard.space.graphics.light;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Lights implements Iterable<Light> {
    private final List<Light> list = new ArrayList<>();

    public Light add(float r, float g, float b, float range, float intensity) {
        var light = Light.of(r, g, b, range, intensity);
        list.add(light);
        return light;
    }

    public void remove(Light light) {
        list.remove(light);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<Light> iterator() {
        return list.iterator();
    }
}
