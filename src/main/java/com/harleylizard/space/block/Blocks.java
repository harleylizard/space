package com.harleylizard.space.block;

import com.harleylizard.space.math.BoundingBox;
import com.harleylizard.space.registry.Registry;

public final class Blocks {
    public static final Block MODELER_AIR = new Block(null);
    public static final Block MODELER_STEM = new Block(Block.createBlock());
    public static final Block MODELER_CAP = new Block(Block.createBlock());
    public static final Block MODELER_DIRT = new Block(Block.createBlock());
    public static final Block MODELER_GRASS = new Block(Block.createBlock());
    public static final Block MODELER_WILD_GRASS = new Block(null);
    public static final Block MODELER_MUSHROOM = new Block(null);
    public static final Block MODELER_GLOW = new Block(null);
    public static final Block MODELER_FLOOR = new Block(new BoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F));
    public static final Block MODELER_LIGHT = new Block(Block.createBlock());

    public static final Registry<Block> REGISTRY = new Registry.Builder<Block>()
            .register("modeler_air", MODELER_AIR)
            .register("modeler_stem", MODELER_STEM)
            .register("modeler_cap", MODELER_CAP)
            .register("modeler_dirt", MODELER_DIRT)
            .register("modeler_grass", MODELER_GRASS)
            .register("modeler_wild_grass", MODELER_WILD_GRASS)
            .register("modeler_mushroom", MODELER_MUSHROOM)
            .register("modeler_glow", MODELER_GLOW)
            .register("modeler_floor", MODELER_FLOOR)
            .register("modeler_light", MODELER_LIGHT)
            .build();

    private Blocks() {}
}
