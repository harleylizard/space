package com.harleylizard.space.graphics;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL45.*;

public final class Quad {
    private final int vao = glCreateVertexArrays();
    private final int vbo = glCreateBuffers();
    private final int ebo = glCreateBuffers();

    {
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 16);
        glVertexArrayAttribBinding(vao, 0, 0);
        glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0);

        glVertexArrayElementBuffer(vao, ebo);

        float[] vertices = {
                -0.5F, -0.5F, 0.0F, 1.0F,
                 0.5F, -0.5F, 0.0F, 1.0F,
                 0.5F,  0.5F, 0.0F, 1.0F,
                -0.5F,  0.5F, 0.0F, 1.0F
        };
        int[] elements = {
                0, 1, 2,
                2, 3, 0
        };

        var flags = 0;
        glNamedBufferStorage(vbo, vertices, flags);
        glNamedBufferStorage(ebo, elements, flags);
    }

    public void draw() {
        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexArrayAttrib(vao, 0);
    }

    public static void unbind() {
        glBindVertexArray(0);
    }
}
