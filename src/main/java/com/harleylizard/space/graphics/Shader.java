package com.harleylizard.space.graphics;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL41.GL_FRAGMENT_SHADER_BIT;
import static org.lwjgl.opengl.GL41.GL_VERTEX_SHADER_BIT;

public enum Shader {
    FRAGMENT("fragment", GL_FRAGMENT_SHADER, GL_FRAGMENT_SHADER_BIT),
    VERTEX("vertex", GL_VERTEX_SHADER, GL_VERTEX_SHADER_BIT);

    private final String name;
    private final int type;
    private final int bit;

    Shader(String name, int type, int bit) {
        this.name = name;
        this.type = type;
        this.bit = bit;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getBit() {
        return bit;
    }
}
