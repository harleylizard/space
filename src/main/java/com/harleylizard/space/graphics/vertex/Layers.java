package com.harleylizard.space.graphics.vertex;

import java.util.EnumMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL45.*;

public final class Layers {
    private final Map<Layer, VertexBuffer> map = new EnumMap<>(Layer.class);
    private final Map<Layer, VertexParameters> parameters = new EnumMap<>(Layer.class);

    public void upload() {
        for (var entry : parameters.entrySet()) {
            var buffer = map.computeIfAbsent(entry.getKey(), layer -> new VertexBuffer());
            buffer.upload(entry.getValue());
        }
        parameters.clear();
    }

    public void draw() {
        for (var entry : map.entrySet()) {
            entry.getValue().draw();
        }
    }

    public VertexParameters getVertexParameter(Layer layer) {
        return parameters.computeIfAbsent(layer, Layers::create);
    }

    private static VertexParameters create(Layer layer) {
        return VaryingVertexParameters.create();
    }

    private static final class VertexBuffer {
        private final int vao = glCreateVertexArrays();
        private final int vbo = glCreateBuffers();
        private final int ebo = glCreateBuffers();

        private int count;

        private VertexBuffer() {
            glVertexArrayVertexBuffer(vao, 0, vbo, 0, VaryingVertexParameters.VERTEX_SIZE);
            glVertexArrayAttribBinding(vao, 0, 0);
            glVertexArrayAttribBinding(vao, 1, 0);
            glVertexArrayAttribBinding(vao, 2, 0);
            glVertexArrayAttribBinding(vao, 3, 0);
            glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0);
            glVertexArrayAttribFormat(vao, 1, 3, GL_FLOAT, false, 16);
            glVertexArrayAttribFormat(vao, 2, 3, GL_FLOAT, false, 28);
            glVertexArrayAttribFormat(vao, 3, 4, GL_FLOAT, false, 40);

            glVertexArrayElementBuffer(vao, ebo);
        }

        private void upload(VertexParameters parameters) {
            parameters.triangulate();

            count = parameters.getCount();

            var usage = GL_DYNAMIC_DRAW;
            glNamedBufferData(vbo, parameters.getVerticesInBytes(), usage);
            glNamedBufferData(ebo, parameters.getElementsInBytes(), usage);

            var mapped = glMapNamedBuffer(vbo, GL_WRITE_ONLY);
            if (mapped != null) {
                mapped.put(parameters.getVertices());
            }
            glUnmapNamedBuffer(vbo);
            mapped = glMapNamedBuffer(ebo, GL_WRITE_ONLY);
            if (mapped != null) {
                mapped.put(parameters.getElements());
            }
            glUnmapNamedBuffer(ebo);
            parameters.free();
        }

        private void draw() {
            glBindVertexArray(vao);

            glEnableVertexArrayAttrib(vao, 0);
            glEnableVertexArrayAttrib(vao, 1);
            glEnableVertexArrayAttrib(vao, 2);
            glEnableVertexArrayAttrib(vao, 3);

            glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);

            glDisableVertexArrayAttrib(vao, 0);
            glDisableVertexArrayAttrib(vao, 1);
            glDisableVertexArrayAttrib(vao, 2);
            glDisableVertexArrayAttrib(vao, 3);

            glBindVertexArray(0);
        }
    }
}
