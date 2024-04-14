package com.harleylizard.space.graphics.shape;

import com.harleylizard.space.graphics.vertex.VertexParameters;

public sealed interface Shape permits Cube, Plane {

    void build(VertexParameters parameters);
}
