package com.harleylizard.space.world;

import com.harleylizard.space.block.Block;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class Spaceship implements RotatableSpaceship {
    private final Vector3f position = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();

    @Override
    public Block getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
    }

    @Override
    public void removeBlock(int x, int y, int z) {

    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public Quaternionf getRotation() {
        return rotation;
    }
}
