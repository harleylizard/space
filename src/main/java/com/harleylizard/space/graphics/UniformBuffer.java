package com.harleylizard.space.graphics;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL44.GL_DYNAMIC_STORAGE_BIT;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.opengl.GL45.glUnmapNamedBuffer;

public final class UniformBuffer {
    private static final int UBO = glCreateBuffers();

    static {
        glBindBufferBase(GL_UNIFORM_BUFFER, 0, UBO);

        var flags = GL_DYNAMIC_STORAGE_BIT | GL_MAP_READ_BIT | GL_MAP_WRITE_BIT;
        glNamedBufferStorage(UBO, 16 * 4 * 3, flags);
    }

    private UniformBuffer() {}

    public static void uploadMatrices(Matrix4f projection, Matrix4f view, Matrix4f model) {
        var mapped = glMapNamedBuffer(UBO, GL_READ_WRITE);
        if (mapped != null) {
            projection.get(0, mapped);
            view.get(16 * 4, mapped);
            model.get(32 * 4, mapped);
        }
        glUnmapNamedBuffer(UBO);
    }
}
