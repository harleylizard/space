package com.harleylizard.space;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialise GLFW");
        }

        var window = new Window();

        try {

            while (!window.shouldClose()) {

                window.refresh();
            }
        } finally {
            window.destroy();
            glfwTerminate();
        }
    }
}
