package com.harleylizard.space.graphics.vertex;

import java.nio.ByteBuffer;

public sealed interface VertexParameters permits VaryingVertexParameters {

    void vertex(float x, float y, float z, float u, float v);

    void triangulate();

    ByteBuffer getVertices();

    ByteBuffer getElements();

    int getCount();

    void free();
}
