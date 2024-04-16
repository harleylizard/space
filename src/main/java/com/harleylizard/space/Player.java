package com.harleylizard.space;

import com.harleylizard.space.input.Keyboard;
import com.harleylizard.space.input.Mouse;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class Player {
    private static final float SENSITIVITY = Math.toRadians(0.15F);

    private final Vector3f position = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();

    private double lastX;
    private double lastY;
    private float pitch;
    private float yaw;

    public void step(Keyboard keyboard, Mouse mouse) {
        var x = mouse.getX();
        var y = mouse.getY();

        var deltaX = (float) (x - lastX) * SENSITIVITY;
        var deltaY = (float) (y - lastY) * SENSITIVITY;

        pitch += deltaY;
        yaw += deltaX;

        var max = (float) Math.toRadians(90.0);
        pitch = Math.clamp(-max, max, pitch);

        rotation.identity();
        rotation.rotateX(pitch);
        rotation.rotateY(yaw);

        lastX = x;
        lastY = y;

        move(keyboard);
    }

    private void move(Keyboard keyboard) {
        var speed = 0.1F;
        var sin = Math.sin(yaw);
        var cos = Math.cos(yaw);

        if (keyboard.isPressed(GLFW_KEY_W)) {
            position.z -= speed * cos;
            position.x += speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_A)) {
            position.x -= speed * cos;
            position.z -= speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_S)) {
            position.z += speed * cos;
            position.x -= speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_D)) {
            position.x += speed * cos;
            position.z += speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_SPACE)) {
            position.y += speed;
        }
        if (keyboard.isPressed(GLFW_KEY_LEFT_SHIFT)) {
            position.y -= speed;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }
}
