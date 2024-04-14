package com.harleylizard.space;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
