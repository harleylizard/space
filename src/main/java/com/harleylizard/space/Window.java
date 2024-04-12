package com.harleylizard.space;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class Window {
    private final long window;

    {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, GLFW_RELEASE_BEHAVIOR_FLUSH);

        if ((window = glfwCreateWindow(854, 480, "Space", NULL, NULL)) == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create GLFW Window");
        }
        centre();

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    private void centre() {
        try (var stack = MemoryStack.stackPush()) {
            var buffer = stack.callocInt(6);

            long monitor = glfwGetPrimaryMonitor();
            nglfwGetMonitorWorkarea(monitor, memAddress(buffer), memAddress(buffer) + 4, memAddress(buffer) + 8, memAddress(buffer) + 12);
            nglfwGetWindowSize(window, memAddress(buffer) + 16, memAddress(buffer) + 20);

            var width = buffer.get(4);
            var height = buffer.get(5);

            glfwSetWindowPos(window, (buffer.get(2) - width) / 2, (buffer.get(3) - height) / 2);
        }
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void refresh() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}
