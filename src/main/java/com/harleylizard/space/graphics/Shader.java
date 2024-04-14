package com.harleylizard.space.graphics;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL41.GL_FRAGMENT_SHADER_BIT;
import static org.lwjgl.opengl.GL41.GL_VERTEX_SHADER_BIT;

public enum Shader {
    FRAGMENT(GL_FRAGMENT_SHADER, GL_FRAGMENT_SHADER_BIT),
    VERTEX(GL_VERTEX_SHADER, GL_VERTEX_SHADER_BIT);

    private final int type;
    private final int bit;

    Shader(int type, int bit) {
        this.type = type;
        this.bit = bit;
    }

    public int getType() {
        return type;
    }

    public int getBit() {
        return bit;
    }
}
