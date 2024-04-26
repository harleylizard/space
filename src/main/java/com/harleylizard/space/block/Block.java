package com.harleylizard.space.block;

import static java.util.Objects.requireNonNull;

public final class Block {
    private final Properties properties;

    public Block(Properties properties) {
        this.properties = requireNonNull(properties);
    }

    public Properties getProperties() {
        return properties;
    }
}
