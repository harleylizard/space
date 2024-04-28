package com.harleylizard.space.star;

import java.util.Random;

public final class StarMap {
    private final long seed;

    private final Random random;

    public StarMap(long seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public long getSeed() {
        return seed;
    }

    public Random getRandom() {
        return random;
    }
}
