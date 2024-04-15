package com.harleylizard.space;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Quad;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.graphics.UniformBuffer;
import com.harleylizard.space.graphics.model.ModelDisplay;
import com.harleylizard.space.graphics.texture.NormalMap;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialise GLFW");
        }

        var window = new Window();

        try {
            var quad = new Quad();

            var program = new ProgramPipeline.Builder()
                    .useProgram(Shader.FRAGMENT, "shaders/quad_fragment.glsl")
                    .useProgram(Shader.VERTEX, "shaders/quad_vertex.glsl")
                    .build();
            var modelProgram = new ProgramPipeline.Builder()
                    .useProgram(Shader.FRAGMENT, "shaders/model_fragment.glsl")
                    .useProgram(Shader.VERTEX, "shaders/model_vertex.glsl")
                    .build();

            var projection = new Matrix4f();
            var view = new Matrix4f();
            var model = new Matrix4f();

            var modelDisplay = new ModelDisplay();
            var cubeModel = modelDisplay.read("models/cube.json");
            var singular = modelDisplay.singular(cubeModel);

            glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

            var angle = 0.0F;

            glEnable(GL_CULL_FACE);

            var normalMap = NormalMap.from("textures/dirt.png");

            memFree(normalMap);

            while (!window.shouldClose()) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                var aspectRatio = window.getAspectRatio();
                var fovy = (float) Math.toRadians(70.0F);

                projection.identity();
                projection.perspective(fovy, aspectRatio,  0.01F, 100.0F);

                view.identity();
                view.translate(0.0F, 0.0F, -10.0F);

                model.identity();
                model.rotate((float) Math.toRadians(angle++), 0.0F, 1.0F, 0.0F);

                UniformBuffer.uploadMatrices(projection, view, model);

                modelProgram.bind();
                singular.draw();
                Quad.unbind();
                ProgramPipeline.unbind();

                window.refresh();
            }
        } finally {
            window.destroy();
            glfwTerminate();
        }
    }
}
