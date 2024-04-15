package com.harleylizard.space.graphics.texture;

import com.harleylizard.space.Resources;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class NormalMap {
    private static final Path CACHE = Paths.get(".cache");

    private NormalMap() {}

    public static ByteBuffer from(String path) throws IOException {
        if (!Files.isDirectory(CACHE)) {
            Files.createDirectories(CACHE);
        }
        var fileName = path.substring(path.lastIndexOf("/") + 1);

        var systemPath = CACHE.resolve(fileName);
        if (!Files.isRegularFile(systemPath)) {
            return create(path, systemPath);
        } else {
            try (var stream = Files.newInputStream(systemPath)) {
                return Resources.readImage(stream);
            }
        }
    }

    private static ByteBuffer create(String path, Path systemPath) throws IOException {
        try (var stack = MemoryStack.stackPush(); var outputStream = Files.newOutputStream(systemPath)) {
            var widthBuffer = stack.callocInt(1);
            var heightBuffer = stack.callocInt(1);
            var channels = stack.callocInt(1);

            var image = Resources.readImage(Resources.get(path));
            var pixels = stbi_load_from_memory(image, widthBuffer, heightBuffer, channels, 4);
            if (pixels == null) {
                memFree(image);
                throw new RuntimeException("Failed to load stbi pixels");
            }
            var width = widthBuffer.get(0);
            var height = heightBuffer.get(0);

            var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (var x = 0; x < width; x++) {
                for (var y = 0; y < height; y++) {
                    var colorLeft = getRgb(pixels, width, Math.max(x - 1, 0), y);
                    var colorRight = getRgb(pixels, width, Math.min(x + 1, width - 1), y);
                    var colorUp = getRgb(pixels, width, x, Math.max(y - 1, 0));
                    var colorDown = getRgb(pixels, width, x, Math.min(y + 1, height - 1));

                    var dx = (colorRight >> 16 & 0xFF) / 255.0F - (colorLeft >> 16 & 0xFF) / 255.0F;
                    var dy = (colorDown >> 16 & 0xFF) / 255.0F - (colorUp >> 16 & 0xFF) / 255.0F;
                    var dz = 1.0f;

                    var length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
                    dx /= length;
                    dy /= length;
                    dz /= length;

                    var color = pack((int) ((dx + 1) * 0.5F * 255), (int) ((dy + 1) * 0.5F * 255), (int) ((dz + 1) * 0.5F * 255));
                    bufferedImage.setRGB(x, y, color);
                }
            }

            ImageIO.write(bufferedImage, "png", outputStream);
            outputStream.flush();
            stbi_image_free(pixels);
            memFree(image);

            try (var inputStream = Files.newInputStream(systemPath)) {
                return Resources.readImage(inputStream);
            }
        }
    }

    private static int getRgb(ByteBuffer pixels, int width, int x, int y) {
        var i = (x + y * width) * 4;
        var r = pixels.get(i) & 0xFF;
        var g = pixels.get(i + 1) & 0xFF;
        var b = pixels.get(i + 2) & 0xFF;
        return (r << 16) | (g << 8) | b;
    }

    private static int pack(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }
}
