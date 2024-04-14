package com.harleylizard.space;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Quad;
import com.harleylizard.space.graphics.Shader;

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

            glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

            while (!window.shouldClose()) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                program.bind();
                quad.draw();
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
