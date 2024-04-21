package com.harleylizard.space.graphics;

import com.harleylizard.space.Resources;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.nstbi_image_free;
import static org.lwjgl.stb.STBImage.nstbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class Texture {

    private Texture() {}

    public static int create(String path) {
        try (var stack = MemoryStack.stackPush()) {
            var buffer = stack.callocInt(3);

            var texture = glCreateTextures(GL_TEXTURE_2D);
            glTextureParameteri(texture, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTextureParameteri(texture, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            var image = Resources.readImage(path);
            var pixels = nstbi_load_from_memory(memAddress(image), image.remaining(), memAddress(buffer), memAddress(buffer) + 4, memAddress(buffer) + 8, 4);

            var width = buffer.get(0);
            var height = buffer.get(1);

            glTextureStorage2D(texture, 1, GL_RGBA8, width, height);
            glTextureSubImage2D(texture, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

            memFree(image);
            nstbi_image_free(pixels);
            return texture;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
