package com.harleylizard.space.graphics.modeler;

import com.harleylizard.space.Resources;
import com.harleylizard.space.block.Block;
import com.harleylizard.space.graphics.model.ModelReader;
import com.harleylizard.space.graphics.vertex.VaryingVertexParameters;
import com.harleylizard.space.modeler.ModelerBlocks;
import org.joml.Matrix4fStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL45.*;

public final class ModelerBackground {
    private final int vao = glCreateVertexArrays();
    private final int vbo = glCreateBuffers();
    private final int ebo = glCreateBuffers();

    private final int count;

    private ModelerBackground(int[] blocks, List<Block> palette) throws IOException {
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, VaryingVertexParameters.VERTEX_SIZE);
        glVertexArrayAttribBinding(vao, 0, 0);
        glVertexArrayAttribBinding(vao, 1, 0);
        glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0);
        glVertexArrayAttribFormat(vao, 1, 3, GL_FLOAT, false, 16);

        glVertexArrayElementBuffer(vao, ebo);

        var parameters = VaryingVertexParameters.create();
        var stack = new Matrix4fStack(2);
        stack.pushMatrix();
        for (int i = 0; i < blocks.length; i++) {
            stack.identity();

            var x = i % 32;
            var y = (i / 32) % 32;
            var z = i / (32 * 32);
            stack.translate(x, y, z);

            var block = palette.get(blocks[i]);
            var model = ModelReader.getModel(ModelerBlocks.REGISTRY, block);
            for (var shape : model) {
                shape.build(parameters, stack);
            }
        }
        stack.popMatrix();
        parameters.triangulate();

        count = parameters.getCount();

        var flags = 0;
        glNamedBufferStorage(vbo, parameters.getVertices(), flags);
        glNamedBufferStorage(ebo, parameters.getElements(), flags);
        parameters.free();
    }

    public void draw() {
        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);
        glEnableVertexArrayAttrib(vao, 1);

        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);

        glDisableVertexArrayAttrib(vao, 0);
        glDisableVertexArrayAttrib(vao, 1);
    }

    public static ModelerBackground of(String path) {
        try (var dataInput = new DataInputStream(Resources.get(path))) {
            var size = 32 * 32 * 32;
            var blocks = new int[size];

            for (var i = 0; i < size; i++) {
                blocks[i] = dataInput.readInt();
            }

            size = dataInput.readInt();
            var palette = new ArrayList<Block>(size);
            for (var i = 0; i < size; i++) {
                palette.add(ModelerBlocks.REGISTRY.get(dataInput.readUTF()));
            }
            return new ModelerBackground(blocks, Collections.unmodifiableList(palette));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
