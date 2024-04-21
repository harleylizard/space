package com.harleylizard.space.graphics.texture;

import com.harleylizard.space.Resources;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.nstbi_image_free;
import static org.lwjgl.stb.STBImage.nstbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class ModelTextures {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    private static boolean bound;

    private ModelTextures() {}

    public static void bind(int unit) {
        if (bound) {
            return;
        }
        try (var stack = MemoryStack.stackPush()) {
            var buffer = stack.callocInt(3);

            var texture = glCreateTextures(GL_TEXTURE_2D_ARRAY);

            glTextureParameteri(texture, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTextureParameteri(texture, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            var textures = TextureManager.getInstance().getPaths();

            var size = textures.size();
            glTextureStorage3D(texture, 2, GL_RGBA8, WIDTH, HEIGHT, size * 3);

            var i = 0;
            for (var path : textures) {
                var color = path.getColor();
                var image = Resources.readImage(Resources.get(color));
                var normalMap = NormalMap.from(color);
                loadImage(texture, buffer, image, i);
                loadImage(texture, buffer, normalMap, i + 1);
                if (path.hasEmissive()) {
                    var emissive = path.getEmissive();
                    loadImage(texture, buffer, Resources.readImage(Resources.get(emissive)), i + 2);
                }
                i += 3;
            }
            glGenerateTextureMipmap(texture);
            glBindTextureUnit(unit, texture);
            bound = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadImage(int texture, IntBuffer buffer, ByteBuffer image, int i) throws IOException {
        var pixels = nstbi_load_from_memory(memAddress(image), image.remaining(), memAddress(buffer), memAddress(buffer) + 4, memAddress(buffer) + 8, 4);
        if (pixels == NULL) {
            memFree(image);
            throw new RuntimeException("Failed to load pixels");
        }
        var width = buffer.get(0);
        var height = buffer.get(1);
        glTextureSubImage3D(texture, 0, 0, 0, i, width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        nstbi_image_free(pixels);
        memFree(image);
    }
}
