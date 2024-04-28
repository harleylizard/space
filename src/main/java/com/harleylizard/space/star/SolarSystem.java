package com.harleylizard.space.star;

import java.util.List;

public final class SolarSystem {
    private final List<PlanetaryBody> bodies;
    private final int size;

    private SolarSystem(List<PlanetaryBody> bodies, int size) {
        this.bodies = bodies;
        this.size = size;
    }

    public static SolarSystem fromSeed(long seed) {
        return null;
    }
}
