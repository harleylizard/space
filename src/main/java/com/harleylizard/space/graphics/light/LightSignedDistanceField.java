package com.harleylizard.space.graphics.light;

import com.harleylizard.space.math.Color;

public final class LightSignedDistanceField {
    private final int[] field;

    private LightSignedDistanceField(int[] field) {
        this.field = field;
    }

    public int getLight(int x, int y, int z) {
        return field[toIndex(x, y, z)];
    }

    public static LightSignedDistanceField createFrom(Lights lights) {
        var field = new int[32 * 32 * 32];

        for (var x = 0; x < 32; x++) for (var y = 0; y < 32; y++) for (var z = 0; z < 32; z++) {
            var r = 0.0F;
            var g = 0.0F;
            var b = 0.0F;
            var a = 0.0F;

            for (var light : lights) {
                var lx = Math.floor(light.getX());
                var ly = Math.floor(light.getY());
                var lz = Math.floor(light.getZ());
                var lr = light.getR();
                var lg = light.getG();
                var lb = light.getB();
                var la = light.getA();

                var distance = getDistance(x, y, z, lx, ly, lz, 1.0F);
                var intensity = Math.min(1.0F, (float) (1.0F / Math.pow(distance, 2)));

                r += lr * intensity;
                g += lg * intensity;
                b += lb * intensity;
                a += la * intensity;
            }

            r = Math.min(r, 0.75F);
            g = Math.min(g, 0.75F);
            b = Math.min(b, 0.75F);
            a = Math.min(a, 0.75F);
            field[toIndex(x, y, z)] = Color.pack(r, g, b, a);
        }
        return new LightSignedDistanceField(field);
    }

    private static double getDistance(double x, double y, double z, double nx, double ny, double nz, double r) {
        return Math.sqrt(Math.pow(x - nx, 2) + Math.pow(y - ny, 2) + Math.pow(z - nz, 2)) - r;
    }

    private static int toIndex(int x, int y, int z) {
        return (z * 32 * 32) + (y * 32) + x;
    }
}
