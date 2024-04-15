package com.harleylizard.space.graphics.texture;

import java.util.ArrayList;
import java.util.List;

public final class ModelTextures {
    private final List<String> textures = new ArrayList<>();

    public void delegate(String path) {
        textures.add(path);
    }

    public void create() {

    }
}
