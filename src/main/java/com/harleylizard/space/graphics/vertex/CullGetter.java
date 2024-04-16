package com.harleylizard.space.graphics.vertex;

import com.harleylizard.space.math.Direction;

public interface CullGetter {

    boolean shouldCull(int x, int y, int z, Direction direction);
}
