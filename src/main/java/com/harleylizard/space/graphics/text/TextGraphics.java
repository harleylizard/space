package com.harleylizard.space.graphics.text;

import com.harleylizard.space.Window;
import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.graphics.Texture;
import com.harleylizard.space.graphics.UniformBuffer;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class TextGraphics {
    ProgramPipeline pipeline = new ProgramPipeline.Builder()
            .useProgram(Shader.FRAGMENT, "shaders/text_fragment.glsl")
            .useProgram(Shader.VERTEX, "shaders/text_vertex.glsl")
            .useBuffer(Shader.VERTEX, "bufferStorage", 1)
            .build();
    private final int fragment = pipeline.getProgram(Shader.FRAGMENT);
    private final int location = glGetUniformLocation(fragment, "sampler");

    private final int ssbo = pipeline.getBuffer("bufferStorage");


    private final int vao = glCreateVertexArrays();

    private final int texture = Texture.create("textures/font/english.png");

    {
        glBindTextureUnit(1, texture);

        var vbo = glCreateBuffers();
        var ebo = glCreateBuffers();
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

    public void draw(Window window, List<MutableText> list, Matrix4f projection, Matrix4f view, Matrix4f model) {
        upload(list);

        var aspectRatio = window.getAspectRatio();
        var fovy = 28.75F;

        projection.identity();
        projection.ortho(-fovy * aspectRatio, fovy * aspectRatio, -fovy, fovy, 1.0F, -1.0F);

        view.identity();

        model.identity();

        UniformBuffer.uploadMatrices(projection, view, model);

        glProgramUniform1i(fragment, location, 1);

        pipeline.bind();

        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        var size = getCharSize(list);
        glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, size);

        glDisableVertexArrayAttrib(vao, 0);

        glBindVertexArray(0);

        ProgramPipeline.unbind();
    }

    private void upload(List<MutableText> list) {
        var buffer = memCalloc(((16 + 4) * 4) * getCharSize(list));

        var matrix4f = new Matrix4f();

        var j = 0;
        for (var text : list) {
            var string = text.stringify();
            if (string == null || string.isEmpty()) {
                continue;
            }

            var x = text.getX();
            var y = text.getY();
            var font = text.getFont();

            for (var i = 0; i < string.length(); i++) {
                var c = string.charAt(i);
                matrix4f.identity();
                matrix4f.translate(x + i, y, 0.0F);

                var coordinates = font.getCoordinates(c);

                var offset = ((16 + 4) * 4) * j;
                matrix4f.get(offset, buffer);

                var n = offset + 16 * 4;
                buffer.putFloat(n, coordinates.getMinX());
                buffer.putFloat(n + 4, coordinates.getMinY());
                buffer.putFloat(n + 8, coordinates.getMaxX());
                buffer.putFloat(n + 12, coordinates.getMaxY());
                j++;
            }
        }

        glNamedBufferData(ssbo, buffer, GL_STREAM_DRAW);
        memFree(buffer);
    }

    private int getCharSize(List<MutableText> list) {
        var i = 0;
        for (var text : list) {
            var string = text.stringify();
            if (string != null && !string.isEmpty()) {
                i += string.length();
            }
        }
        return i;
    }
}
