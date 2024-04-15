package com.harleylizard.space.graphics.vertex;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.*;

public final class VaryingVertexParameters implements VertexParameters {
    private static final int AMOUNT = 24 * 4;

    private ByteBuffer buffer = memCalloc(AMOUNT);
    private int vertices;
    private int elements;

    private VaryingVertexParameters() {}

    @Override
    public void vertex(float x, float y, float z, float u, float v) {
        grow(6 * 4);
        buffer.putFloat(x).putFloat(y).putFloat(z).putFloat(1.0F).putFloat(u).putFloat(v);
        vertices += 6 * 4;
    }

    @Override
    public void triangulate() {
        var quads = (vertices / 4) / 24;
        grow(6 * 4 * quads);
        var last = 0;
        for (var i = 0; i < quads; i++) {
            buffer
                    .putInt(0 + last).putInt(1 + last).putInt(2 + last)
                    .putInt(2 + last).putInt(3 + last).putInt(0 + last);
            elements += 6 * 4;
            last += 4;
        }
        buffer.flip();
    }

    @Override
    public ByteBuffer getVertices() {
        buffer.position(0);
        buffer.limit(vertices);
        return buffer;
    }

    @Override
    public ByteBuffer getElements() {
        buffer.position(vertices);
        buffer.limit(vertices + elements);
        return buffer;
    }

    @Override
    public int getCount() {
        return elements / 4;
    }

    @Override
    public void free() {
        memFree(buffer);
    }

    private void grow(int size) {
        if (buffer.remaining() == 0) {
            buffer = memRealloc(buffer, buffer.capacity() + size);
        }
    }

    public static VertexParameters create() {
        return new VaryingVertexParameters();
    }
}
