package com.harleylizard.space;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memRealloc;

public final class Resources {

    private Resources() {}

    public static InputStream get(String path) throws IOException {
        var url = Resources.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new RuntimeException("Missing file %s".formatted(path));
        }
        return url.openStream();
    }

    public static String readString(String path) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(get(path)))) {
            var builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            return builder.toString();
        }
    }

    public static ByteBuffer readImage(InputStream stream) throws IOException {
        try (var channel = Channels.newChannel(stream)) {
            var buffer = memCalloc(1024 * 4);

            while(channel.read(buffer) != -1) {
                if (buffer.remaining() == 0) {
                    buffer = memRealloc(buffer, buffer.capacity() * 2);
                }
            }
            return buffer.flip();
        }
    }
}
