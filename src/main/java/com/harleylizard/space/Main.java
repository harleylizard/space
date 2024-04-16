package com.harleylizard.space;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Quad;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.graphics.modeler.ModelerGraphics;
import com.harleylizard.space.graphics.texture.ModelTextures;
import com.harleylizard.space.input.Keyboard;
import com.harleylizard.space.input.Mouse;
import com.harleylizard.space.modeler.Modeler;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11C.*;

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

            var projection = new Matrix4f();
            var view = new Matrix4f();
            var model = new Matrix4f();

            var modeler = new Modeler();
            var modelerGraphics = new ModelerGraphics();

            ModelTextures.bind(0);

            System.gc();

            var player = new Player();

            var keyboard = new Keyboard();
            var mouse = new Mouse();
            window.setInput(keyboard, mouse);

            //glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            while (!window.shouldClose()) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                var aspectRatio = window.getAspectRatio();
                var fovy = (float) Math.toRadians(70.0F);

                projection.identity();
                projection.perspective(fovy, aspectRatio,  0.01F, 100.0F);

                player.step(keyboard, mouse);

                modelerGraphics.draw(player, modeler, projection, view, model);

                window.refresh();
            }
        } finally {
            window.destroy();
            glfwTerminate();
        }
    }
}
