package com.harleylizard.space.math;

public enum Direction implements Stringifiable {
    NORTH("north", new ImmutableVector3i(0, 0, -1)),
    EAST("east", new ImmutableVector3i(1, 0, 0)),
    SOUTH("south", new ImmutableVector3i(0, 0, 1)),
    WEST("west", new ImmutableVector3i(-1, 0, 0)),
    UP("up", new ImmutableVector3i(0, 1, 0)),
    DOWN("down", new ImmutableVector3i(0, -1, 0));

    private final String name;

    private final ImmutableVector3i normal;

    Direction(String name, ImmutableVector3i normal) {
        this.name = name;
        this.normal = normal;
    }

    public ImmutableVector3i getNormal() {
        return normal;
    }

    @Override
    public String stringify() {
        return name;
    }
}
