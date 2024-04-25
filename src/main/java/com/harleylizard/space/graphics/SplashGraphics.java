package com.harleylizard.space.graphics;

import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL45.*;

public final class SplashGraphics {
    private final ProgramPipeline pipeline = new ProgramPipeline.Builder()
            .useProgram(Shader.FRAGMENT, "shaders/splash_fragment.glsl")
            .useProgram(Shader.VERTEX, "shaders/splash_vertex.glsl")
            .build();

    private final int fragment = pipeline.getProgram(Shader.FRAGMENT);
    private final int location = glGetUniformLocation(fragment, "sampler");

    private final int vao = glCreateVertexArrays();

    private boolean ready;
    private int steps;
    private int time;

    {
        var vbo = glCreateBuffers();
        var ebo = glCreateBuffers();

        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 24);
        glVertexArrayAttribBinding(vao, 0, 0);
        glVertexArrayAttribBinding(vao, 1, 0);
        glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0);
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 16);

        glVertexArrayElementBuffer(vao, ebo);

        var width = 1.0F;
        var height = 1.0F;
        float[] vertices = {
                -width, -height, 0.0F, 1.0F, 0.0F, 1.0F,
                 width, -height, 0.0F, 1.0F, 1.0F, 1.0F,
                 width,  height, 0.0F, 1.0F, 1.0F, 0.0F,
                -width,  height, 0.0F, 1.0F, 0.0F, 0.0F
        };
        int[] elements = {
                0, 1, 2,
                2, 3, 0
        };

        var flags = 0;
        glNamedBufferStorage(vbo, vertices, flags);
        glNamedBufferStorage(ebo, elements, flags);


        var texture = Texture.create(getPath());
        glBindTextureUnit(2, texture);
    }

    public void step() {
        steps++;
        if (steps % 20 == 0) {
            time++;
        }
        if (time > 8) {
            ready = true;
        }
    }

    public void draw() {
        if (!ready) {
            glProgramUniform1i(fragment, location, 2);

            pipeline.bind();
            glBindVertexArray(vao);

            glEnableVertexArrayAttrib(vao, 0);
            glEnableVertexArrayAttrib(vao, 1);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT,  0);

            glDisableVertexArrayAttrib(vao, 0);
            glDisableVertexArrayAttrib(vao, 1);

            glBindVertexArray(0);
            ProgramPipeline.unbind();
        }
    }

    public boolean isReady() {
        return ready;
    }

    private static String getPath() {
        String[] paths = {
                "textures/splash/splash_0.png",
                "textures/splash/splash_1.png",
                "textures/splash/splash_2.png"
        };
        return paths[ThreadLocalRandom.current().nextInt(paths.length)];
    }
}
