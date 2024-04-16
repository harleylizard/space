package com.harleylizard.space.graphics.vertex;

import com.harleylizard.space.graphics.texture.Material;
import com.harleylizard.space.math.ImmutableVector3i;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;

public sealed interface VertexParameters permits VaryingVertexParameters {

    void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, Material material, float nx, float ny, float nz);

    void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, Material material, ImmutableVector3i normal);

    void triangulate();

    ByteBuffer getVertices();

    ByteBuffer getElements();

    int getCount();

    void free();
}
