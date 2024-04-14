package com.harleylizard.space;

import com.harleylizard.space.graphics.Quad;

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

            glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

            while (!window.shouldClose()) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                quad.draw();

                window.refresh();
            }
        } finally {
            window.destroy();
            glfwTerminate();
        }
    }
}
