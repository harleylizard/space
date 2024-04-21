package com.harleylizard.space.block;

import com.harleylizard.space.registry.Registry;

public final class Blocks {
    public static final Block MODELER_AIR = new Block();
    public static final Block MODELER_STEM = new Block();
    public static final Block MODELER_CAP = new Block();
    public static final Block MODELER_DIRT = new Block();
    public static final Block MODELER_GRASS = new Block();
    public static final Block MODELER_WILD_GRASS = new Block();
    public static final Block MODELER_MUSHROOM = new Block();
    public static final Block MODELER_GLOW = new Block();
    public static final Block MODELER_FLOOR = new Block();
    public static final Block MODELER_LIGHT = new Block();

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
