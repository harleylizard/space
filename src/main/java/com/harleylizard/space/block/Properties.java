package com.harleylizard.space.block;

import com.harleylizard.space.math.BoundingBox;

public final class Properties {
    private final BoundingBox boundingBox;
    private final boolean canCollide;

    private Properties(BoundingBox boundingBox, boolean canCollide) {
        this.boundingBox = boundingBox;
        this.canCollide = canCollide;
    }

    private Properties(Properties properties) {
        boundingBox = properties.boundingBox;
        canCollide = properties.canCollide;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean canCollide() {
        return canCollide;
    }

    public static Properties createDefault() {
        return new Properties(createBlock(), true);
    }

    public static Properties copy(Block block) {
        return new Properties(block.getProperties());
    }

    private static BoundingBox createBlock() {
        return new BoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public static final class Builder {
        private BoundingBox boundingBox = createBlock();
        private boolean canCollide = true;

        public Builder setBoundingBox(BoundingBox boundingBox) {
            this.boundingBox = boundingBox;
            return this;
        }

        public Builder canCollide(boolean canCollide) {
            this.canCollide = canCollide;
            return this;
        }

        public Properties build() {
            return new Properties(boundingBox, canCollide);
        }
    }
}
