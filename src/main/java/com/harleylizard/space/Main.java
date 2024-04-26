package com.harleylizard.space;

import com.harleylizard.space.graphics.*;
import com.harleylizard.space.graphics.debug.DebugGraphics;
import com.harleylizard.space.graphics.text.English;
import com.harleylizard.space.graphics.text.MutableText;
import com.harleylizard.space.graphics.text.TextGraphics;
import com.harleylizard.space.input.Keyboard;
import com.harleylizard.space.input.Mouse;
import org.joml.Matrix4f;

import java.util.ArrayList;

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
            var pipeline = new ProgramPipeline.Builder()
                    .useProgram(Shader.FRAGMENT, "shaders/quad_fragment.glsl")
                    .useProgram(Shader.VERTEX, "shaders/quad_vertex.glsl")
                    .build();

            var projection = new Matrix4f();
            var view = new Matrix4f();
            var model = new Matrix4f();

            var debugGraphics = new DebugGraphics();

            var texts = new ArrayList<MutableText>();
            var textGraphics = new TextGraphics();

            var english = new English();
            var fps = new MutableText(english, -46, 26);
            var copyright = new MutableText(english, -25, -26);
            copyright.set("Copyright 2024. Harley Oswald, All Rights Reserved.");

            var version = new MutableText(english, -46, 24);
            version.set("version 1.0 alpha");

            texts.add(version);
            texts.add(fps);
            texts.add(copyright);

            var splashGraphics = new SplashGraphics();

            fps.set("FPS 0");

            System.gc();

            var player = new Player();

            var keyboard = new Keyboard();
            var mouse = new Mouse();
            window.setInput(keyboard, mouse);

            // glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            var targetFps = 60;
            var targetTime = 1000000000 / targetFps;

            var previousTime = System.nanoTime();
            var delta = 0.0D;

            var fpsTimer = System.currentTimeMillis();
            var frames = 0;

            while (!window.shouldClose()) {
                var currentTime = System.nanoTime();
                var elapsedTime = currentTime - previousTime;

                previousTime = currentTime;

                delta += elapsedTime / (double) targetTime;

                while (delta >= 1.0D) {
                    splashGraphics.step();
                    if (splashGraphics.isReady()) {
                        var time = (float) delta / (float) targetFps;
                        player.step(keyboard, mouse, time);
                        debugGraphics.step(time);
                    }

                    delta--;
                }

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                if (splashGraphics.isReady()) {
                    var aspectRatio = window.getAspectRatio();
                    var fovy = (float) Math.toRadians(70.0F);

                    projection.identity();
                    projection.perspective(fovy, aspectRatio,  0.01F, 100.0F);

                    debugGraphics.draw(window, player, projection, view, model);
                }

                // GUI
                var aspectRatio = window.getAspectRatio();
                var fovy = 28.75F;

                projection.identity();
                projection.ortho(-fovy * aspectRatio, fovy * aspectRatio, -fovy, fovy, 1.0F, -1.0F);

                view.identity();
                model.identity();

                UniformBuffer.uploadMatrices(projection, view, model);

                splashGraphics.draw();
                textGraphics.draw(texts);

                window.refresh();

                frames++;

                if (System.currentTimeMillis() - fpsTimer > 1000) {
                    fpsTimer += 1000;
                    fps.set("FPS %d".formatted(frames));
                    frames = 0;
                }

            }
        } finally {
            window.destroy();
            glfwTerminate();
        }
    }
}
