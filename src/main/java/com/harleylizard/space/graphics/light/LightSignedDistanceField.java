package com.harleylizard.space.graphics.light;

import com.harleylizard.space.math.Color;

public final class LightSignedDistanceField {

    private LightSignedDistanceField() {
    }

    public int getLight(Lights lights, int x, int y, int z) {
        var r = 0.0F;
        var g = 0.0F;
        var b = 0.0F;
        var a = 0.0F;

        for (var light : lights) {
            var j = Math.floor(light.getX());
            var k = Math.floor(light.getY());
            var l = Math.floor(light.getZ());
            var m = light.getR();
            var n = light.getG();
            var o = light.getB();
            var p = light.getA();

            var distance = getDistance(x, y, z, j, k, l, 1.0F);
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

    public static LightSignedDistanceField createFrom(Lights lights) {
        return new LightSignedDistanceField();
    }

    private static double getDistance(double x, double y, double z, double nx, double ny, double nz, double r) {
        return Math.sqrt(Math.pow(x - nx, 2) + Math.pow(y - ny, 2) + Math.pow(z - nz, 2)) - r;
    }

    private static int toIndex(int x, int y, int z) {
        return (z * 32 * 32) + (y * 32) + x;
    }
}
