package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.CullGetter;
import com.harleylizard.space.graphics.vertex.VertexParameters;
import org.joml.Matrix4fStack;

public sealed interface Shape permits Cube, Plane {

    void build(CullGetter cullGetter, VertexParameters parameters, Matrix4fStack stack, int x, int y, int z, boolean ambient);
}
