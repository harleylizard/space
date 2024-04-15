package com.harleylizard.space.graphics.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harleylizard.space.Resources;
import com.harleylizard.space.graphics.texture.ModelTextures;
import com.harleylizard.space.graphics.vertex.VaryingVertexParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL45.*;

public final class ModelDisplay {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Model.class, Model.DESERIALIZER).create();

    private final Map<Model, Singular> map = new HashMap<>();

    private final ModelTextures textures = new ModelTextures();

    {
        textures.delegate("textures/dirt.png");
        textures.bind(0);
    }

    public Singular singular(Model model) {
        return map.computeIfAbsent(model, Singular::new);
    }

    public void and(Model model, VaryingVertexParameters parameters) {
        for (var shape : model) {
            shape.build(parameters);
        }
    }

    public Model read(String path) {
        try (var reader = new BufferedReader(new InputStreamReader(Resources.get(path)))) {
            return gson.fromJson(reader, Model.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Singular {
        private final int vao = glCreateVertexArrays();
        private final int vbo = glCreateBuffers();
        private final int ebo = glCreateBuffers();

        private final int count;

        private Singular(Model model) {
            glVertexArrayVertexBuffer(vao, 0, vbo, 0, (4 + 2) * 4);
            glVertexArrayAttribBinding(vao, 0, 0);
            glVertexArrayAttribBinding(vao, 1, 0);
            glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0);
            glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 16);

            glVertexArrayElementBuffer(vao, ebo);

            var parameters = VaryingVertexParameters.create();
            for (var shape : model) {
                shape.build(parameters);
            }
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
    }
}
