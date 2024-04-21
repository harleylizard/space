package com.harleylizard.space.light;

import com.harleylizard.space.math.Color;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class LightSdf implements Iterable<Light> {
    private final List<Light> lights = new CopyOnWriteArrayList<>();

    public void add(Light light) {
        lights.add(light);
    }

    public void remove(Light light) {
        lights.remove(light);
    }

    public int getColor(int x, int y, int z) {
        var r = 0.0F;
        var g = 0.0F;
        var b = 0.0F;
        var a = 0.0F;

        for (var light : lights) {
            var v = Math.floor(light.getX());
            var k = Math.floor(light.getY());
            var l = Math.floor(light.getZ());
            var m = light.getR();
            var n = light.getG();
            var o = light.getB();
            var p = light.getA();

            var distance = getDistance(x, y, z, v, k, l, 1.0F);
            var intensity = Math.min(1.0F, (float) (1.0F / Math.pow(distance, 2)));

            r += m * intensity;
            g += n * intensity;
            b += o * intensity;
            a += p * intensity;
        }

        r = Math.min(r, 1.0F);
        g = Math.min(g, 1.0F);
        b = Math.min(b, 1.0F);
        a = Math.min(a, 1.0F);
        return Color.pack(r, g, b, a);
    }

    public void clear() {
        lights.clear();
    }

    public int getSize() {
        return lights.size();
    }

    @Override
    public Iterator<Light> iterator() {
        return lights.iterator();
    }

    private static double getDistance(double x, double y, double z, double nx, double ny, double nz, double r) {
        return Math.sqrt(Math.pow(x - nx, 2) + Math.pow(y - ny, 2) + Math.pow(z - nz, 2)) - r;
    }
}
