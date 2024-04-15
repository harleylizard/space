package com.harleylizard.space.graphics.vertex;

import com.harleylizard.space.graphics.texture.Material;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;

public sealed interface VertexParameters permits VaryingVertexParameters {

    void vertex(Matrix4f matrix4f, float x, float y, float z, float u, float v, Material material);

    void triangulate();

    ByteBuffer getVertices();

    ByteBuffer getElements();

    int getCount();

    void free();
}
