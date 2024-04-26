package com.harleylizard.space.block;

import com.harleylizard.space.math.BoundingBox;
import com.harleylizard.space.registry.Registry;

public final class Blocks {
    public static final Block AIR = new Block(new Properties.Builder().canCollide(false).build());
    public static final Block STEM = new Block(Properties.createDefault());
    public static final Block CAP = new Block(Properties.createDefault());
    public static final Block DIRT = new Block(Properties.createDefault());
    public static final Block GRASS = new Block(Properties.createDefault());
    public static final Block WILD_GRASS = new Block(Properties.copy(AIR));
    public static final Block MUSHROOM = new Block(Properties.copy(AIR));
    public static final Block GLOW = new Block(Properties.copy(AIR));
    public static final Block SLAB = new Block(new Properties.Builder().setBoundingBox(new BoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F)).build());
    public static final Block WHITE_LIGHT = new Block(Properties.createDefault());

    public static final Registry<Block> REGISTRY = new Registry.Builder<Block>()
            .register("air", AIR)
            .register("stem", STEM)
            .register("cap", CAP)
            .register("dirt", DIRT)
            .register("grass", GRASS)
            .register("wild_grass", WILD_GRASS)
            .register("mushroom", MUSHROOM)
            .register("glow", GLOW)
            .register("slab", SLAB)
            .register("white_light", WHITE_LIGHT)
            .build();

    private Blocks() {}
}
