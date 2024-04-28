package com.harleylizard.space.graphics.star;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Shader;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class SolarSystemGraphics {
    private final ProgramPipeline pipeline = new ProgramPipeline.Builder()
            .useProgram(Shader.FRAGMENT, "shaders/solar_system_fragment.glsl")
            .useProgram(Shader.VERTEX, "shaders/solar_system_vertex.glsl")
            .useBuffer(Shader.VERTEX, "bufferStorage", 2)
            .build();

    private final int ssbo = pipeline.getBuffer("bufferStorage");

    private final Matrix4f matrix4f = new Matrix4f();

    private final int vao = glCreateVertexArrays();
    private final int vbo = glCreateBuffers();
    private final int ebo = glCreateBuffers();

    {
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 16);
        glVertexArrayAttribBinding(vao, 0, 0);
        glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0);

        glVertexArrayElementBuffer(vao, ebo);

        float[] vertices = {
                -0.5F, -0.5F, 0.5F, 1.0F,
                 0.5F, -0.5F, 0.5F, 1.0F,
                 0.5F,  0.5F, 0.5F, 1.0F,
                -0.5F,  0.5F, 0.5F, 1.0F,

                 0.5F, -0.5F, -0.5F, 1.0F,
                -0.5F, -0.5F, -0.5F, 1.0F,
                -0.5F,  0.5F, -0.5F, 1.0F,
                 0.5F,  0.5F, -0.5F, 1.0F,

                 0.5F,  0.5F, -0.5F, 1.0F,
                -0.5F,  0.5F, -0.5F, 1.0F,
                -0.5F,  0.5F,  0.5F, 1.0F,
                 0.5F,  0.5F,  0.5F, 1.0F
        };
        int[] elements = {
                0, 1, 2,
                2, 3, 0,
                4, 5, 6,
                6, 7, 4,
                8, 9, 10,
                10, 11, 8
        };
        var flags = 0;
        glNamedBufferStorage(vbo, vertices, flags);
        glNamedBufferStorage(ebo, elements, flags);
    }

    public void draw(float animation, long seed) {
        var size = getPlanets(seed);

        uploadPlanets(animation, seed, size);

        pipeline.bind();

        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        glDrawElementsInstanced(GL_TRIANGLES, 6 * 3, GL_UNSIGNED_INT, 0, size);

        glDisableVertexArrayAttrib(vao, 0);

        ProgramPipeline.unbind();
    }

    public void uploadPlanets(float animation, long seed, int size) {
        var buffer = memCalloc((16 * 4) * size);

        for (var i = 0; i < size; i++) {
            var planetSeed = (((seed + i) << 2) >> 1);

            var rotationSpeed = 1.0F - (float) i / size;
            var rotationOffset = Math.abs((float) (planetSeed >> 2 & 5));

            matrix4f.identity();
            matrix4f.translate(15.0F, 10.0F, 15.0F);
            if (i > 0) {
                matrix4f.rotate((animation * rotationSpeed) / rotationOffset, 0.0F, 1.0F, 0.0F);
            }
            matrix4f.translate(0, 0.0F, 2.5F * i);
            matrix4f.translate(0.5F, 0.5F, 0.5F);
            if (i == 0) {
                matrix4f.scale(1.5F, 1.5F, 1.5F);
            }
            matrix4f.rotate(animation + (planetSeed & 4), 0.0F, 1.0F, 0.0F);


            var offset = (16 * 4) * i;
            matrix4f.get(offset, buffer);
        }

        glNamedBufferData(ssbo, buffer, GL_STREAM_DRAW);
        memFree(buffer);
    }

    public int getPlanets(long seed) {
        return (int) (seed >> 4 & 10) + 1;
    }
}
