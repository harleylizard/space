package com.harleylizard.space.graphics.shape;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public sealed interface Shape permits Cube, Plane {

    void build(Builder builder);

    final class Builder {
        private final ByteBuffer buffer;

        private Builder(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        void vertex(float x, float y, float z) {

        }

        public void free() {
            memFree(buffer);
        }

        public static Builder of(int size) {
            return new Builder(memCalloc(size));
        }
    }
}
