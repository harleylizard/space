package com.harleylizard.space;

import com.harleylizard.space.input.Keyboard;
import com.harleylizard.space.input.Mouse;
import com.harleylizard.space.math.BoundingBox;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class Player {
    private static final float SENSITIVITY = Math.toRadians(0.15F);

    private final BoundingBox boundingBox = new BoundingBox(-0.5F, -1.5F, -0.5F, 0.5F, 0.5F, 0.5F);

    private final BoundingBox toCollideWith = new BoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F).move(15.0F, 4.0F, 15.0F);

    private final Vector3f position = new Vector3f().add(1.0F, 5.0F, 0.0F);
    private final Vector3f velocity = new Vector3f();

    private final Quaternionf rotation = new Quaternionf();

    private double lastX;
    private double lastY;
    private float pitch;
    private float yaw;

    public void step(Keyboard keyboard, Mouse mouse, float time) {
        var boundingBox = this.boundingBox.move(position.x, position.y, position.z);

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

        var gravity = 0.075F * time;
        // velocity.y -= gravity;

        move(keyboard, time);
        move(time);
    }

    private void move(Keyboard keyboard, float time) {
        var speed = 4.5F * time;
        var sin = Math.sin(yaw);
        var cos = Math.cos(yaw);

        if (keyboard.isPressed(GLFW_KEY_W)) {
            velocity.z -= speed * cos;
            velocity.x += speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_A)) {
            velocity.x -= speed * cos;
            velocity.z -= speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_S)) {
            velocity.z += speed * cos;
            velocity.x -= speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_D)) {
            velocity.x += speed * cos;
            velocity.z += speed * sin;
        }
        if (keyboard.isPressed(GLFW_KEY_SPACE)) {
            velocity.y += speed;
        }
        if (keyboard.isPressed(GLFW_KEY_LEFT_SHIFT)) {
            velocity.y -= speed;
        }
    }

    private void move(float time) {
        var friction = 1.6F;
        velocity.div(friction);

        var futureX = position.x + velocity.x;
        var futureY = position.y + velocity.y;
        var futureZ = position.z + velocity.z;

        var boundingBoxX = this.boundingBox.move(futureX, position.y, position.z);
        var boundingBoxY = this.boundingBox.move(position.x, futureY, position.z);
        var boundingBoxZ = this.boundingBox.move(position.x, position.y, futureZ);

        if (boundingBoxX.intersects(toCollideWith)) {
            velocity.x = 0.0F;
        }
        if (boundingBoxY.intersects(toCollideWith)) {
            velocity.y = 0.0F;
        }
        if (boundingBoxZ.intersects(toCollideWith)) {
            velocity.z = 0.0F;
        }

        position.add(velocity);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }
}
