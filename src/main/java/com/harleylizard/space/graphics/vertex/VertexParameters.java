package com.harleylizard.space.graphics.vertex;

import com.harleylizard.space.math.ImmutableVector3i;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;

public sealed interface VertexParameters permits VaryingVertexParameters {

    void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, int t, float nx, float ny, float nz, int i, boolean ambient);

    void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, int t, ImmutableVector3i normal, int i);

    void triangulate();

    ByteBuffer getVertices();

    ByteBuffer getElements();

    int getVerticesInBytes();

    int getElementsInBytes();

    int getCount();

    void free();
}
