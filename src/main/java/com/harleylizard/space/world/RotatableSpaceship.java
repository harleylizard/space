package com.harleylizard.space.world;

import com.harleylizard.space.block.Block;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public sealed interface RotatableSpaceship permits Spaceship {

    Block getBlock(int x, int y, int z);

    void setBlock(int x, int y, int z, Block block);

    void removeBlock(int x, int y, int z);

    Vector3f getPosition();

    Quaternionf getRotation();
}
