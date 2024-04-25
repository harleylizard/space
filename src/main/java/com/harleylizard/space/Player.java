package com.harleylizard.space;

import com.harleylizard.space.graphics.debug.MutableScene;
import com.harleylizard.space.input.Keyboard;
import com.harleylizard.space.input.Mouse;
import com.harleylizard.space.math.BoundingBox;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class Player {
    private static final float SENSITIVITY = Math.toRadians(0.15F);

    private final BoundingBox boundingBox = new BoundingBox(-0.25F, -1.5F, -0.25F, 0.25F, 0.45F, 0.25F);

    private final Vector3f position = new Vector3f().add(15.0F, 7.0F, 15.0F);
    private final Vector3f velocity = new Vector3f();

    private final Quaternionf rotation = new Quaternionf();

    private double lastX;
    private double lastY;
    private float pitch;
    private float yaw;

    public void step(Keyboard keyboard, Mouse mouse, float time) {
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

        var gravity = 3.0F * time;
        velocity.y -= gravity;

        move(keyboard, time);
        move(time);
    }

    private void move(Keyboard keyboard, float time) {
        var speed = 2.0F * time;
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
            //velocity.y += speed;
        }
        if (keyboard.isPressed(GLFW_KEY_LEFT_SHIFT)) {
            //velocity.y -= speed;
        }
    }

    private void move(float time) {
        var friction = 1.45F;
        velocity.div(friction);
        collide(time);
        position.add(velocity);
    }

    private void collide(float time) {
        var futureX = position.x + velocity.x;
        var futureY = position.y + velocity.y;
        var futureZ = position.z + velocity.z;

        var boundingBoxX = this.boundingBox.move(futureX, position.y, position.z);
        var boundingBoxY = this.boundingBox.move(position.x, futureY, position.z);
        var boundingBoxZ = this.boundingBox.move(position.x, position.y, futureZ);

        var radius = 8;
        for (var x = -radius; x < radius; x++) for (var y = -radius; y < radius; y++) for (var z = -radius; z < radius; z++) {
            var j = x + (int) Math.floor(position.x);
            var k = y + (int) Math.floor(position.y);
            var l = z + (int) Math.floor(position.z);
            var block = MutableScene.SCENE.getBlock(j, k, l);
            var colliding = block.getBoundingBox();
            if (colliding != null) {
                var moved = colliding.move(j, k, l);
                if (boundingBoxX.intersects(moved)) {
                    velocity.x = 0.0F;
                }
                if (boundingBoxY.intersects(moved)) {
                    velocity.y = 0.0F;
                }
                if (boundingBoxZ.intersects(moved)) {
                    velocity.z = 0.0F;
                }
            }
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }
}
