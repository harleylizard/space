package com.harleylizard.space.graphics.vertex;

import com.harleylizard.space.math.ImmutableVector3i;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.*;

public final class VaryingVertexParameters implements VertexParameters {
    public static final int VERTEX_SIZE = 14 * 4;

    private static final int AMOUNT = VERTEX_SIZE * 4;

    private ByteBuffer buffer = memCalloc(AMOUNT);
    private int vertices;
    private int elements;

    private VaryingVertexParameters() {}

    @Override
    public void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, int t, float nx, float ny, float nz, int i) {
        var vec4f = matrix4f.transform(x, y, z, 1.0F, new Vector4f());

        var r = (float) ((i >> 16) & 0xFF) / 255.0F;
        var g = (float) ((i >> 8) & 0xFF) / 255.0F;
        var b = (float) ((i >> 0) & 0xFF) / 255.0F;
        var a = 1.0F;
        grow(VERTEX_SIZE);
        buffer
                .putFloat(vec4f.x).putFloat(vec4f.y).putFloat(vec4f.z).putFloat(1.0F)
                .putFloat(u).putFloat(v).putFloat(t)
                .putFloat(nx).putFloat(ny).putFloat(nz)
                .putFloat(r).putFloat(g).putFloat(b).putFloat(a);
        vertices += VERTEX_SIZE;
    }

    @Override
    public void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, int t, ImmutableVector3i normal, int i) {
        vertex(matrix4f, x, y, z, u, v, t, normal.getX(), normal.getY(), normal.getZ(), i);
    }

    @Override
    public void triangulate() {
        var quads = (vertices / 4) / VERTEX_SIZE;
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
