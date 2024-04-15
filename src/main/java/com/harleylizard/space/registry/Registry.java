package com.harleylizard.space.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Registry<T> {
    private final Map<T, String> map;
    private final Map<String, T> inverse;

    private Registry(Map<T, String> map) {
        this.map = map;
        var inverse = new HashMap<String, T>(map.size());
        for (var entry : map.entrySet()) {
            inverse.put(entry.getValue(), entry.getKey());
        }
        this.inverse = Collections.unmodifiableMap(inverse);
    }

    public String get(T t) {
        return map.get(t);
    }

    public T get(String string) {
        return inverse.get(string);
    }

    public static final class Builder<T> {
        private final Map<T, String> map = new HashMap<>();

        public Builder<T> register(String name, T t) {
            map.put(t, name);
            return this;
        }

        public Registry<T> build() {
            return new Registry<>(Collections.unmodifiableMap(map));
        }
    }
}
